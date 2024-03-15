# Reach Native Kommunicate CHat v2.3.3
- Fixed assignee profile image on conversation screen - iOS
- Fixed Assigne name is cropping issue on covnersation screen - iOS
- Improved read receipt label for messages. - iOS
- Improved the Flow of Showing Rating Bar. - iOS
- Fixed Link preview issue in iOS
- Added the last Message icon in Conversation List Screen in iOS
- Added support for iframe in HTML type rich message - Android
- Fixed location sharing issues - Android
- Image full screen view improvement - Android
- Added back button in in-app browser - Android

# React Native Kommunicate Chat v2.3.2
- Upgraded iOS SDK to 7.1.3

# React Native Kommunicate Chat v2.3.1
- Exposed a function to fetch unreadcount
- Fixed `openparticularconversation` bug when single threaded is enabled
- Exposed a function to fetch channel info, conversation assignee info

# React Native Kommunicate Chat v2.3.0
- Fixed a crash that was happening by message template customisation - Android
- Added Support For Auto Suggestions Rich Message - iOS
- Added pseudonym support -  iOS 
- Added support for custom input field rich message - iOS
- Added support for XCode 15 - iOS

# React Native Kommunicate Chat v2.2.9
- Fixed android build issue

# React Native Kommunicate Chat v2.2.8
- Upgraded minimum SDK version to iOS 13.0  - iOS
- Passed kmUserLocale in groupMetadata and messageMetaData - iOS
- Added support for Sending GIF from device - iOS
- Exposed a customisation function for a rating menu icon on conversation screen. - Android & iOS

# React Native Kommunicate Chat v2.2.7
- UI/UX improvements and fixed tool bar issue in android
- Fixed Rating bar & keyboard overlap issue
# React Native Kommunicate Chat v2.2.6
- Updated Refresh icon on conversation screen - iOS
- Improved the UI of Multiple language selection Page - iOS

# React Native Kommunicate Chat v2.2.5
- Fixed down arrow coming in bottom of the screen when welcome message get rendered issue.
- Form Submit button width is corrected.
- Added border to the form and removed paddding form the top of each cell.
- Fixed UI/UX mismatches across platforms

## React Native Kommunicate Chat v2.2.4
- Added support to send message on language change
- Show/Hide Template message buttons when resolved conversation
- Fixed refresh icon squeeze and crashes - Android
- Fixed the form submission with empty fields issue
  
## React Native Kommunicate Chat v2.2.3
- Fixed visibility of startNewConversation button
- Added support for API Suggestions
- Added "showTypingIndicatorWhileFetchingResponse" settings to show typing indicator when bot fetching response
- Crashes fixed

## React Native Kommunicate Chat v2.2.1
- UI/UX changes to match Android and iOS
- Added support for Custom Input Field - Android
- Bug fixes and optimizations

## React Native Kommunicate Chat v2.2.0
- Added support for Default Settings -
```javascript
var setting = {
         "defaultAgentIds": [""], //list of agentID
        "defaultBotIds": [""], // list of BotID
        "defaultAssignee": "amantoppo3199@gmail.com", //string of conversation Assignee
        "skipRouting": true, // If you pass this value true then it will skip routing rules set from conversation rules section.
         "teamId": "<pass any teamID>"
        };
        RNKommunicateChat.updateDefaultSetting(setting, (status, message) => {
          console.log(message);
        });
```
- Fixed closeConversatioNScreen not working in iOS

## React Native Kommunicate Chat v2.1.8
- Upgraded iOS SDK to 6.8.9
- Fixed event data mismatch

## React Native Kommunicate Chat v2.1.7
- Upgraded iOS SDK to 6.8.8
- Upgraded Android SDK to 2.7.0
- Fixed form template submission issues in iOS 


## React Native Kommunicate Chat v2.1.6
- Upgraded iOS SDK version to 6.8.7
- Fixed build issue for iOS


## React Native Kommunicate Chat v2.1.5
- Exposed enableSpeechToText() method which will create UI for multiple speech to text language - iOS
- Added customizable option to disable chat widget in Helpcenter "hideChatInHelpcenter"
- Attachment name and icon improvement - Android
- Added support to delete conversation for end-user - Android

## React Native Kommunicate Chat v2.1.0
- Exposed createSettings() methods which will create settings for both Android and iOS
- Exposed enableSpeechToText() method which will create UI for multiple speech to text language - Android
- Added option to rate conversation anytime from the conversation menu
- Added multiple customizations

## React Native Kommunicate Chat v2.0.2
- Upgraded iOS SDK to 6.7.8

## React Native Kommunicate Chat v2.0.1
- Added support for listening to Events - iOS
```javascript
    const eventEmitter = new NativeEventEmitter(RNKommunicateChat);
    const eventListener = eventEmitter.addListener("onMessageReceived", event => {
        console.log("onMessageReceived" );
    });
 ```
 - Exposed CloseConversationScreen() to close the conversation screen
## React Native Kommunicate Chat v2.0.0
- Added support for listening to Events - Android only
 ```javascript
    const eventEmitter = new NativeEventEmitter(RNKommunicateChat);
    const eventListener = eventEmitter.addListener("onPluginLaunch", event => {
        console.log("onPluginLaunch" );
    });
 ```
 For more events, refer to our docs
 - Expose method to close Kommunicate screen - ```RNKommunicateChat.closeConversationScreen()```
 - Typing Indicator Design Changed - Android
 - Added Support for Android API 33
 - Added support for AES256 encryption and decryption
 - Added multiple customization option
 - Multiple bug fixes and optimization


## React Native Kommunicate Chat v1.6.8

- Exposed methods to iOS: updateConversationAssignee and updateConversationInfo
```javascript
    RNKommunicateChat.updateConversationAssignee({
            clientConversationId: "<clientconversationid>",
            conversationAssignee: "<assignee email>"}, (success, error) => {
                console.log("Updated conversation assignee");
            });

    RNKommunicateChat.updateConversationInfo({
            clientConversationId: "<client conversation id>",
            conversationInfo: {
            "test1": "value1",
            "test2": "value2"
            }
        }, (success, error) => {
            console.log("Updated conversation info");
            });
```
- HTML Rich Message Video template - Android
- Fixed showing empty body for rich message having no text - Android
- Fix for attachment sending empty message - Android
- Sync deleted message from dashboard - Android
- Optimized group list API - Android
- Fixed empty notification body - Android
- Added Text to Speech Support for messages. To enable it, add this in AppDelegate didFinishLaunchingWithOptions method: 'Kommunicate.defaultConfiguration.enableTextToSpeechInConversation = true' - iOS