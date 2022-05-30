//
//  SplashPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 26.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

extension SplashUIView {
    
    class SplashPresenter : ObservableObject {
        @Published var nextPage : String = "" //переход на след страницу
        let sdk: NetworkManager
        var sharePreferenses : SharedPreferenses
        
        init(){
            sdk=NetworkManager()
            sharePreferenses = SharedPreferenses()
            
            testOpenNextActivity ()
        }
        
        func testOpenNextActivity (){
            let sig = "EwXFAQoz6ca1uJAQxbeU6YD4Eps"
            var login = sharePreferenses.currentLogin ?? nil
            var pass = sharePreferenses.currentPassword ?? nil
            
//            if(login==nil || pass==nil){
//                nextPage="Login"
//                return
//            }else{
//                verifyUser ()
//            }
            
            if(login == nil){
                login = "9888888888"
            }
            if(pass==nil){
                pass = "635088"
            }

            verifyUser (sig,login,pass)
        }
        
        func verifyUser (_ sig:String,_ login:String?,_ pass:String? ){
            
            sdk.doLoginApiCall(signature: sig ,username: login!, password:pass!, completionHandler: { response, error in
                if let res : UserList = response {
                    if(res.response[0].login != nil ){
                        self.sharePreferenses.usersLogin=res.response
                        
                       // var tmp=String(describing: type(of: res.response))
                        //var tmp2=String(describing: type(of:  self.sharePreferenses.usersLogin))
                        
                        let tmpCurrentUserInfo = self.sharePreferenses.currentUserInfo
                        if(tmpCurrentUserInfo == nil || tmpCurrentUserInfo?.apiKey == nil){
                            self.sharePreferenses.currentUserInfo = res.response[0]
                        }
                        
                        self.updateHeaderInfo()
                    }else{
                        //self.sharePreferenses.currentPassword = ""
                       // self.sharePreferenses.usersLogin = nil
                        self.nextPage="Login"  //переход на след страницу
                    }
                    
                } else {
                    let tmpErr = error
                    
                    self.nextPage="Login" 
                }
            })
        }
        
        func updateHeaderInfo(){
            LoggingTree.i("text ios")
            
//            sdk.getCenterApiCall(  , completionHandler: { response, error in
//                if let res : UserList = response {
//              
//                    
//                } else {
//                
//                }
//            })
            
        }
        
        
        
    }
    
    
}


