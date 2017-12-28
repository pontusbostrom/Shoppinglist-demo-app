function ChangeEvent(d) {
  this.type = 'pushevent';
  this.details = d;
};

self.addEventListener('push', function(event) {
  console.log('[Service Worker] Push Received.');
  console.log('[Service Worker] Push had this data: ' + event.data.text());

  var jsonStr = event.data.text();
  var message = JSON.parse(jsonStr);
  const options = {
    body: message.body,
    icon: 'images/icon.png',
    badge: 'images/badge.png'
  };

  event.waitUntil(clients.matchAll({
    includeUncontrolled: true,
    type: "window"
  }).then(function(clientList) {
    for (var i = 0; i < clientList.length; i++) {
      var client = clientList[i];
      if (client.url.endsWith('/index.html') && 'focused' in client) {
        if (client.focused || client.visibilityState == 'visible') {
          var json = JSON.parse(event.data.text());
          return client.postMessage(new ChangeEvent(message));
        }
      }
    }
    return self.registration.showNotification(message.title, options);

  }));

});

self.addEventListener('notificationclick', function(event) {
  console.log('On notification click: ', event.notification.tag);

  event.notification.close();

  event.waitUntil(clients.matchAll({
    includeUncontrolled: true,
    type: "window"
  }).then(function(clientList) {
    for (var i = 0; i < clientList.length; i++) {
      var client = clientList[i];
      if ( /*client.url.endsWith('/index.html') &&*/ 'focus' in client)
        return client.focus();
    }
    if (clients.openWindow) {
      return clients.openWindow('/main');
    }
  }).then(function(client) {
    return client.postMessage(new ChangeEvent({}));
  }));
});
