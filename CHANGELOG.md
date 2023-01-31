
## [Unreleased]
- Added support for listening to Events - iOS
```javascript
    const eventEmitter = new NativeEventEmitter(RNKommunicateChat);
    const eventListener = eventEmitter.addListener("onMessageReceived", event => {
        console.log("onMessageReceived" );
    });
 ```
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