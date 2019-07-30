//
//  KommunicateChat.swift
//  KommunicateReactNativeSample
//
//  Created by Ashish Kanswal on 30/07/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import Kommunicate

@objc (KommunicateChat)
class KommunicateChat : NSObject{
  
  @objc
  func isLoggedIn(callback: RCTResponseSenderBlock) -> Void {
    var msg = "False"
    
    if Kommunicate.isLoggedIn {
      msg = "True"
    }
    
    callback([msg])
  }
}
