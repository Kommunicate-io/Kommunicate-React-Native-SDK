



import React
import Kommunicate
import KommunicateChatUI_iOS_SDK
import KommunicateCore_iOS_SDK

@objc(RNEventEmitter)
open class RNEventEmitter: RCTEventEmitter, ALKCustomEventCallback {
    public func messageSent(message: ALMessage) {
        print("pakka101 \(message.message)")
        RNEventEmitter.emitter.sendEvent(withName: "onMessageSent", body: nil)

    }
    
    public func messageReceived(message: ALMessage) {
        print("pakka101 \(message.message)")
        // let encodedData = try JSONEncoder().encode(message)
        // let jsonString = String(data: encodedData,
                                encoding: .utf8)
        RNEventEmitter.emitter.sendEvent(withName: "onMessageReceived", body: nil)

    }
    
    public func conversationRestarted(converstionId: String) {
      RNEventEmitter.emitter.sendEvent(withName: "onConversationRestarted", body: nil)

    }
    
    public func onBackButtonClick(isConversationOpened: Bool) {
      RNEventEmitter.emitter.sendEvent(withName: "onBackButtonClicked", body: nil)

    }
    
    public func faqClicked(url: String) {
        RNEventEmitter.emitter.sendEvent(withName: "onFaqClick", body: nil)

    }
    
    public func conversationCreated(conversationId: String) {
      RNEventEmitter.emitter.sendEvent(withName: "onStartNewConversation", body: nil)

    }
    
    public func ratingSubmitted(conversationId: String, rating: Int, comment: String) {
        RNEventEmitter.emitter.sendEvent(withName: "onSubmitRatingClick", body: nil)
    }
    
    public func richMessageClicked(conversationId: String, action: [String : Any], type: String) {
        RNEventEmitter.emitter.sendEvent(withName: "onRichMessageButtonClick", body: nil)
    }
    

  public static var emitter: RCTEventEmitter!

  override init() {
//    super.init()
      super.init(disabledObservation: ())
    RNEventEmitter.emitter = self
      Kommunicate.subscribeCustomEvents(events: [CustomEvent.messageReceive, CustomEvent.messageSend,CustomEvent.faqClick, CustomEvent.conversationBackPress, CustomEvent], callback: self)
      print("Pakka101 entering")
  }

  open override func supportedEvents() -> [String] {
      ["onMessageReceived", "onMessageSent", "onConversationRestarted", "onRichMessageButtonClick", "onStartNewConversation", "onSubmitRatingClick", "onBackButtonClicked", "onFaqClick", "onConversationRestarted" ]      // etc.
  }

}
