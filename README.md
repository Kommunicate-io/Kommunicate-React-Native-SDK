
# react-native-kommunicate-chat

## Getting started
`$ npm install react-native-kommunicate-chat --save`

### Linking
`$ react-native link react-native-kommunicate-chat`

### Manual installation
#### iOS
1) Go to iOS directory from terminal and run the below command (Skip if the directory already has a Podfile):
   pod init

2) Open Podfile and add the below pod entry(inside your App target) :
    `pod 'Kommunicate', '~>2.2.0'`

3) Then set the ios platform as 10 at the top of the Podfile (Skip if the platform is already set to 10 or above):
     `platform :ios, '10.0'`

4) Then inside your app target in Podfile add the below line:
     `use_frameworks!`

5) Then go to ios directory from terminal and run the below command:
     `pod install`

6) Open YourApp.xcworkspace file in xcode from ios directory.

7) Change minimum iOS version in xcode for project (General -> Deployment Info -> Deployment Target - > 10)

8) Right Click on yourApp -> Add Files -> YourApp -> node_modules -> react-native-kommunicate-chat -> ios and add the two files ->  RNKommunicateChat.swift and KommunicateChatBridge.m

9) While adding the above 2 files, you will be asked to generate the swift bridging header, click yes. Ignore if your project already has a bridging header file. In the bridging header.h file add the below 2 lines:
   ```
   #import <React/RCTBridge.h>
   #import <React/RCTBridgeModule.h>
   ```
   
#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import io.kommunicate.app.RNKommunicateChatPackage;` to the imports at the top of the file
  - Add `new RNKommunicateChatPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-kommunicate-chat'
  	project(':react-native-kommunicate-chat').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-kommunicate-chat/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-kommunicate-chat')
  	```
   
### Launch Conversation
In your app, create a button to launch the support chat. On click of the button create the conversation launch object and then pass it to the conversation launch object.

To launch the conversation create a conversation launchObject as below:

```
var conversationObject = {
                applicationId : '<Your-APP_ID>',    //Replace it with your APP_ID from https://dashboard.kommunicate.io/settings/install
            };
```

Refer to this doc for more parameters: https://docs.kommunicate.io/docs/cordova-installation#launch-chat

Note: In the above doc its mentioned as appId, here you need to pass applicationId. Rest all parameters are same.


  Then call the conversationBuilder with this object:

```
  Kommunicate.buildConversation(conversationObject, (response)=> {
               console.log("Test response : " + response);
            }); 
```

  
