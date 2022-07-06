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
    
    var sharePreferenses : SharedPreferenses
    
    init(item : VisitResponseIos){
        sharePreferenses = SharedPreferenses()
        
        let currentUserInfo = sharePreferenses.currentUserInfo
        
        if(currentUserInfo != nil && item.photoSotr != nil){
            let imagePathString = item.photoSotr! + "&token=" + currentUserInfo!.apiKey!
            
            DownloadManager(imagePathString,  resultUiImage: { (tmp : UIImage) -> Void in

                self.iuImageLogo = tmp
            })
        }
    
    }
}
