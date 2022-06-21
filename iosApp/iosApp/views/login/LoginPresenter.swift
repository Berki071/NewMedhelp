//
//  LoginPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 02.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import FirebaseMessaging


//extension LoginUiView {
//}

class LoginPresenter : ObservableObject {
    @Published var nextPage : String = "" //переход на след страницу
    @Published var valueShowAlert: AlertAttention?
    
    @Published var showDialog: String = ""
    @Published var showDialogSupportMSg: Bool = false
    
    @Published var username: String = ""
    @Published var password: String = ""
    @Published var togleState: Bool = true
    @Published var alertLogind : StandartAlertData?
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
        
        let status = netConnection.connType
        if status == NetMonitor.ConnectionType.unknown{
            self.showAlert("Отсутствует соединение с интернетом")
        }
        
    }
    
    func updateUsernameHint(){
        let login = sharePreferenses.currentLogin
        let pass = sharePreferenses.currentPassword
        
        if(login != nil && !login!.isEmpty){
            self.username = login!
        }
        
        if(pass != nil && !pass!.isEmpty){
            self.password = pass!
        }
    }
    
    func onLoginClick(){
       // let t1 = self.username
       // let t2 = self.password
        
        if(self.username.isEmpty || self.password.isEmpty){
            showAlert("Все поля дожны быть заполнены")
            return
        }
        
        let tmp = self.username.count
        
        if(self.username.count != 10){
            showAlert("Пожалуйста введите корректные данные")
            return
        }
        
        if netConnection.connType == NetMonitor.ConnectionType.unknown{
            self.showAlert("Отсутствует соединение с интернетом")
        }else{
            verifyUser(Constants.sig, self.username, self.password)
        }
    }
    
    func verifyUser (_ sig:String,_ login:String?,_ pass:String? ){
        self.showLoading(true)
        
        sdk.doLoginApiCall(signature: sig ,username: login!, password:pass!, completionHandler: { response, error in
            if let res : UserList = response {
                if(res.response[0].login != nil ){
                    self.sharePreferenses.usersLogin=res.response
                    
                    if(self.togleState){
                        self.sharePreferenses.currentLogin = login
                        self.sharePreferenses.currentPassword = pass
                    }else{
                        self.sharePreferenses.currentPassword = nil
                    }
                    
                    self.sharePreferenses.currentUserInfo = res.response[0]
                    
                    self.updateHeaderInfo()
                }else{
                    self.showLoading(false)
                    self.showAlert("Неверное имя пользователя или пароль")
                }
                
            } else {
                self.showLoading(false)
                if let t=error{
                    LoggingTree.e("LoginPresenter/verifyUser", t)
                }
                self.showAlert("Что-то пошло не так.")
            }
        })
    }
    
    func updateHeaderInfo(){
        let idCent=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idCenter!))
        
        sdk.getCenterApiCall(idCenter: idCent, completionHandler: { response, error in
            if let res : CenterList = response {
                self.sharePreferenses.centerInfo = res.response[0]
                self.allHospitalBranch()
            } else {
                self.showLoading(false)
                self.showAlert("Ошибка загрузки информации о центре")

                if let t=error{
                    LoggingTree.e("LoginPresenter/updateHeaderInfo", t)
                }
            }
        })
    }
    
    
    func allHospitalBranch(){
        let idCent = String(Int.init(self.sharePreferenses.currentUserInfo!.idCenter!))
        
        sdk.getAllHospitalBranch(idCenter: idCent, completionHandler: { response, error in
            if let res : SettingsAllBaranchHospitalList = response {
                self.processingYandexKey(resp: res.response)
                self.currentUserInfo()
            } else {
                self.showLoading(false)
                self.showAlert("Ошибка загрузки информации о филиалах")
                
                if let t=error{
                    LoggingTree.e("LoginPresenter/getAllHospitalBranch", t)
                    
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
                LoggingTree.e("LoginPresenter. Не у всех филиалов прописан Yandex IdShop")
            }
        }
        
    }
    
    
    func currentUserInfo(){
        
        let userLogin = self.sharePreferenses.usersLogin
        
        
        if userLogin!.count == 0 {
            LoggingTree.e("LoginPresenter/currentUserInfo 0 , Ошибка загрузки информации о пользователе")
            self.showLoading(false)
            self.showAlert("Ошибка загрузки информации о пользователе")
            return
        }
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        
        for i in  userLogin! {
            let idUserItem=String(Int.init(truncating: i.idUser!))
            let idBranchItem=String(Int.init(truncating: i.idBranch!))
            
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
                    
                    for tm in userLogin! {
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
                        LoggingTree.e("LoginPresenter/currentUserInfo", t)
                        self.showLoading(false)
                        self.showAlert("Ошибка загрузки информации о пользователе")
                    }
                }
            })
        }
    }
    
    func firebaseId (){
        Messaging.messaging().token { token, error in
          if let error = error {
              LoggingTree.e("LoginPresenter/firebaseId", error)
              self.showNextpage()
              self.showLoading(false)
          } else if let token = token {
          
              self.sendFcmToken(token: token)
          }
        }
    }
    
    private var countFcmSend=0
    func sendFcmToken(token : String){
        let userLogin = self.sharePreferenses.usersLogin
        
        if userLogin!.count == 0 {
            self.showNextpage()
            self.showLoading(false)
            return
        }
        
        countFcmSend=userLogin!.count
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        let idUser=String(Int.init(self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(self.sharePreferenses.currentUserInfo!.idBranch!))
        
        for i in userLogin! {
            let idUserItem=String(Int.init(i.idUser!))
            let idBranchItem=String(Int.init(i.idBranch!))
            
            sdk.sendFcmId(idUser: idUserItem, idFilial: idBranchItem, idFcm: token, h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch, completionHandler: { response, error in
                if let res : SimpleResBooleanAsString = response {
                    self.countFcmSend -= 1
                    if self.countFcmSend <= 0 {
                        self.showNextpage()
                        self.showLoading(false)
                    }
                    
                } else {
                    if let t=error{
                        LoggingTree.e("LoginPresenter/restorePass", t)
                        self.showLoading(false)
                        self.showAlert("Произошла непредвиденная ошибка")
                    }
                }
            })
        }
    }
    
    
    func restoreAlertClick(){
//        let d = username
//        let dd = username.count
//        let dd2 = self.username.count == 10
        
        if(username.isEmpty || username.count != 10  ){
            showAlert("Проверьте правильность ввода номера")
            return
        }

        showLoading(true)

        sdk.requestNewPass(username: username, completionHandler: { response, error in
            if let res : SimpleResponseString = response {
                self.showLoading(false)

                self.alertLogind = StandartAlertData(titel: "Изменение пароля!", text: res.response!, isShowCansel: true ,someFuncOk: {() -> Void in  self.alertLogind = nil},
                                               someFuncCancel: {() -> Void in self.alertLogind = nil})
            } else {
                self.showLoading(false)
                self.showAlert("Ошибка восстановления пароля")

                if let t=error{
                    LoggingTree.e("LoginPresenter/restoreAlertClick", t)

                }
            }
        })
        
    }
    
    func sendMsgToSupport(_ email : String, _ msg : String){
        self.showLoading( true)
        
        if(self.username.count != 10){
            self.showLoading( false)
            showAlert("Телефон не указан или указан не корректно", titleM: "Ошибка!")
        }
        
        if !Utils.textFieldValidatorEmail(email){
            self.showLoading( false)
            showAlert("Email не указан или указан не корректно", titleM: "Ошибка!")
        }
        
        if(msg.count < 10){
            self.showLoading( false)
            showAlert("Сообщения нет или оно сильно короткое. Попробуйте написать более развернуто", titleM: "Ошибка!")
        }
        
        sdk.sendMsgToSupport(login: self.username, email: email, msg: msg, completionHandler: { response, error in
            if let res : SimpleResBooleanAsString = response {
                self.showLoading(false)
                LoggingTree.v("Отправлено сообщение в техподдержку:  \(self.username) \(email) \(msg)")
                self.showAlert("Ваше обращение отправлено и будет обработано в рабочие часы техподдержки. Ответ будет предоставлен на указанный адрес электронной почты.", titleM: "Отправлено успешно!")
                
                self.msgNotification()
            } else {
                self.showLoading(false)
                self.showAlert("Что-то пошло не так.")

                if let t=error{
                    LoggingTree.e("LoginPresenter/sendMsgToSupport", t)

                }
            }
        })
        
    }
    func msgNotification(){
        
    }

    
    func showAlert(_ str: String, titleM : String = "Внимание!" ){
        valueShowAlert = AlertAttention(titel: titleM, text: str)
    }
    
    func showNextpage(){
        netConnection.stopMonitoring()
        self.nextPage = "Main"
    }
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialog = "loading"
        }else{
            self.showDialog = ""
        }
    }
    
    func showMsgSupporAlert(_ isShow : Bool){
        self.showDialogSupportMSg = isShow
    }
    
    func showAlertRestorePas(){
        self.alertLogind = StandartAlertData(titel: "Вы действительно хотите восстановить пароль?", text: "Старый пароль будет заменен на новый ", isShowCansel: true ,  someFuncOk: {() -> Void in
            self.restoreAlertClick()
            self.alertLogind = nil
            
        }, someFuncCancel: {() -> Void in self.alertLogind = nil})
    }
}

