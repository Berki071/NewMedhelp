//
//  DoctorsPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 21.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared


class DoctorsPresenter : ObservableObject {
    @Published var showDialogLoading: Bool = false
    @Published var showDialogErrorScreen: Bool = false
    @Published var isShowAlertRecomend: StandartAlertData? = nil
    @Published var categoryList :[CategoryResponse] = []
    var doctorList : [AllDoctorsResponseIos] = []
    @Published var doctorListForRecy : [AllDoctorsResponseIos] = []
 
    var selectedOption: CategoryResponse?
    var textSearch: String = "" {
         willSet(newValue) {
             //print(">>> " + textSearch)
             filterList()
         }
    }
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
 
    
    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
        
        getSpecialtyByCenter()
    
        
    }
       
    
    
    func getSpecialtyByCenter() {
        
        if(self.sharePreferenses.currentUserInfo == nil || self.sharePreferenses.centerInfo == nil){
            LoggingTree.e("DoctorsPresenter/getSpecialtyByCenter sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            return
        }
        
        showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        
        sdk.getCategoryApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                               completionHandler: { response, error in
            if let res : SpecialtyList = response {
                if(res.spec.count > 1 || res.spec[0].id != nil){
                    let catAll = CategoryResponse(title: "Все")
                    catAll.id = -1
                    res.spec.insert(catAll, at: 0)
                    self.categoryList = res.spec
                }
                self.getDoctorList(-1)
            } else {
                if let t=error{
                    LoggingTree.e("DoctorsPresenter/getSpecialtyByCenter", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen(true)
            }
        })
    }
    
    
    
    func getDoctorList(_ idSpec: Int){
        if(idSpec == -1 && self.doctorList.count != 0){
            
            self.showLoading(false)
            return
        }
        
        if(self.doctorList.count != 0){
            self.showLoading(false)
            return
        }
        
        self.showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        
        sdk.getAllDoctors(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                          completionHandler: { response, error in
            if let res : AllDoctorsList = response {
                if(res.mResponses.count > 1 || res.mResponses[0].id != nil ){
                    
                    var tmpList :[AllDoctorsResponseIos] = []
                    
                    res.mResponses.forEach{ i in
                        tmpList.append(AllDoctorsResponseIos(id: i.id, fio_doctor: i.fio_doctor, id_specialties_string: i.id_specialties_string, experience: i.experience, name_specialties: i.name_specialties, dop_info: i.dop_info, image_url: i.image_url))
                    }
                    
                    self.doctorList = tmpList
                    self.doctorListForRecy = tmpList
                }
                self.showLoading(false)
                
                
                
            } else {
                if let t=error{
                    LoggingTree.e("DoctorsPresenter/getDoctorList", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen(true)
            }
        })
    }
    
    func filterList(){
        
        var tmpList :[AllDoctorsResponseIos] = []
        
       
        if(self.selectedOption == nil || self.selectedOption!.id == -1){
            tmpList = doctorList
        }else{
            doctorList.forEach{i in
                let listSpec = i.getIdSpecialtiesIntList()
                
                if listSpec == nil {
                    return
                }else if(listSpec?.count == 1) {
                    let tmpIdSpec = (listSpec![0] as? Int)
                    
                    if(tmpIdSpec != nil && tmpIdSpec! == self.selectedOption!.id) {
                        tmpList.append(i)
                        return
                    }
                }else{
                    listSpec?.forEach{j in
                        let tmpIdSpec2 = (j as? Int)
                        if(tmpIdSpec2 != nil && tmpIdSpec2! == self.selectedOption!.id){
                            tmpList.append(i)
                            return
                        }
                    }
                }
            }
        }
        
        
        if(!textSearch.isEmpty && tmpList.count>0){
            //print(">>> " + textSearch)
            
            var tmpList2 :[AllDoctorsResponseIos] = []
            tmpList.forEach{ i in
                if(i.fio_doctor!.lowercased().contains(textSearch.lowercased())){
                    tmpList2.append(i)
                }
            }
            
            tmpList = tmpList2
        }
        
        
        doctorListForRecy = tmpList
    }
    
    
    
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
    
    
    func showErrorScreen(_ isShow : Bool){
        if isShow {
            showDialogErrorScreen = true
            doctorListForRecy = []
        }else{
            showDialogErrorScreen = false
        }
        
    }
    
    
}
