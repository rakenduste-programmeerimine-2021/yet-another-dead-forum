# Yet Another Dead Forum

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/rakenduste-programmeerimine-2021/yet-another-dead-forum.svg)](https://github.com/rakenduste-programmeerimine-2021/yet-another-dead-forum/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/rakenduste-programmeerimine-2021/yet-another-dead-forum.svg)](https://github.com/rakenduste-programmeerimine-2021/yet-another-dead-forum/pulls)

## ☕ Authors:
* Marilii Saar
* Markus Tammeoja

## 💡 Idea:
A simple, dead forum.

## 🎨 Wireframe
[Wireframe](https://lucid.app/lucidchart/d2b2d9c6-6dbd-4ebd-8750-2ed59556a80b/edit?viewport_loc=927%2C-111%2C1664%2C791%2C0_0&invitationId=inv_2093a20f-be4d-4a9e-811f-ef1ee4aaaaf2 "Initial wireframe")

## 🪄 Technologies
* React
* Java (Spring)
* PostgreSQL

## 🛠 Development

### **Backend**
Make sure you have:
* Maven 
* Postgres (and pgAdmin)
* Java 11

Inside of `backend`'s root folder, open a CLI and enter the following:

```
./mvnw spring-boot:run
```

Alternatively, you can start it via your editor of choice, eg IntelliJ IDEA


### **Frontend**

Make sure you have:
* Node

Inside of `frontend`'s root folder, open a CLI and enter the following:

```
npm install         // installs the required dependencies (run once)
```

```
npm start
```

## 📄 Functionality
### For everyone
* Registration
* Logging in
* Emoji support
* Can view threads
* Can view posts
* Can donate
### For logged in users
* Logging out
* Changing your profile
* Viewing other people's profiles
* Creating a thread
* Creating a post
* Sending a private message
* Sending a private group message
* Viewing received messages
* Replying to received messages
* Deleting received messages
### For the author of a thread or post
* Editing their thread or post
### For admin
* Editing a user
* Deleting a user
* Deleting a thread
* Deleting a post
* Create a rank
* Edit a rank
* Delete a rank
* Add a rank to a user
* Change the website title
* Change the website colour scheme
* Turn maintenance mode on
* Turn maintenance mode off
* Change the maintenance mode message
* Add a global announcement banner with a message
