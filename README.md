
# react-native-kommunicate-chat

## Getting started

`$ npm install react-native-kommunicate-chat --save`

### Mostly automatic installation

`$ react-native link react-native-kommunicate-chat`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-kommunicate-chat` and add `RNKommunicateChat.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNKommunicateChat.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import io.kommunicate.app.RNKommunicateChatPackage;` to the imports at the top of the file
  - Add `new RNKommunicateChatPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-kommunicate-chat'
  	project(':react-native-kommunicate-chat').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-kommunicate-chat/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-kommunicate-chat')
  	```


## Usage
```javascript
import RNKommunicateChat from 'react-native-kommunicate-chat';

// TODO: What to do with the module?
RNKommunicateChat;
```
  