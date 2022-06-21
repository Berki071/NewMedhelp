//
//  ProfilePage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ProfilePage: View {
    @ObservedObject var mainPresenter = ProfilePresenter()
    @State var isShowAlertRecomend: StandartAlertData? = nil
    
    
    var body: some View {
        ZStack{
            //Color("color_primary")
            
            
            VStack(spacing: 0){
                HStack {
                   
                }
                .frame(maxWidth: .infinity)
                .background(Color("color_primary"))
                
            
                List {
                    if(mainPresenter.actualReceptions.count > 0){
                        Section(header: Text("Предстоящие")) {
                            ForEach(mainPresenter.actualReceptions) { item in
                                ProfileItem(item : item, isShowAlertRecomend: $isShowAlertRecomend)
                                    .listRowInsets(EdgeInsets(top: 0, leading: -8, bottom: 0, trailing: -8))
                            }
                        }
                    }
                    
                    if(mainPresenter.latestReceptions.count > 0){
                        Section(header: Text("Прошедшие")) {
                            ForEach(mainPresenter.latestReceptions) { item in
                                ProfileItem(item : item, isShowAlertRecomend: $isShowAlertRecomend)
                                    .listRowInsets(EdgeInsets(top: 0, leading: -8, bottom: 0, trailing: -8))
                            }
                        }
                    }
                }
                .listStyle(.sidebar)
                .frame(maxWidth: .infinity)
        
        }
        .background(.white)
        
        if(self.mainPresenter.showDialogLoading == true){
            LoadingView()
        }
        
        if(isShowAlertRecomend != nil){
            StandartAlert(dataOb: isShowAlertRecomend!)
        }
        
    }
    //.edgesIgnoringSafeArea(.all)
    
}

}

struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage(isShowAlertRecomend : nil)
    }
}
