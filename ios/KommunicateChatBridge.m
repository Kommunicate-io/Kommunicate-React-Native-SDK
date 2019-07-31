//
//  KommunicateChatBridge.m
//  KommunicateReactNativeSample
//
//  Created by Ashish Kanswal on 30/07/19.
//  Copyright © 2019 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(KommunicateChat, NSObject)

RCT_EXTERN_METHOD(isLoggedIn: (RCTResponseSenderBlock));

RCT_EXTERN_METHOD(loginUser:(NSDictionary<NSString *, id> * _Nonnull)user :(RCTResponseSenderBlock _Nonnull)callback);

RCT_EXTERN_METHOD (loginAsVisitor:(NSString * _Nonnull) appId :(RCTResponseSenderBlock _Nonnull) callback);
RCT_EXTERN_METHOD (registerPushNotification: (RCTResponseSenderBlock));
RCT_EXTERN_METHOD (updatePushToken:(NSString * _Nonnull)token :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (openConversation: (RCTResponseSenderBlock));
RCT_EXTERN_METHOD (buildConversation: (NSDictionary<NSString *, id> * _Nonnull)jsonObj :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (openParticularConversation:(NSString * _Nonnull)conversationId :(BOOL)skipConversationList :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (logout: (RCTResponseSenderBlock));
@end

