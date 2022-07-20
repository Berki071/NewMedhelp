//
//  BonusesItemIos.swift
//  iosApp
//
//  Created by Михаил Хари on 12.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class BonusesItemIos: BonusesItem, Identifiable{
    let id = UUID()
    
    init(_ item : BonusesItem){
        super.init(item: item)
    }
    init(date: String, status: String){
        super.init()
        self.date = date
        self.status = status
    }
}
