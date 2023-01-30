



import React
import Kommunicate
import KommunicateChatUI_iOS_SDK
import KommunicateCore_iOS_SDK

@objc(RNEventEmitter)
open class RNEventEmitter: RCTEventEmitter, ALKCustomEventCallback {
    public func messageSent(message: ALMessage) {
        print("pakka101 \(message.message)")
        RNEventEmitter.emitter.sendEvent(withName: "onPluginLaunch", body: nil)
    }
    
    public func messageReceived(message: ALMessage) {
        print("pakka101 \(message.message)")
    }
    
    public func conversationRestarted(converstionId: String) {
        
    }
    
    public func onBackButtonClick(isConversationOpened: Bool) {
        
    }
    
    public func faqClicked(url: String) {
        
    }
    
    public func conversationCreated(conversationId: String) {
        
    }
    
    public func ratingSubmitted(conversationId: String, rating: Int, comment: String) {
        
    }
    
    public func richMessageClicked(conversationId: String, action: [String : Any], type: String) {
        
    }
    

  public static var emitter: RCTEventEmitter!

  override init() {
    super.init()
    RNEventEmitter.emitter = self
      Kommunicate.subscribeCustomEvents(events: [CustomEvent.messageReceive, CustomEvent.messageSend], callback: self)
      print("Pakka101 entering")
  }

  open override func supportedEvents() -> [String] {
    ["onPluginLaunch", "onPending"]      // etc.
  }

  


}
