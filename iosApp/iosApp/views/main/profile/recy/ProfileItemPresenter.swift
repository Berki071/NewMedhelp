//
//  ProfileItemPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 29.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class ProfileItemPresenter : ObservableObject {
    @Published var iuImageLogo : UIImage =  UIImage(named: "sh_doc")!
    var timeAndDateServer: Date?
    
    var sharePreferenses : SharedPreferenses
    var centerResponse : CenterResponse
    
    init(item : VisitResponseIos, timeAndDateServer: Date?){
        self.timeAndDateServer = timeAndDateServer
        
        sharePreferenses = SharedPreferenses()
        centerResponse = sharePreferenses.centerInfo!
        
        let currentUserInfo = sharePreferenses.currentUserInfo
        
        if(currentUserInfo != nil && item.photoSotr != nil){
            let imagePathString = item.photoSotr! + "&token=" + currentUserInfo!.apiKey!
            
            DownloadManager(imagePathString,  resultUiImage: { (tmp : UIImage) -> Void in

                self.iuImageLogo = tmp
            })
        }
    
        
    }
    
    func isTheTimeConfirm(_ timeAndDate: Date?) -> Bool{
        if timeAndDate == nil || timeAndDateServer == nil{
            return false
        }
        
        var timeTo = timeAndDateServer!
        timeTo.addTimeInterval(TimeInterval(Int(centerResponse.timeForConfirm)))
        
        return timeAndDate! >= timeAndDateServer! && timeAndDate! <= timeTo
        
    }
    
    func isTheTimeCancel(_ timeAndDate: Date?) -> Bool {
        if timeAndDate == nil || timeAndDateServer == nil{
            return false
        }
        
        var timeTo = timeAndDateServer!
        timeTo.addTimeInterval(TimeInterval(Int(centerResponse.timeForDenial)))
        
        return timeAndDate! >= timeAndDateServer! && timeAndDate! >= timeTo
    }
}
