//
//  VisitResponseIos.swift
//  iosApp
//
//  Created by Михаил Хари on 17.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class VisitResponseIos :  VisitResponse ,Identifiable{
    let id = UUID()
    
    var children: [VisitResponseIos]? = nil
    
    init(_ item : VisitResponse){
        super.init()
        
        self.idRecord = item.idRecord
        self.idServices = item.idServices
        self.nameServices = item.nameServices
        self.id_specialty = item.id_specialty
        self.dateOfReceipt = item.dateOfReceipt
        self.timeOfReceipt = item.timeOfReceipt
        self.status = item.status
        self.call = item.call
        self.idSotr = item.idSotr
        self.photoSotr = item.photoSotr
        self.nameSotr = item.nameSotr
        self.works = item.works
        self.idUser = item.idUser
        self.idBranch = item.idBranch
        self.nameBranch = item.nameBranch
        self.cabinet = item.cabinet
        self.comment = item.comment
        self.durationService = item.durationService
        self.price = item.price
        self.dop = item.dop
        self.isAddInBasket = item.isAddInBasket
        self.timeMils = item.timeMils
        self.userName = item.userName
        self.executeTheScenario = item.executeTheScenario
        self.durationSec = item.durationSec
    }
    
    init(servName : String, nameSotr : String, date : String, time : String, branch : String, cost : Int32){
        super.init()
        
        self.nameServices = servName
        self.nameSotr = nameSotr
        self.dateOfReceipt = date
        self.timeOfReceipt = time
        self.nameBranch = branch
        self.price = cost
    }
    
    override init(){
        super.init()
    }
    
    func getPriceString() -> String{
        return String(price)
    }
    
    func isCurrentListVisit() -> Bool{
        if( self.timeOfReceipt==nil || self.dateOfReceipt == nil){
            return false
        }
        
        let dateStrCur = self.timeOfReceipt! + " " +  self.dateOfReceipt!
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = MDate.DATE_FORMAT_HHmm_ddMMyyyy
        let date = dateFormatter.date(from: dateStrCur)!
        
        let curDate = Date()
        
        return date >= curDate
    }
}
