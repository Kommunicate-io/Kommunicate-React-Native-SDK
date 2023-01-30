




import Kommunicate
import KommunicateChatUI_iOS_SDK
import KommunicateCore_iOS_SDK
import RCTEventEmitter

@objc(RNEventEmitter)
open class RNEventEmitter: RCTEventEmitter, ALKCustomEventCallback {

  public static var emitter: RCTEventEmitter!

  override init() {
    super.init()
    RNEventEmitter.emitter = self
  }

  open override func supportedEvents() -> [String] {
    ["onPluginLaunch", "onPending"]      // etc.
  }

  


}