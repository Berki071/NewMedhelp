//
//  ProfilePresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 14.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

class ProfilePresenter: ObservableObject {
    
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
    }
}
