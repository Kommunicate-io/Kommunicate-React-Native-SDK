



import React
import Kommunicate
import KommunicateChatUI_iOS_SDK
import KommunicateCore_iOS_SDK

@objc(RNEventEmitter)
open class RNEventEmitter: RCTEventEmitter, ALKCustomEventCallback {
    public func messageSent(message: ALMessage) {
        
    }
    
    public func messageReceived(message: ALMessage) {
        
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
  }

  open override func supportedEvents() -> [String] {
    ["onPluginLaunch", "onPending"]      // etc.
  }

  


}
