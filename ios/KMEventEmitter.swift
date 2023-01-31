
import React
import Kommunicate
import KommunicateChatUI_iOS_SDK
import KommunicateCore_iOS_SDK

@objc(KMEventEmitter)
open class KMEventEmitter: RCTEventEmitter {
    
    public static var emitter: RCTEventEmitter!

    open override class func requiresMainQueueSetup() -> Bool {
        return true
    }
    override init() {
        super.init(disabledObservation: ())
        KMEventEmitter.emitter = self
    }
    
    open override func supportedEvents() -> [String]! {
        return ["onMessageReceived", "onMessageSent", "onRichMessageButtonClick", "onStartNewConversation", "onSubmitRatingClick", "onBackButtonClicked", "onFaqClick", "onConversationRestarted"]
    }
}