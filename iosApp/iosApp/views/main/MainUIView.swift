//
//  MainUIView.swift
//  iosApp
//
//  Created by Михаил Хари on 27.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
//import FirebaseDatabaseSwift

struct MainUIView: View {
    @StateObject var mainPresenter: MainPresenter
   
    
    init(startPage: Int) {
        _mainPresenter = StateObject(wrappedValue: MainPresenter(startPage: startPage))
    }
    
    
    var body: some View {
        let drag = DragGesture()
            .onEnded {
                if $0.translation.width < -100 {
                    withAnimation {
                        self.mainPresenter.showMenu = false
                    }
                }
                
                if $0.translation.width > 100 {
                    //withAnimation {
                        self.mainPresenter.showMenu = true
                   // }
                }
            }
        
        if(self.mainPresenter.nextPage == "Login"){
            LoginUiView()
        }else{
            ZStack{
                
                GeometryReader { geometry in
                    ZStack(alignment: .leading) {
                        
                        MainView(mainP: self.mainPresenter)
                            .frame(width: geometry.size.width, height: geometry.size.height)
                        // .offset(x: self.showMenu ? geometry.size.width/2 : 0)
                            .disabled(self.mainPresenter.showMenu ? true : false)
                        
                        
                        
                        if self.mainPresenter.showMenu {
                            MenuView(selectitem : self.$mainPresenter.selectMenuPage, selectMenuAlert: self.$mainPresenter.selectMenuAlert, showMenu: self.$mainPresenter.showMenu, listBonuses: self.mainPresenter.listBonuses, clickBonus: {() -> Void in
                                self.mainPresenter.showMenu = false
                                self.mainPresenter.showBonusesList()
                            })
                                .transition(.move(edge: .leading))
                        }
                        
                        if self.mainPresenter.isShowBonusesListAlert != nil {
                            BonusesListAlert(dataOb: self.mainPresenter.isShowBonusesListAlert!)
                        }
                        
                        if(self.mainPresenter.selectMenuAlert == 1){
                            StandartAlert(dataOb: StandartAlertData(titel: "", text: "Вы действительно хотите выйти из учетной записи?", isShowCansel: true, someFuncOk: {() -> Void in
                                self.mainPresenter.selectMenuAlert = 0
                                self.mainPresenter.logOut()
                            }, someFuncCancel: {() -> Void in
                                self.mainPresenter.selectMenuAlert = 0
                            }))
                        }
                    }
                    .gesture(drag)
                }    
            }
        }
        
        
    }
}


struct MainView: View {
    @StateObject var mainP : MainPresenter
   
    var body: some View {
 
        if(self.mainP.selectMenuPage == 0){
            ProfilePage( clickButterMenu:{() -> Void in
                    self.mainP.showMenu = true
        })
        
        }else if(self.mainP.selectMenuPage == 1){
            DoctorsPage(clickButterMenu:{() -> Void in
               // withAnimation {
                    self.mainP.showMenu = true
               // }
            })
            
        }else if(self.mainP.selectMenuPage == 2){
            ServicesPage(clickButterMenu:{() -> Void in
             //   withAnimation {
                    self.mainP.showMenu = true
              //  }
            })
        }
        else if(self.mainP.selectMenuPage == 3){
            AnalisePrices(clickButterMenu:{() -> Void in
              // withAnimation {
                    self.mainP.showMenu = true
                //}
            })
        }
    }
}

struct MainUIView_Previews: PreviewProvider {
    static var previews: some View {
        MainUIView(startPage: 0)
    }
}
