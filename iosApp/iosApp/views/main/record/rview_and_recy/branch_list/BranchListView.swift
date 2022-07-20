//
//  BranchListView.swift
//  iosApp
//
//  Created by Михаил Хари on 19.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct BranchListView: View {
    @StateObject var presenter: BranchListPresenter
    
    init(selectServis: ServiceResponseIos?, selctDoctor: AllDoctorsResponseIos?, showDialogLoading: Binding<Bool>, listenerSelectedBranch: @escaping (SettingsAllBranchHospitalResponse) -> Void){
        _presenter = StateObject(wrappedValue: BranchListPresenter(selectServis: selectServis, selctDoctor: selctDoctor, showDialogLoading: showDialogLoading, listenerSelectedBranch: listenerSelectedBranch))
    }
    
    var body: some View {
        if(self.presenter.listBranch.count > 0){
            VStack{
                ForEach(self.presenter.listBranch) { item in
                    ItemBranch(item: item, clickItem: {(i: SettingsAllBranchHospitalResponse) -> Void in
                        self.presenter.clickFavoriteBranch(item: i)
                    })
                    
                }
            }
            .padding(2.0)
        }
    }
}

struct BranchListView_Previews: PreviewProvider {
//    static func createListBranch() -> Binding<[BranchLineItem]>{
//        let tt1 = SettingsAllBranchHospitalResponse()
//        tt1.nameBranch = "Name1"
//        tt1.idBranch=0
//        let tt2 = SettingsAllBranchHospitalResponse()
//        tt2.nameBranch = "Name2"
//        tt2.idBranch=1
//        let tt3 = SettingsAllBranchHospitalResponse()
//        tt3.nameBranch = "Name3"
//        tt3.idBranch=2
//        tt3.isFavorite = true
//        let tt4 = SettingsAllBranchHospitalResponse()
//        tt4.nameBranch = "Name4"
//        tt4.idBranch=3
//        let tt5 = SettingsAllBranchHospitalResponse()
//        tt5.nameBranch = "Name5"
//        tt5.idBranch=4
//
//        let tttb = BranchLineItem(item1: tt1, item2: tt2)
//        let tttb2 = BranchLineItem(item1: tt3, item2: tt4)
//        let tttb3 = BranchLineItem(item1: tt5, item2: nil)
//
//        let listBranch = [tttb,tttb2,tttb3]
//
//        return Binding(get: {listBranch}, set: {_ in})
//    }
    
    @State static var showDialogLoading : Bool = false
    
    static var previews: some View {
        BranchListView(selectServis: ServiceResponseIos(id: 1,title: "title", value: 100), selctDoctor: nil, showDialogLoading: $showDialogLoading, listenerSelectedBranch: {(SettingsAllBranchHospitalResponse) -> Void in})
    }
}
