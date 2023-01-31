
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
    
    override func supportedEvents() -> [String]! {
        return ["onMessageReceived", "onMessageSent", "onRichMessageButtonClick", "onStartNewConversation", "onSubmitRatingClick", "onBackButtonClicked", "onFaqClick", "onConversationRestarted"]
    }
    
    
//    open override func addListener(_ eventName: String!) {
//        super.addListener(eventName)
//        Kommunicate.subscribeCustomEvents(events: [CustomEvent.messageReceive, CustomEvent.messageSend,CustomEvent.faqClick, CustomEvent.newConversation, CustomEvent.submitRatingClick, CustomEvent.restartConversationClick, CustomEvent.richMessageClick, CustomEvent.conversationBackPress, CustomEvent.conversationListBackPress ], callback: self)
//    }
   
   
   
//    open override func removeListeners(_ count: Double) {
//        super.removeListeners(count)
//        print("Pakka101 remove listener is called")
//    }

    
//     public func messageSent(message: ALMessage) {
//         print("pakka101 \(message.message)")
//         RNEventEmitter.emitter.sendEvent(withName: "onMessageSent", body: nil)
//     }
    
    
//     var hasListener: Bool = false

//     open override func startObserving() {
//         super.startObserving()
//         hasListener = true
//     }

//     open override func stopObserving() {
//         super.stopObserving()
//             hasListener = false
//     }    
//     public func messageReceived(message: ALMessage) {
//         print("pakka101 \(message.message)")
//         // let encodedData = try JSONEncoder().encode(message)
//         // let jsonString = String(data: encodedData,encoding: .utf8)
//         RNEventEmitter.emitter.sendEvent(withName: "onMessageReceived", body: nil)

//     }
    
//     public func conversationRestarted(converstionId: String) {
//       RNEventEmitter.emitter.sendEvent(withName: "onConversationRestarted", body: nil)

//     }
    
//     public func onBackButtonClick(isConversationOpened: Bool) {
//       RNEventEmitter.emitter.sendEvent(withName: "onBackButtonClicked", body: nil)

//     }
    
//     public func faqClicked(url: String) {
//         RNEventEmitter.emitter.sendEvent(withName: "onFaqClick", body: nil)

//     }
    
//     public func conversationCreated(conversationId: String) {
//       RNEventEmitter.emitter.sendEvent(withName: "onStartNewConversation", body: nil)

//     }
    
//     public func ratingSubmitted(conversationId: String, rating: Int, comment: String) {
//         RNEventEmitter.emitter.sendEvent(withName: "onSubmitRatingClick", body: nil)
//     }
    
//     public func richMessageClicked(conversationId: String, action: [String : Any], type: String) {
//         RNEventEmitter.emitter.sendEvent(withName: "onRichMessageButtonClick", body: nil)
//     }
    

}