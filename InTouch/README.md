# In-Touch
**Developed over Android SDK**

In-Touch is a open source social network App templet.

  - Build over open-source/free **services** and **libraries**.
  - Ready to deploy code just set create your own *Firebase Instance*.
  - Proper refactored code base for easy modification of ***Activities/Fragments/Models/Adapters/ViewHolders***.

# New Features!

  - Now user can create Account using email for **private chats** Only with Friends.
  - **Image compression library** is used to upload image faster.
  - **Offline image caching** of Picasso is enabled for faster image loads and less data usage.
  - Database references are also stored **offline** to increase load speed.
  - Friends **Online/*Lastseen*** Status are visible in Friends fragment.
  - **Push Notification** for new Friend request are enabled that lands directly to senders profile.
  - **Chat Message time** is added in chat bubble.


You can also:
>   - *change your Display pic and Status*.
>   - *Accept / Decline friend request directly from request fragment*. 
>   - **Crop** your image before upload.
>

# Upcoming Features!
  - Live **Group-Chat** feature.
  - Picture message.  
  - Landscape mode layout.
  - [Cloudinary](https://cloudinary.com) use to handle picture message.

# Signed APK Alpha build
Excited !!! lets check these feature on a test build APK (*Signed to remove conflicts*) .  Download apk from  **[In-Touch](https://github.com/g0621/Android-Projects/blob/master/InTouch/InTouch.apk)**

> The theme is little childish
> because i did't get much time to polish it
> used basic *cardView* and *Drawable* resource (*Available [Here](https://github.com/g0621/Android-Projects/tree/master/InTouch/app/src/main/res)* .)
> I will redesign the UI in next build because
> i am still going through material design documentation.

The User on Landing page were actually  my class mates. Their feedback was key part in debugging :) .

### Technology Used

In-Touch uses a number of open source projects to work properly:

* [Android Image Croper](https://github.com/ArthurHub/Android-Image-Cropper) - A free image cropping library.
* [Circular Image view](https://github.com/hdodenhof/CircleImageView) - Library to make Circular Image view Backward compatible.
* [Picasso](http://square.github.io/picasso/) - Image downloading and Caching Library.
* [Image Compressor](https://github.com/zetbaitsu/Compressor) - Image compressing library.
* [Firebase](https://firebase.google.com) - To handle backend.
* [OkHttp](http://square.github.io/okhttp/) - Picasso support Library to handle http connections.

And of course now In-Touch  itself is open source so can be used as base for more complex Social networking Apps, Hope soo :) lol just kidding again.

### Installation

In-Touch requires [SDK](https://developer.android.com/studio/releases/platforms.html) v26.0.2 to compile but use can change App level gradel file to compile with other SDK versions .

Install the dependencies, Sync the project and Hit the run button.



```sh
$ git clone https://github.com/g0621/Android-Projects/archive/master.zip
$ unzip Android-Projects.zip
$ cd In-Touch
$ cp google-services.json 
```

**NOTE :** Add index.js in Functions folder of ROOT directory to Your Firebase functions to activate push Notifications.

# Screenshots

| All Chats Fragment                       | Splash Screen                            | Change Status Activity                   | Chat Activity                            |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| ![All Chat Fragment](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/ListChatsByRecency.png?raw=true) | ![Splash Screen](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/SplashScreen.png?raw=false&width=200&height=200) | ![Change Status Activity](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/changeStatus.png?raw=true) | ![Chat Activity](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/chatActivity.png?raw=true) |

| Account Settings                         | Crop Image                               | Menu                                     | Push Notification                        |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| ![Account Settings](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/coustomizeProfile.png?raw=true) | ![Crop Image](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/cropImage.png?raw=true) | ![Menu](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/menu.png?raw=true) | ![Push Notification](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/notificationOnRequest.png?raw=true) |

| All Requests at one place                | Signup Page                              | Login Page                               | All friends at one place                 |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| ![Request Fragment](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/showsAllTheRequests.png?raw=true) | ![Signup Activity](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/signupActivity.png?raw=true) | ![Login Activity](https://github.com/g0621/Android-Projects/blob/master/InTouch/LoginActivity.png?raw=true) | ![Friend Fragment](https://github.com/g0621/Android-Projects/blob/master/InTouch/Screenshots/friendFrag.png?raw=true) |




### Development

Want to contribute? or have some ideas Great! .Lets work together
#### Contact Me
 - [Facebook](https://www.facebook.com/gyan199)
 - [Whatsapp Me](#) (+91) 8107066370 

### To-dos

 - Implement live Chat (group) almost done.
 - Redesign the Chat database Schema for faster loading .
 - Re-implement the UI/UX with material design.

License
----

MIT


**Free Software, Hell Yeah!** (*just mention the repo link*)
