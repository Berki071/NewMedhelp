//
//  LoggingTree.swift
//  iosApp
//
//  Created by Михаил Хари on 27.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class LoggingTree{
    //Timber.e - for error (priority 6)
    //Timber.i - for onCreate Activity (priority 4)
    //Timber.v - verbose information about actions (priority 2)
    //Timber.d - ALERT_TYPE (priority 3)
    
    static let VERBOSE_INFORMATION="VERBOSE_INFORMATION_ios"
    static let CREATE_ACTIVITY="CREATE_ACTIVITY_ios"
    static let ERROR="ERROR_ios"
    static let ALERT_TYPE="ALERT_TYPE_ios"
    static let ERROR_PAYMENT="ERROR_PAYMENT_ios"
    
    static let sdk: NetworkManager = NetworkManager()
    static let sharePreferenses : SharedPreferenses = SharedPreferenses()
    
//    init(){
//        sdk=NetworkManager()
//        //sharePreferenses = SharedPreferenses()
//    }
    
    static func i (_ msg : String){
        self.sendLogToServer(CREATE_ACTIVITY, msg)
    }
    static func v (_ msg : String){
        self.sendLogToServer(VERBOSE_INFORMATION, msg)
    }
    static func d (_ msg : String){
        self.sendLogToServer(ALERT_TYPE, msg)
    }
    static func e (_ msg : String, _ error : Error){
        self.sendLogToServer(ERROR, msg+"; \(error)")
    }
    static func e (_ msg : String){
        self.sendLogToServer(ERROR, msg)
    }
    static func ePay (_ msg : String, _ error : Error){
        self.sendLogToServer(ERROR_PAYMENT, msg+"; \(error)")
    }

    
    static func sendLogToServer(_ type : String, _ message : String){
        var idUSer="0"
        var idBranch="0"
        var idCenter="0"
        
        if(sharePreferenses.currentUserInfo != nil){
            if let t = sharePreferenses.currentUserInfo!.idUser {
                idUSer=String( Int.init(t))
            }
            if let t = sharePreferenses.currentUserInfo!.idBranch {
                idBranch = String(Int.init(t))
            }
            if let t = sharePreferenses.currentUserInfo!.idCenter {
                idCenter = String(Int.init(t))
            }
        }
        
        let versionCode = Bundle.main.object(forInfoDictionaryKey: "CFBundleVersion") as! String? ?? "x"
        
        sdk.sendLogToServer(type: type, log: message, versionCode: versionCode, idUSer: idUSer, idBranch: idBranch, idCenter: idCenter
                            , completionHandler: { response, error in
            if let res : SimpleResponseBoolean = response {
            } else {
            }
        })
    }
    
}
