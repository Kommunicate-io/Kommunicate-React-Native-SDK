//
//  RNKommunicateChat.m
//  KommunicateReactNativeSample
//
//  Created by Ashish Kanswal on 30/07/19.
//  Copyright © 2019 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNKommunicateChat, NSObject)

RCT_EXTERN_METHOD (isLoggedIn: (RCTResponseSenderBlock));

RCT_EXTERN_METHOD (loginUser:(NSDictionary<NSString *, id> * _Nonnull)user :(RCTResponseSenderBlock _Nonnull)callback);

RCT_EXTERN_METHOD (loginAsVisitor:(NSString * _Nonnull) appId :(RCTResponseSenderBlock _Nonnull) callback);
RCT_EXTERN_METHOD (registerPushNotification: (RCTResponseSenderBlock));
RCT_EXTERN_METHOD (updatePushToken:(NSString * _Nonnull)token :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (openConversation: (RCTResponseSenderBlock));
RCT_EXTERN_METHOD (buildConversation: (NSDictionary<NSString *, id> * _Nonnull)jsonObj :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (openParticularConversation:(NSString * _Nonnull)conversationId :(BOOL)skipConversationList :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (logout: (RCTResponseSenderBlock));
RCT_EXTERN_METHOD (updateChatContext: (NSDictionary<NSString *, id> * _Nonnull)chatContext :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (updateUserDetails: (NSDictionary<NSString *, id> * _Nonnull)kmUser :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (updateConversationAssignee: (NSDictionary<NSString *, id> * _Nonnull)assigneeData :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (updateTeamId: (NSDictionary<NSString *, id> * _Nonnull)teamData :(RCTResponseSenderBlock _Nonnull)callback);
RCT_EXTERN_METHOD (updateConversationInfo: (NSDictionary<NSString *, id> * _Nonnull)infoData :(RCTResponseSenderBlock _Nonnull)callback);


+ (BOOL)requiresMainQueueSetup { return YES; }
@end



