package org.vaadin.pontus.shoppinglist.repositories;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vaadin.pontus.shoppinglist.data.PushSubscription;

@Repository
public class PushSubscriptionRepository
extends CouchDbRepositorySupport<PushSubscription> {

    @Autowired
    public PushSubscriptionRepository(CouchDbConnector db) {
        super(PushSubscription.class, db);
        initStandardDesignDocument();
    }

    @GenerateView
    public List<PushSubscription> findBySharingKey(String key) {
        return queryView("by_sharingKey", key);
    }

}
