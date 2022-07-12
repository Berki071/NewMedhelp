//
//  ProfilePage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ProfilePage: View {
    @State var isShowAlertRecomend: StandartAlertData? = nil
    @StateObject var mainPresenter : ProfilePresenter  = ProfilePresenter()
    var clickButterMenu: (() -> Void)?
    
    
    init(isShowAlertRecomend: StandartAlertData?, clickButterMenu: (() -> Void)?){
        self.isShowAlertRecomend = isShowAlertRecomend
        self.clickButterMenu = clickButterMenu
        
        //mainPresenter.checkCurrentUser()
    }
    
    var body: some View {
        ZStack{

            let  _ = self.mainPresenter.checkCurrentUser()
 
            VStack(spacing: 0){
                MyToolBar(title1: self.mainPresenter.centerName, isShowSearchBtn: false, clickHumburger: {() -> Void in
                    self.clickButterMenu?()
                }, strSerch: nil)
                
                HStack(spacing: 0){
            
                    VStack{
                        Image(uiImage: self.mainPresenter.iuImageLogo)   //"ico_root"
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 100.0, height: 72.0)
                    }
                    .padding(.leading, 16.0)
                    
                    VStack(alignment: .leading, spacing: 0){
                        HStack{
                            Image("account_balance-account_balance_symbol")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 15.0, height: 15.0)
                                .foregroundColor(.white)
                            Text(self.mainPresenter.nameBranch)
                                .font(.footnote)
                                .foregroundColor(Color.white)
                        }
                        .padding(.vertical, 2.0)
                     
                        HStack{
                            Image("call-call_symbol")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 15.0, height: 15.0)
                                .foregroundColor(.white)
                            Text(self.mainPresenter.centerPhone)
                                .font(.footnote)
                                .foregroundColor(Color.white)
                        }
                        .padding(.vertical, 5.0)
                        HStack{
                            Image("web-web_symbol")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 15.0, height: 15.0)
                                .foregroundColor(.white)
                            Text(self.mainPresenter.centerSite)
                                .font(.footnote)
                                .foregroundColor(Color.white)
                        }
                        .padding(.bottom, 4.0)
                        
                    }
                    .padding(.leading, 16.0)
                    
                    Spacer()
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
            
            if(self.mainPresenter.showDialogEmptyScreen){
                VStack{
                    
                    Image("sh_profile")
                        .resizable(resizingMode: .stretch)
                        .frame(width: 190.0, height: 150.0)
                    
                    Spacer()
                        .frame(height: 20.0)

                    Text("Здесь будут отображаться Ваши приемы в нашем медицинском центре")
                        .multilineTextAlignment(.center)
    
                }
            }
            
            if(self.mainPresenter.showDialogErrorScreen){
                VStack{
                    Text("Не удалось загрузить данные.\n Проверьте подключение \nк интернету и \nповторите попытку.")
                        .multilineTextAlignment(.center)
                    
                    Spacer()
                        .frame(height: 20.0)
                    
                    Button("Повторить") {
                        self.mainPresenter.showErrorScreen(false)
                        self.mainPresenter.getVisits1()
                    }
                    .padding(.all, 12)
                    .foregroundColor(.white)
                    .background(Color("color_primary"))
                    .cornerRadius(5.0)
                }
            }
            
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
    @State static private var na = 0
    static var previews: some View {
        
        ProfilePage(isShowAlertRecomend : nil, clickButterMenu: nil)
    }
}
