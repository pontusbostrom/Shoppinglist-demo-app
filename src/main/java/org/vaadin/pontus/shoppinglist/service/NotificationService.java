package org.vaadin.pontus.shoppinglist.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.vaadin.pontus.shoppinglist.data.PushMessage;
import org.vaadin.pontus.shoppinglist.data.PushSubscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Environment env;

    @Autowired
    public NotificationService(Environment env) {
        this.env = env;
    }

    public void pushToSubscribers(String name,
            List<PushSubscription> subscriptions) {

        logger.info("Staring to push with {} subscriptions",
                subscriptions.size());
        for (PushSubscription sub : subscriptions) {
            logger.info("Pushing for {}", sub.getUser());
            if (sub.getUser().equals(name)) {
                continue;
            }
            logger.info("Pushing with endpoint {}", sub.getEndpoint());
            PushMessage message = new PushMessage();
            message.setTitle("Shopping list changed");
            message.setBody("User " + name + " changed the shopping list.");
            message.setSharingId(sub.getSharingKey());
            message.setSender(name);

            ObjectMapper mapper = new ObjectMapper();
            String msg;
            try {
                msg = mapper.writeValueAsString(message);
                try {
                    sendPushMessage(sub, msg.getBytes("UTF-8"));
                } catch (InvalidKeyException | NoSuchProviderException
                        | NoSuchAlgorithmException | InvalidKeySpecException
                        | InvalidAlgorithmParameterException
                        | BadPaddingException | IllegalBlockSizeException
                        | NoSuchPaddingException | JoseException
                        | IOException e) {
                    logger.error("Failed to push", e);
                }

            } catch (JsonProcessingException e1) {
                logger.error("Could not construct push message", e1);
            }
        }
    }

    private void sendPushMessage(PushSubscription sub, byte[] payload)
            throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeySpecException, JoseException, IOException,
            InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, java.security.InvalidKeyException,
            NoSuchPaddingException {

        Notification notification;
        PushService pushService;

        // Create a notification with the endpoint, userPublicKey from the
        // subscription and a custom payload
        notification = new Notification(sub.getEndpoint(),
                sub.getUserPublicKey(), sub.getAuthAsBytes(), payload);

        // Instantiate the push service, no need to use an API key for Push API
        pushService = new PushService();
        pushService.setGcmApiKey(
                env.getProperty("org.pontus.shoppinglist.gcmKey"));

        // Send the notification
        HttpResponse httpResponse = pushService.send(notification);
        logger.info("Status code: {}",
                httpResponse.getStatusLine().getStatusCode());
        logger.info("Message: {}", IOUtils.toString(
                httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
    }

}
