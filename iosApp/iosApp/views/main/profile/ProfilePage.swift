//
//  ProfilePage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ShowDialogCancelReceptionData{
    let item : VisitResponseIos
    let someFuncOk: (String) -> Void
    let someFuncCancel: () -> Void
}

struct ProfilePage: View {
    
    @StateObject var mainPresenter : ProfilePresenter  = ProfilePresenter()
    var clickButterMenu: (() -> Void)?
    
    
    init(clickButterMenu: (() -> Void)?){
        self.clickButterMenu = clickButterMenu
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
                                .onTapGesture {
                                    var cleanNum = self.mainPresenter.centerPhone
                                    let charsToRemove: Set<Character> = [" ", "(", ")", "-"] // "+" can stay
                                    cleanNum.removeAll(where: { charsToRemove.contains($0) })
                                    
                                    guard let phoneURL = URL(string: "tel://\(cleanNum)") else { return }
                                    UIApplication.shared.open(phoneURL, options: [:], completionHandler: nil)
                                }
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
                                .onTapGesture {
                                    let address = "https://" + self.mainPresenter.centerSite
                                    if let urlString = address.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed), let url = URL(string: urlString) {
                                        UIApplication.shared.open(url)
                                    }
                                }
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
                                getProfileIem(true, item)
                                    .listRowInsets(EdgeInsets(top: 0, leading: -8, bottom: 0, trailing: -8))
                            }
                        }
                    }

                    if(mainPresenter.latestReceptions.count > 0){
                        Section(header: Text("Прошедшие")) {
                            ForEach(mainPresenter.latestReceptions) { item in
                                getProfileIem(false, item)
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
            
            if self.mainPresenter.showDialogCancelReceptionData != nil {
                CancelReceptionAlert(dataOb : self.mainPresenter.showDialogCancelReceptionData!)
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
            
            if(self.mainPresenter.isShowAlertRecomend != nil){
                StandartAlert(dataOb: self.mainPresenter.isShowAlertRecomend!)
            }
            
        }
    }
}

private extension ProfilePage {
    @ViewBuilder
    func getProfileIem(_ isUpcoming : Bool, _ item : VisitResponseIos) -> some View {
        
        ProfileItem(item : item, isShowAlertRecomend: self.$mainPresenter.isShowAlertRecomend, isUpcoming: isUpcoming,
                    timeAndDateServer: self.mainPresenter.timeAndDateServer,
                    clickConfirmBtn : {(i: VisitResponseIos) -> Void in
            self.mainPresenter.confirmBtnClick(i)},
                    clickIAmHere: {(j: VisitResponseIos) -> Void in
            self.mainPresenter.iAmHereBtnClick(j)},
                    clickReceptionCancel:{(j: VisitResponseIos) -> Void in
            self.mainPresenter.cancelBtnClick(j)}
        )
    }
}


struct ProfilePage_Previews: PreviewProvider {
    
    static var previews: some View {
        ProfilePage( clickButterMenu: nil)
    }
}
