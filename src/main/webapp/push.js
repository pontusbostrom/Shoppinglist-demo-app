
var isSubscribed = false;

function initialiseState() {

    // Check if desktop notifications are supported
    if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
        console.warn('Notifications aren\'t supported.');
        return;
    }

    if (Notification.permission === 'denied') {
        console.warn('The user has blocked notifications.');
        return;
    }

    // Check is push API is supported
    if (!('PushManager' in window)) {
        console.warn('Push messaging isn\'t supported.');
        return;
    }

    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {

        serviceWorkerRegistration.pushManager.getSubscription().then(function (subscription) {

            isSubscribed = (subscription!=null);

        })
        .catch(function(err) {
            console.warn('Error during getSubscription()', err);
        });
    });
}


function subscribe(id, username) {
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {

        var sub = null;
        serviceWorkerRegistration.pushManager.getSubscription().then(function (subscription) {

            sub = subscription;

        }).catch(function(err) {
            console.warn('Error during getSubscription()', err);
        });

        if(sub!=null){
            return sendSubscriptionToServer(sub, id, username);
        }


        serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true}).then(function (subscription) {
            // Update the server state with the new subscription
            return sendSubscriptionToServer(subscription, id, username);
        })
        .catch(function (e) {
            if (Notification.permission === 'denied') {
                console.warn('Permission for Notifications was denied');
            } else {
                console.error('Unable to subscribe to push.', e);
            }
        });
    });
}


function sendSubscriptionToServer(subscription, id, username) {
    var key = subscription.getKey ? subscription.getKey('p256dh') : '';
    var auth = subscription.getKey ? subscription.getKey('auth') : '';

    return fetch(shoppinglist.properties.pushRegistrationEndpoint, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            endpoint: subscription.endpoint,
            user: username,
            sharingKey: id,
            // Take byte[] and turn it into a base64 encoded string suitable for
            // POSTing to a server over HTTP
            key: key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
            auth: auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : ''
        })
    });
}
