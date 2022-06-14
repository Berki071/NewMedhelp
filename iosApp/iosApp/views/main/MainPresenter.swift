//
//  MainPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class MainPresenter : ObservableObject{
    @Published var nextPage : String = "" //переход на след страницу
    
    @Published var showMenu = false
    @Published var selectMenuPage = 0
    @Published var selectMenuAlert = 0  //1 алер выхода
    
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
    }
    
    func logOut(){
        LoggingTree.v("Выход из приложения")
        
        self.sharePreferenses.currentPassword = "";
        self.sharePreferenses.usersLogin = nil;
        self.sharePreferenses.currentUserInfo = nil;
        
        nextPage = "Login"
    }
    
}
