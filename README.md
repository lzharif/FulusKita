# FulusKita
Fulus Kita is an open-source money management for business based on Firebase platform

# Background Story
Recently I just started a business with my partner. We worked just fine, and enjoy ourselves some orders and bucks. However, things started to get tough when it comes to the financial management. No one of us is having a good time to manage financially as we're too busy acquiring a new customer. At this point, we realized that we need an app to record and track our cash flow. As I started searching for money management apps for business, I only found paid (or trial) apps. It's feature also **suck**, as only one admin that can add/edit a transaction, and rely a lot on a third-party server for saving our database.

**We don't want such kind of apps.**

Then, we developed Fulus Kita to give every business a freedom to manage their own financial matter. It feels like Money Manager but with multi-account capability and online Realtime database thanks to [Firebase](https://firebase.google.com/). Firebase is a mobile & web development platform by Google. With Firebase, you can manage your own database for free, and scale it big with ease in the future.

# Current Feature
FulusKita version: **beta.1**
1. Account sign-in and sign-up
2. Add and Edit Transaction
3. Calculator Function
4. Current Balance

# Feature to Come
1. Multi-language support (currently only support Bahasa. Sorry :( )
1. Add types of credit and debit
2. Filter transaction based on months
3. Track transaction by a user
4. Transaction summary
5. Export data to the spreadsheet
6. Themes :D

You can also request features by using issues with label 'enhancement'. Don't forget to include some description and example so I can clearly understand your request :)

# Installation
## Firebase Installation
1. [Go to Firebase console](https://console.firebase.google.com/) using your Google Account.
2. Create new Firebase Project, named it whatever you like.
3. Choose 'Add Firebase to your Android app'
4. Enter package name 'com.lzharif.fuluskita' in the column provided
5. Download google-services.json provided by Firebase. 
6. Click skip activity when it comes to step 4 (checking whether apps is connected to Firebase)
7. After that, you're brought into Firebase Dashboard. Go to menu Develop -> Authentication and click the button 'Set up sign-in method'
8. Choose Email/Password, then click Enable on the first choice (not the passwordless sign-in). Click save
9. Go to menu Develop -> Database, choose Real-time Database by pressing 'Get started', and choose 'start in locked mode'
10. Under Database tabs, click 'Rules' and paste the following code so only authenticated user that have access to modify database content. After that, press 'Publish'.
```
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
```
11. Done! Now we're move to Android apps installation :)

## Android apps installation
1. Clone this repo or download it as a zip. If you download it, don't forget to unzip it first.
2. Install [Android Studio](https://developer.android.com/studio/install) on your desktop
3. While waiting for installation, copy google-services.json downloaded from Firebase (step 5) to ./FulusKita/app/
4. Open your Android Studio, then choose File > Open. Choose 'FulusKita' folder. Wait until sync process finished.
5. If there's no error, you can generate your APK file by choosing Build -> Build APK from Android Studio.
6. Done! You can install APK into your Android apps and share it with your business partner! :)

# When there's update...
You can check my latest update in the commit or this Readme (see FulusKita version). Just clone this repo and overwrite your existing folder. Then generate your own APK with Android Studio. Simple :)

# One last thing...
If you (or your business) find this repo useful, please donate some bucks for me. Your donation may help me to buy extra coffee so I can improve this repo all night along. You can donate via Paypal (links below). Or, if you're Indonesian, you can transfer me directly into Mandiri Account which you can see directly in FulusKita apps. Thank you :)

[![Support via PayPal](https://cdn.rawgit.com/twolfson/paypal-github-button/1.0.0/dist/button.svg)](https://www.paypal.me/lzharif/)
