//
//  KommunicateChat.swift
//  KommunicateReactNativeSample
//
//  Created by Ashish Kanswal on 30/07/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import Kommunicate
import Applozic
import ApplozicSwift

@objc (RNKommunicateChat)
class RNKommunicateChat : NSObject, KMPreChatFormViewControllerDelegate {
  
  var appId : String? = nil;
  var agentIds: [String]? = [];
  var botIds: [String]? = [];
  var createOnly: Bool = false
  var callback: RCTResponseSenderBlock? = nil;
  var isSingleConversation: Bool = true;
  
  @objc
  func isLoggedIn(_ callback: RCTResponseSenderBlock) -> Void {
    var msg = "False"
    
    if Kommunicate.isLoggedIn {
      msg = "True"
    }
    
    callback([msg])
  }
  
  @objc
  func loginUser(_ user: Dictionary<String, Any>, _ callback: @escaping RCTResponseSenderBlock)-> Void{
    let kmUser = KMUser()
    
    if(user["userId"] != nil){
      kmUser.userId = user["userId"] as? String
    }
    if(user["applicationId"] != nil){
      kmUser.applicationId = user["applicationId"] as? String
    }
    if(user["password"] != nil){
      kmUser.password = user["password"] as? String
    }
    if(user["email"] != nil){
      kmUser.email = user["email"] as? String
    }
    if(user["displayName"] != nil){
      kmUser.displayName = user["displayName"] as? String
    }
    if(user["imageLink"] != nil){
      kmUser.imageLink = user["imageLink"] as? String
    }
    if(user["contactNumber"] != nil){
      kmUser.contactNumber = user["contactNumber"] as? String
    }
    if(user["authenticationTypeId"] != nil){
      kmUser.authenticationTypeId = user["authenticationTypeId"] as! Int16
    }
    if(user["pushNotificationFormat"] != nil){
      kmUser.pushNotificationFormat = user["pushNotificationFormat"] as! Int16
    }
    if(user["registrationId"] != nil){
      kmUser.registrationId = user["registrationId"] as? String
    }
    if(user["deviceApnsType"] != nil){
      kmUser.deviceApnsType = user["deviceApnsType"] as! Int16
    }
    if(user["metadata"] != nil){
      kmUser.metadata = user["metadata"] as? NSMutableDictionary
    }
    Kommunicate.registerUser(kmUser, completion: {
      response, error in
      guard error == nil else{
        callback(["Error", error as Any])
        return
      }
      callback(["Success", response as Any])
    })
  }
  
  @objc
  func loginAsVisitor(_ appId: String, _ callback: @escaping RCTResponseSenderBlock) -> Void {
    let kmUser = KMUser()
    kmUser.userId = Kommunicate.randomId()
    Kommunicate.setup(applicationId: appId)
    kmUser.applicationId = appId
    
    Kommunicate.registerUser(kmUser, completion: {
      response, error in
      guard error == nil else{
        callback(["Error", error as Any])
        return
      }
      callback(["Success", response as Any])
    })
  }
  
  @objc
  func registerPushNotification(_ callback: RCTResponseSenderBlock) -> Void{
    
  }
  
  @objc
  func updatePushToken(_ token: String, _ callback: @escaping RCTResponseSenderBlock) -> Void{
    
  }
  
  @objc
  func openConversation(_ callback: @escaping RCTResponseSenderBlock) -> Void{
    DispatchQueue.main.async{
      if let top = UIApplication.topViewController(){
        Kommunicate.showConversations(from: top)
        callback(["Success", "Successfully launched conversation list screen"])
      }else{
        callback(["Error", "Failed to launch conversation list screen"])
      }
    }
  }
  
  @objc
  func buildConversation(_ jsonObj: Dictionary<String, Any>, _ callback: @escaping RCTResponseSenderBlock) -> Void{
    self.isSingleConversation = true
    self.createOnly = false;
    self.agentIds = [];
    self.botIds = [];
    self.callback = callback;
    
    let viewController = UIApplication.topViewController()
  
    do{
        var withPrechat : Bool = false
        var kmUser : KMUser? = nil
        
        if jsonObj["appId"] != nil {
          appId = jsonObj["appId"] as? String
        }
        
        if jsonObj["withPreChat"] != nil {
          withPrechat = jsonObj["withPreChat"] as! Bool
        }
        
        if jsonObj["isSingleConversation"] != nil{
          self.isSingleConversation = jsonObj["isSingleConversation"] as! Bool
        }
        
        if(jsonObj["createOnly"] != nil){
          self.createOnly = jsonObj["createOnly"] as! Bool
        }
        
        if let messageMetadataStr = (jsonObj["messageMetadata"] as? String)?.data(using: .utf8) {
          if let messageMetadataDict = try JSONSerialization.jsonObject(with: messageMetadataStr, options : .allowFragments) as? Dictionary<String,Any> {
            Kommunicate.defaultConfiguration.messageMetadata = messageMetadataDict
          }
        }
      
          let agentIds = jsonObj["agentIds"] as? [String]
          let botIds = jsonObj["botIds"] as? [String]
          
          self.agentIds = agentIds
          self.botIds = botIds
          
          if Kommunicate.isLoggedIn{
            self.handleCreateConversation()
          }else{
            if jsonObj["appId"] != nil {
              Kommunicate.setup(applicationId: jsonObj["appId"] as! String)
            }
            
            if !withPrechat {
              if jsonObj["kmUser"] != nil{
                var jsonSt = jsonObj["kmUser"] as! String
                jsonSt = jsonSt.replacingOccurrences(of: "\\\"", with: "\"")
                jsonSt = "\(jsonSt)"
                kmUser = KMUser(jsonString: jsonSt)
                kmUser?.applicationId = appId
              }else {
                kmUser = KMUser.init()
                kmUser?.userId = Kommunicate.randomId()
                kmUser?.applicationId = appId
              }
              
              Kommunicate.registerUser(kmUser!, completion:{
                response, error in
                guard error == nil else{
                  callback(["Error", error as Any])
                  return
                }
                self.handleCreateConversation()
              })
            }else{
              DispatchQueue.main.async {
              let controller = KMPreChatFormViewController(configuration: Kommunicate.defaultConfiguration)
              controller.delegate = self
                viewController?.present(controller, animated: false, completion: nil)
              }
            }
          }
      }catch _ as NSError{
       
    }
  }
  
  func handleCreateConversation(){
    Kommunicate.createConversation(userId: ALUserDefaultsHandler.getUserId(),
                                   agentIds: self.agentIds ?? [],
                                   botIds: self.botIds,
                                   useLastConversation: self.isSingleConversation,
                                   completion: { response in
                                    guard !response.isEmpty else{
                                      self.callback!(["Error", "Unable to create conversation"])
                                      return
                                    }
                                    if self.createOnly{
                                      self.callback!(["Success", response])
                                    }else{
                                      self.openParticularConversation(response, true, self.callback!)
                                    }
    })
  }
  
  @objc
  func openParticularConversation(_ conversationId: String,_ skipConversationList: Bool, _ callback: @escaping RCTResponseSenderBlock) -> Void {
    DispatchQueue.main.async{
      if let top = UIApplication.topViewController(){
        Kommunicate.showConversationWith(groupId: conversationId, from: top, completionHandler: ({ (shown) in
          if(shown){
            callback(["Success", "Sucessfully launched conversation with conversationId : " + conversationId])
          }else{
            callback(["Error", "Failed to launch conversation with conversationId : " + conversationId])
          }
        }))
      }else{
        callback(["Error", "Failed to launch conversation with conversationId : " + conversationId])
      }}
  }
  
  @objc
  func logout(_ callback: RCTResponseSenderBlock) -> Void {
    Kommunicate.logoutUser()
    callback(["Success"])
  }
  
  func closeButtonTapped() {
    UIApplication.topViewController()?.dismiss(animated: false, completion: nil)
  }
  
  func userSubmittedResponse(name: String, email: String, phoneNumber: String) {
    UIApplication.topViewController()?.dismiss(animated: false, completion: nil)
   
    let kmUser = KMUser.init()
    guard let applicationKey = appId else {
      return
    }
    
    kmUser.applicationId = applicationKey
    
    if(!email.isEmpty){
      kmUser.userId = email
      kmUser.email = email
    }else if(!phoneNumber.isEmpty){
      kmUser.contactNumber = phoneNumber
    }
    
    kmUser.contactNumber = phoneNumber
    kmUser.displayName = name
    
    Kommunicate.registerUser(kmUser, completion:{
      response, error in
      guard error == nil else{
        self.callback!(["Error", "Unable to login"])
        return
      }
      self.handleCreateConversation()
    })
  }
}

extension UIApplication {
  class func topViewController(controller: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {
    if let navigationController = controller as? UINavigationController {
      return topViewController(controller: navigationController.visibleViewController)
    }
    if let tabController = controller as? UITabBarController {
      if let selected = tabController.selectedViewController {
        return topViewController(controller: selected)
      }
    }
    if let presented = controller?.presentedViewController {
      return topViewController(controller: presented)
    }
    return controller
  }}
