//
//  DoctorsItemPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 29.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class DoctorsItemPresenter: ObservableObject{
    @Published var iuImageLogo : UIImage =  UIImage(named: "sh_doc")!
    
    var sharePreferenses : SharedPreferenses
    
    init(item : AllDoctorsResponseIos){
        sharePreferenses = SharedPreferenses()
        
        let currentUserInfo = sharePreferenses.currentUserInfo
        
        if(currentUserInfo != nil && item.image_url != nil){
            let imagePathString = item.image_url! + "&token=" + currentUserInfo!.apiKey!
            
            DownloadManager(imagePathString,  resultUiImage: { (tmp : UIImage) -> Void in
                item.iuImageLogo = tmp
                self.iuImageLogo = tmp
            })
        }
    
    }
}
