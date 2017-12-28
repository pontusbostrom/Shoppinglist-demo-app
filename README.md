
# A simple shopping list manager demo application with Vaadin Flow

This project demonstrates how to build an application with Polymer and then 
also control that application from [Vaadin Flow](https://github.com/vaadin/flow/). The server-side of the project is a Spring 
boot project. Storage is handled by a PouchDB/CouchDB combinations, which allows
 handling of offline mode, as well as, sharing and syncing of shopping lists between users.
 
The project contains the following parts:
* A Polymer frontend. The skeleton for this application has been created by Polymer CLI. 
It can be used as is and in principle allows offline use. Push notifications are also sent when adding items to lists.
This frontend uses a REST-endpoint for authentication to the CouchDB and another for pushing. Note that a 
valid GCM sender id/api-key needs to be provided in order for push to work. 

* A REST backend. This is used by the Polymer application for authentication and pushing. A CouchDB instance need to be provided
for authenication and storing shopping lists.

* A Vaadin Flow application. This application provides the same functionality as the pure Polymer application except for push notifications. 
However, the views are managed by Vaadin Flow. The Flow servlet also serves all Polymer files, also for the pure Polymer app.

There are three configuration files: 
* config.js, for configuring the client-side
* manifest.json, the application manifest
* application.properties, for configuring the server-side


# Application usage

The application can be run as Java application (ApplicationInitializer). Then open [http://localhost:8080](http://localhost:8080) in the browser. 
The Vaadin Flow version is then available at [http://localhost:8080/flow](http://localhost:8080/flow)

The application can be used to manage and share shopping lists. One shopping list is used at the time. 
A shopping list can be shared among users. This is made possible by associating a sharing key with each list. 
Two users share the same list if both use the same sharing key.

The application has three views:

1. *Main view.* This view is used for editing shopping lists.
2. *Sharing view.* This view can be used to view and change the sharing key.
3. *Login view.* This view is used to login in users. This is done against the CouchDB backend. All users needs to be added as users there. 
When a user is logged in, then the lists changes are synced with the CouchDB instance on the server. Sharing the sharing key between users then enables sharing the lists. 
The application can be used offline without logging in also.

# Notes
The application has been tested with the following fork of Vaadin flow: [https://github.com/pontusbostrom/flow](https://github.com/pontusbostrom/flow). 
The fix in the fork has probably already been included in the real deal.

# Credits

The Polymer application shell has been created with [Polymer CLI](https://github.com/Polymer/polymer-cli)

Authentication against the backend is done using [PouchDB authentication](https://github.com/pouchdb-community/pouchdb-authentication). 
The js-file is included, the license is Apache License 2.0.  

The push notifications are very much based on various tutorials on push and service workers.
That part was written some time ago and I cannot remember the exact ones anymore.
