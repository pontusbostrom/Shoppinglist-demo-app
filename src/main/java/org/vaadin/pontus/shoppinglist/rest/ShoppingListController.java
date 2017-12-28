package org.vaadin.pontus.shoppinglist.rest;

import java.security.Security;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.impl.StdCouchDbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.pontus.shoppinglist.data.PushSubscription;
import org.vaadin.pontus.shoppinglist.data.UserInfo;
import org.vaadin.pontus.shoppinglist.repositories.PushSubscriptionRepository;
import org.vaadin.pontus.shoppinglist.service.NotificationService;

@CrossOrigin
@RestController
public class ShoppingListController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    PushSubscriptionRepository pushSubscriptionRepo;

    @Autowired
    CouchDbInstance couchDb;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ShoppingListController() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @RequestMapping(path = "/auth", method = RequestMethod.POST)
    public ResponseEntity<Void> loginUser(@RequestBody UserInfo user) {

        logger.info("Adding user {} to database {}", user.getUsername(),
                user.getSharingKey());
        CouchDbConnector db = new StdCouchDbConnector(user.getSharingKey(),
                couchDb);
        db.createDatabaseIfNotExists();

        org.ektorp.Security sec = db.getSecurity();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (sec.getMembers() == null) {
            sec = new org.ektorp.Security();
        }
        if (!sec.getMembers().getNames().contains(user.getUsername())) {
            sec.getMembers().getNames().add(user.getUsername());
            db.updateSecurity(sec);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Merge the below with login
    @RequestMapping(path = "/subscription", method = RequestMethod.POST)
    public ResponseEntity<Void> createSubscription(
            @RequestBody PushSubscription sub) {

        String name = sub.getUser();
        logger.info("Registering notifications for list {} and user {}",
                sub.getSharingKey(), name);

        List<PushSubscription> subs = pushSubscriptionRepo
                .findBySharingKey(sub.getSharingKey());
        List<PushSubscription> subsForUser = subs.stream()
                .filter(s -> s.getUser().equals(name))
                .collect(Collectors.toList());
        if (subsForUser.isEmpty()) {
            sub.setId(UUID.randomUUID().toString());
            pushSubscriptionRepo.add(sub);
        } else {
            PushSubscription oldSub = subsForUser.get(0);
            oldSub.setEndpoint(sub.getEndpoint());
            oldSub.setAuth(sub.getAuth());
            oldSub.setKey(sub.getKey());
            pushSubscriptionRepo.update(oldSub);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);

    }

    @RequestMapping(path = "/pushnotification", method = RequestMethod.POST)
    public ResponseEntity<Void> push(@RequestBody UserInfo user) {

        String name = user.getUsername();
        logger.info("User {} starting to push", name);
        List<PushSubscription> subs = pushSubscriptionRepo
                .findBySharingKey(user.getSharingKey());

        notificationService.pushToSubscribers(name, subs);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
