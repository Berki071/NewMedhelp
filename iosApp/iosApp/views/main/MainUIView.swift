//
//  MainUIView.swift
//  iosApp
//
//  Created by Михаил Хари on 27.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MainUIView: View {
    @ObservedObject var mainPresenter = MainPresenter()
    
    init() {
        // for navigation bar title color
        UINavigationBar.appearance().titleTextAttributes = [NSAttributedString.Key.foregroundColor:UIColor.white]
       // For navigation bar background color
        //UINavigationBar.appearance().backgroundColor = .green
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
                    withAnimation {
                        self.mainPresenter.showMenu = true
                    }
                }
            }
        
        if(self.mainPresenter.nextPage == "Login"){
            LoginUiView()
        }else{
            
            NavigationView {
                GeometryReader { geometry in
                    ZStack(alignment: .leading) {
                        
                        MainView(mainP: self.mainPresenter)
                            .frame(width: geometry.size.width, height: geometry.size.height)
                        // .offset(x: self.showMenu ? geometry.size.width/2 : 0)
                            .disabled(self.mainPresenter.showMenu ? true : false)
                        
                        
                        
                        if self.mainPresenter.showMenu {
                            MenuView(selectitem : self.$mainPresenter.selectMenuPage, selectMenuAlert: self.$mainPresenter.selectMenuAlert, showMenu: self.$mainPresenter.showMenu)
                            //.frame(width: geometry.size.width/1.4)
                                .transition(.move(edge: .leading))
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
                    .navigationBarItems(leading: (
                        Button(action: {
                            withAnimation {
                                self.mainPresenter.showMenu.toggle()
                            }
                        }) {
                            Image(systemName: "line.horizontal.3")
                                .foregroundColor(Color.white)
                                .imageScale(.large)
                        }
                    ))
                    
                    
                }
                
            }
        }
        
        
    }
}

struct MainView: View {
    @ObservedObject var mainP : MainPresenter
    
    var body: some View {
        if(mainP.selectMenuPage == 0){
            ProfilePage()
                .navigationBarTitle(mainP.titleTop, displayMode: .inline)
            
        }else if(mainP.selectMenuPage == 1){
            DoctorsPage()
                .navigationBarTitle("Специалисты", displayMode: .inline)
              
        }else if(mainP.selectMenuPage == 2){
            ServicesPage()
                .navigationBarTitle("Прейскурант на услуги", displayMode: .inline)
               
        }
    }
}

struct MainUIView_Previews: PreviewProvider {
    static var previews: some View {
        MainUIView()
    }
}
