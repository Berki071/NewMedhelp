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
import FirebaseMessaging

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
                    if let t=error{
                        LoggingTree.e("SplashPresenter/verifyUser", t)
                        self.nextPage="Login"
                    }
                }
            })
        }
        
        func updateHeaderInfo(){
            let idCent=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idCenter!))
            
            sdk.getCenterApiCall(idCenter: idCent, completionHandler: { response, error in
                if let res : CenterList = response {
                    self.sharePreferenses.centerInfo = res.response[0]
                    self.currentUserInfo()
                } else {
                    if let t=error{
                        LoggingTree.e("SplashPresenter/updateHeaderInfo", t)
                        self.nextPage="Login"
                    }
                }
            })
        }
        
        func currentUserInfo(){
            
            let userLogin = self.sharePreferenses.usersLogin
            
            
            if userLogin.count == 0 {
                LoggingTree.e("SplashPresenter/currentUserInfo 0 , Ошибка загрузки информации о пользователе")
                self.nextPage="Login"
                return
            }
            
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let h_dbName = String.init(self.sharePreferenses.centerInfo!.db_name!)
            let idUser=String(Int.init(self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(self.sharePreferenses.currentUserInfo!.idBranch!))
            
            for i in  userLogin {
                let idUserItem=String(Int.init(i.idUser!))
                let idBranchItem=String(Int.init(i.idBranch!))
                
                sdk.getCurrentUserInfoInCenter(idUser: idUserItem, idBranch: idBranchItem, h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch, completionHandler: { response, error in
                    if let res : CurrentUserInfoList = response {
                        
                        i.surname = MUtils.companion.encodeDecodeWord(word: res.responses[0].surname!, key: res.responses[0].keySurname!)
                        i.name = res.responses[0].name
                        i.patronymic = res.responses[0].patronymic
                        i.phone = MUtils.companion.encodeDecodeWord(word: res.responses[0].phone!, key: res.responses[0].keyPhone!)
                        i.birthday = res.responses[0].birthday
                        i.email = res.responses[0].email
                        
                        if self.sharePreferenses.currentUserInfo!.idUser == i.idUser {
                                self.sharePreferenses.currentUserInfo = i
                        }
                        
                        var boo = true
                        
                        for tm in userLogin {
                            if tm.name == nil {
                                boo = false
                                break
                            }
                        }
                        
                        if boo {
                            self.sharePreferenses.usersLogin = userLogin
                            self.firebaseId()
                        }
                        
                    } else {
                        if let t=error{
                            LoggingTree.e("SplashPresenter/currentUserInfo", t)
                            self.nextPage="Login"
                        }
                    }
                })
            }
        }
        
        func firebaseId (){
            Messaging.messaging().token { token, error in
              if let error = error {
                  LoggingTree.e("SplashPresenter/firebaseId", error)
                  self.nextPage="Login"
              } else if let token = token {
              
                  self.sendFcmToken(token: token)
              }
            }
        }
        
        private var countFcmSend=0
        func sendFcmToken(token : String){
            let userLogin = self.sharePreferenses.usersLogin
            
            if userLogin.count == 0 {
                allHospitalBranc()
                return
            }
            
            countFcmSend=userLogin.count
            
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let h_dbName = String.init(self.sharePreferenses.centerInfo!.db_name!)
            let idUser=String(Int.init(self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(self.sharePreferenses.currentUserInfo!.idBranch!))
            
            for i in userLogin {
                let idUserItem=String(Int.init(i.idUser!))
                let idBranchItem=String(Int.init(i.idBranch!))
                
                sdk.sendFcmId(idUser: idUserItem, idFilial: idBranchItem, idFcm: token, h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch, completionHandler: { response, error in
                    if let res : SimpleResBoolean = response {
                        self.countFcmSend += 1
                        if self.countFcmSend <= 0 {
                            self.allHospitalBranc()
                        }
                        
                    } else {
                        if let t=error{
                            LoggingTree.e("LoginPresenter/restorePass", t)
                            self.nextPage="Login"
                        }
                    }
                })
            }
        }
        
        func allHospitalBranc(){
            let idCent = String(Int.init(self.sharePreferenses.currentUserInfo!.idCenter!))
            
            sdk.getAllHospitalBranch(idCenter: idCent, completionHandler: { response, error in
                if let res : SettingsAllBaranchHospitalList = response {
                    self.processingYandexKey(resp: res.response)
                    self.nextPage="Main"
                } else {
                    if let t=error{
                        LoggingTree.e("SplashPresenter/getAllHospitalBranch", t)
                        self.nextPage="Main"
                    }
                }
            })
        }
   
        func processingYandexKey(resp: [SettingsAllBranchHospitalResponse]){
            var keyExit = false
            var allExist = true
            
            for i in resp {
                if i.idShop == nil || i.idShop!.isEmpty{
                    allExist = false
                }else{
                    keyExit = true
                }
            }
            
            if allExist{
                self.sharePreferenses.yandexStoreIsWorks = true
            }else{
                self.sharePreferenses.yandexStoreIsWorks = false
                if keyExit {
                    LoggingTree.e("Не у всех филиалов прописан Yandex IdShop")
                }
            }
            
        }
        
    }
}


