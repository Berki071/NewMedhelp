//
//  MainPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class MainPresenter : ObservableObject{
    @Published var nextPage : String = "" //переход на след страницу
    
    @Published var titleTop : String = ""
    
    @Published var showMenu = false
    @Published var selectMenuPage = 0
    @Published var selectMenuAlert = 0  //1 алер выхода
 
    var listBonuses : [BonusesItemIos]? = nil
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
    @Published var isShowBonusesListAlert : BonusesListAlertData? = nil

    init(startPage: Int){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
        
        selectMenuPage = startPage
    
        if(sharePreferenses.centerInfo != nil && sharePreferenses.centerInfo!.title != nil){
            titleTop=sharePreferenses.centerInfo!.title!
        }
        
        getAllBonuses()
    }
    
    func logOut(){
        LoggingTree.v("Выход из приложения")
        
        self.sharePreferenses.currentPassword = "";
        self.sharePreferenses.usersLogin = nil;
        self.sharePreferenses.currentUserInfo = nil;
        
        nextPage = "Login"
    }
    
    
    func getAllBonuses(){
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo?.idUser ?? -1))
        let h_dbName = self.sharePreferenses.centerInfo?.db_name ?? "-1"
        
        let tmp = Int(self.sharePreferenses.centerInfo?.idCenter ?? -1)
        let idCenter = String(tmp)
         
        sdk.getAllBonuses(h_dbName: h_dbName, h_idKl: idUser, idCenter:  idCenter,
                          completionHandler: { response, error in
            if let res : BonusesResponse = response {
                if(res.response!.count > 1 || res.response![0].date != nil){
                    var tmpL: [BonusesItemIos] = []
                    
                    res.response?.forEach{ i in
                        tmpL.append(BonusesItemIos(i))
                    }
                    
                    self.listBonuses = tmpL
                }
            } else {
                if let t=error{
                    LoggingTree.e("MainPresenter/getAllBonuses", t)
                }
            }
        })
    }
    
    func showBonusesList(){
        self.isShowBonusesListAlert = BonusesListAlertData(listBonuses: listBonuses!, someFuncOk: {() -> Void in
            self.isShowBonusesListAlert = nil
        })
    }

}
