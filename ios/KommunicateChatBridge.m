//
//  KommunicateChatBridge.m
//  KommunicateReactNativeSample
//
//  Created by Ashish Kanswal on 30/07/19.
//  Copyright © 2019 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(KommunicateChat, NSObject)

RCT_EXTERN_METHOD(isLoggedIn: (RCTResponseSenderBlock) callback);
@end
