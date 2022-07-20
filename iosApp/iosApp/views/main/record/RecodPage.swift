//
//  RecodPage.swift
//  iosApp
//
//  Created by Михаил Хари on 18.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct BranchLineItem: Identifiable, Hashable{
    let id = UUID()
    let item1: SettingsAllBranchHospitalResponse
    let item2: SettingsAllBranchHospitalResponse?
}

struct RecodPage: View {
    @StateObject var mainPresenter : RecodPresenter

    var backPage : String
    @State var clickBack = false
    
    init(selectServis: ServiceResponseIos?, selctDoctor: AllDoctorsResponseIos?, backPage : String){
        self.backPage = backPage
        self._mainPresenter =  StateObject(wrappedValue: RecodPresenter(selectServis: selectServis, selctDoctor:selctDoctor))
    }
    
    var body: some View {
        if(clickBack){
            switch(backPage){
            case "DoctorsPage": MainUIView(startPage: 1)
            case "ServicesSelctForDoctor": ServicesSelctForDoctor(selectDocttorForRecord: self.mainPresenter.selctDoctor!)
            default:
                MainUIView(startPage: 0)
            }
            
        }else{
            ZStack{
                VStack(spacing: 0){
                    BranchListView(selectServis: self.mainPresenter.selectServis, selctDoctor: self.mainPresenter.selctDoctor, showDialogLoading: self.$mainPresenter.showDialogLoading, listenerSelectedBranch: {(i: SettingsAllBranchHospitalResponse) -> Void in
                        mainPresenter.selectNeqBranch(item: i)
                    })
                    
                    HStack{
                        Spacer()
                        Text("Выберите дату, на которую планируете записаться")
                            .font(.caption)
                            .padding(.vertical)
                        Spacer()
                    }
                    .background(Color("semi_gray"))
                    
                    Spacer()
                }
                .padding(.top, 44.0)
                
                VStack{
                    MyToolBar(title1: "Расписание", clickHumburger: {() -> Void in
                        self.clickBack = true
                    }, isShowImageFreeLine: false)
                    Spacer()
                }
                
                
                if(self.mainPresenter.showDialogLoading == true){
                    LoadingView()
                }
            }
        }
    }
}

struct RecodPage_Previews: PreviewProvider {

    static var previews: some View {
        RecodPage(selectServis: nil, selctDoctor: nil, backPage: "backPage")
    }
}
