## Installation

This is the installation doc for the Kommunicate SDK for React Native.

To add the Kommunicate module to you react native application, add it using npm:

```
npm install react-native-kommunicate-chat --save
```

Then link the module:

```
react-native link react-native-kommunicate-chat
```

For iOS, navigate to YourApp/ios directory from terminal and run the below command:
    ```
    pod install
    ```

>Note: Kommunicate requires min ios platform version 10 and uses dynamic frameworks. Make sure you have the below settings at the top of your Podfile:
 ```
 platform :ios, '10.0'
 use_frameworks!
 ```

Add permissions for Camera, Photo Library, Microphone, Contacts and Location usage. </br>

Note: We won't be asking the users for these permissions unless they use the respective feature. Due to Apple's requirement, we have to add these permissions if we are using any of their APIs related to Camera, Microphone etc.

Open `Info.plist` from `/ios/YOUR_PROJECT_NAME/Info.plist` file and paste these permissions anywhere inside the `<dict>` tag.
```
<key>NSCameraUsageDescription</key>
<string>Allow Camera</string>
<key>NSContactsUsageDescription</key>
<string>Allow Contacts</string>
<key>NSLocationWhenInUseUsageDescription</key>
<string>Allow location sharing!!</string>
<key>NSMicrophoneUsageDescription</key>
<string>Allow MicroPhone</string>
<key>NSPhotoLibraryUsageDescription</key>
<string>Allow Photos</string>
<key>NSPhotoLibraryAddUsageDescription</key>
<string>Allow write access</string>
```
## Import the module

You can use the module by importing it in your react native files as below:

```js
import RNKommunicateChat from 'react-native-kommunicate-chat';
```

## Get your Application Id
Sign up for [Kommunicate](https://dashboard.kommunicate.io) to get your [APP_ID](https://dashboard.kommunicate.io/settings/install). This APP_ID is used to create/launch conversations.

## Launch chat
Kommunicate provides buildConversation function to create and launch chat directly saving you the extra steps of authentication, creation, initialization and launch. You can customize the process by building the conversationObject according to your requirements.
To launch the chat you need to create a conversation object. This object is passed to the `buildConversation` function and based on the parameters of the object the chat is created/launched.

Below are some examples to launch chat in different scenarios:

### Launching chat for visitor:
If you would like to launch the chat directly without the visiting user entering any details, then use the method as below:

```js
let conversationObject = {
     'appId' : '<APP_ID>' // The [APP_ID](https://dashboard.kommunicate.io/settings/install) obtained from kommunicate dashboard.
}

 RNKommunicateChat.buildConversation(conversationObject, (response, responseMessage) => {
        if(response == "Success") {
            console.log("Conversation Successfully with id:" + responseMessage);
        }
      });
```
### Launching chat for visitor with lead collection:
If you need the user to fill in details like phone number, emailId and name before starting the support chat then launch the chat with `withPreChat` flag as true. In this case you wouldn't need to pass the kmUser. A screen would open up for the user asking for details like emailId, phone number and name. Once the user fills the valid details (atleast emailId or phone number is required), the chat would be launched. Use the function as below:

```js
let conversationObject = {
     'appId' : '<APP_ID>', // The [APP_ID](https://dashboard.kommunicate.io/settings/install) obtained from kommunicate dashboard.
     'withPreChat' : true
}

 RNKommunicateChat.buildConversation(conversationObject, (response, responseMessage) => {
        if(response == "Success") {
            console.log("Conversation Successfully with id:" + responseMessage);
        }
      });
```

### Launching chat with existing user:
If you already have the user details then create a KMUser object using the details and launch the chat. Use the method as below to create KMUser with already existing details:

```js
let user = {
      'userId' : '<USER_ID>',   //Replace it with the userId of the logged in user
      'password' : '<PASSWORD>'  //Put password here if user has password, ignore otherwise
}

let conversationObject = {
     'appId' : '<APP_ID>', // The [APP_ID](https://dashboard.kommunicate.io/settings/install) obtained from kommunicate dashboard.
     'kmUser' : JSON.stringify(user)
}

 RNKommunicateChat.buildConversation(conversationObject, (response, responseMessage) => {
        if(response == "Success") {
            console.log("Conversation Successfully with id:" + responseMessage);
        }
      });
```

If you have a different use-case and would like to customize the chat creation, user creation and chat launch, you can use more parameters in the conversationObject.

Below are all the parameters you can use to customize the conversation according to your requirements:

| Parameter        | Type           | Description  |
| ------------- |:-------------:| :-----|
| appId      | String      |   The [APP_ID](https://dashboard.kommunicate.io/settings/install) obtained from kommunicate dashboard |
| groupName      | String      |   Optional, you can pass a group name or ignore |
| kmUser | KMUser     |    Optional, Pass the details if you have the user details, ignore otherwise. The details you pass here are used **only the first time**, to login the user. These login details persists until the app is uninstalled or you call [logout](https://docs.kommunicate.io/docs/reactnative-logout). |
| withPreChat | boolean      |   Optional, Pass true if you would like the user to fill the details before starting the chat. If you have user details then you can pass false or ignore. |
| isUnique | boolean      |   Optional,  Pass true if you would like to create only one conversation for every user. The next time user starts the chat the same conversation would open, false if you would like to create a new conversation everytime the user starts the chat. True is recommended for single chat|
| metadata      | Any      |   Optional. This metadata if set will be sent with all the messages sent from that device. Also this metadata will be set to the conversations created from that device.  |
| agentIds | List<String>      |    Optional, Pass the list of agents you want to add in this conversation. The agent ID is the email ID with which your agent is registered on Kommunicate. You may use this to add agents to the conversation while creating the conversation. Note that, conversation assignment will be done on the basis of the routing rules set in the [Conversation Rules section](https://dashboard.kommunicate.io/settings/conversation-rules). Adding agent ID here will only add the agents to the conversation and will not alter the routing rules.|
| botIds | List<String>      |    Optional, Pass the list of bots you want to add in this conversation. Go to [bots](https://dashboard.kommunicate.io/bot) -> Manage Bots -> Copy botID . Ignore if you haven't integrated any bots. You may use this to add any number of bots to the conversation while creating the conversation. Note that this has no effect on the conversation assignee, as the [Conversation Rules](https://dashboard.kommunicate.io/settings/conversation-rules) set forth in the Dashboard will prevail.|
| createOnly      | boolean      |   Optional. Pass true if you need to create the conversation and not launch it. In this case you will receive the clientChannelKey of the created conversation in the success callback function.|


For more detailed documentation, follow this: https://docs.kommunicate.io/docs/reactnative-installation
Here is the sample app which implements this SDK: https://github.com/Kommunicate-io/Kommunicate-React-Native-Sample
