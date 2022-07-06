//
//  MenuView.swift
//  iosApp
//
//  Created by Михаил Хари on 08.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MenuView: View {
    var selectitem : Binding<Int>
    var selectMenuAlert : Binding<Int>
    var showMenu : Binding<Bool>
    var curentUserInfo : Binding<UserResponse>
    
    @State var visible = false
    
    var name : String = ""
    var bonuses : String? = nil
    
    init(selectitem : Binding<Int>, selectMenuAlert: Binding<Int>, showMenu : Binding<Bool>, curentUserInfo : Binding<UserResponse>){
        self.selectitem = selectitem
        self.selectMenuAlert = selectMenuAlert
        self.showMenu = showMenu
        self.curentUserInfo = curentUserInfo
        
        name = curentUserInfo.wrappedValue.name! + " " + (curentUserInfo.wrappedValue.patronymic ?? " ")
    }
    
    var body: some View {
        ZStack{
            VStack{
                Color("black_bg")
            }
            
            HStack(spacing: 0){
                VStack(alignment: .leading) {
                    Spacer()
                        .frame(height: 40.0)
                    HStack{
                        Spacer()
                        VStack{
                            VStack(alignment: .center){
                                Text(stringToInitials(str: name))
                                    .font(.title)
                                    .foregroundColor(Color.white)
                                
                            }
                            .frame(width: 80.0, height: 80.0)
                            .background(Color("lightGeen"))
                            .cornerRadius(40)
                            
                            
                            Text(name)
                                .foregroundColor(Color.white)
                            if(bonuses != nil) {
                            Text("Бонусная карта:  \(bonuses!) \u{20BD}")
                                .foregroundColor(Color.white)
                            }
                            
                            Spacer()
                                .frame(height: 10.0)
                        }
                        Spacer()
                    }.background(Color("color_primary"))
            
                    
                    ZStack(alignment: .leading){
                        if(selectitem.wrappedValue == 0){
                            Color("textSideMenu10")
                        }
                        HStack {
                            Spacer()
                                .frame(width: 8.0)
                            Image("account_circle-account_circle_symbol")
                                .imageScale(.large)
                                .foregroundColor(Color("textSideMenu"))
                            Text("Главная")
                                .foregroundColor(Color("textSideMenu"))
                                .font(.headline)
                        }
                    }
                    .frame(height: 50.0)
                    .contentShape(Rectangle())
                    .onTapGesture {
                        selectitem.wrappedValue = 0
                        showMenu.wrappedValue = false
                    }
                    
                    ZStack(alignment: .leading){
                        if(selectitem.wrappedValue == 1){
                            Color("textSideMenu10")
                        }
                        HStack {
                            Spacer()
                                .frame(width: 8.0)
                            Image("person_pin-person_pin_symbol")
                                .imageScale(.large)
                                .foregroundColor(Color("textSideMenu"))
                            Text("Специалисты")
                                .foregroundColor(Color("textSideMenu"))
                                .font(.headline)
                        }
                    }
                    .frame(height: 50.0)
                    .contentShape(Rectangle())
                    .onTapGesture {
                        selectitem.wrappedValue = 1
                        showMenu.wrappedValue = false
                    }
                    
                    ZStack(alignment: .leading){
                        if(selectitem.wrappedValue == 2){
                            Color("textSideMenu10")
                        }
                        HStack {
                            Spacer()
                                .frame(width: 8.0)
                            Image("credit_card-credit_card_symbol")
                                .imageScale(.large)
                                .foregroundColor(Color("textSideMenu"))
                            Text("Прейскурант на услуги")
                                .foregroundColor(Color("textSideMenu"))
                                .font(.headline)
                        }
                    }
                    .frame(height: 50.0)
                    .contentShape(Rectangle())
                    .onTapGesture {
                        selectitem.wrappedValue = 2
                        showMenu.wrappedValue = false
                    }
                    
                    ZStack(alignment: .leading){
                        if(selectitem.wrappedValue == 3){
                            Color("textSideMenu10")
                        }
                        HStack {
                            Spacer()
                                .frame(width: 8.0)
                            Image("list-list_symbol")
                                .imageScale(.large)
                                .foregroundColor(Color("textSideMenu"))
                            Text("Прейскурант на анализы")
                                .foregroundColor(Color("textSideMenu"))
                                .font(.headline)
                        }
                    }
                    .frame(height: 50.0)
                    .contentShape(Rectangle())
                    .onTapGesture {
                        selectitem.wrappedValue = 3
                        showMenu.wrappedValue = false
                    }
                    
                    ZStack(alignment: .leading){
                        HStack {
                            Spacer()
                                .frame(width: 8.0)
                            Image("exit_to_app-exit_to_app_symbol")
                                .imageScale(.large)
                                .foregroundColor(Color("textSideMenu"))
                            Text("Выйти")
                                .foregroundColor(Color("textSideMenu"))
                                .font(.headline)
                        }
                    }
                    .frame(height: 50.0)
                    .contentShape(Rectangle())
                    .onTapGesture { 
                        selectMenuAlert.wrappedValue = 1
                        showMenu.wrappedValue = false
                    }
                                
                    Spacer()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(.white)
                
                VStack{
                    Color("black_bg")
                }
                .frame(width: 2.0)
                
                Spacer()
                    .frame(width: 70.0)
    
            }
            .animation( Animation.easeInOut(duration: 0.4))
            
        }
        .edgesIgnoringSafeArea(.all)
        .frame(maxWidth: .infinity)
        
    }
    
    func stringToInitials(str : String) -> String{
        let s1 = String(str[...str.index(str.startIndex, offsetBy: 0)])
        
        let tt = str.range(of: " ")
        if(tt != nil){
            let s2 = String(str[str.range(of: " ")!.lowerBound...])
            let s3 = String(s2[s2.index(s2.startIndex, offsetBy: 1)...])
            if(s3.isEmpty){
                return s1
            }else{
                let s4 = String(s3[...s3.index(s3.startIndex, offsetBy: 0)])
                return s1+s4
            }
        }else {
            return s1
        }
    }
}

struct MenuView_Previews: PreviewProvider {
    @State static private var na = 1
    @State static private var no = false
    @State static private var ni = 0
    @State static private var trr : UserResponse = UserResponse()
    //selectMenuAlert нажатие на элемент где реакция показ алерта
    
    static var previews: some View {
        MenuView(selectitem: $na, selectMenuAlert: $ni, showMenu: $no, curentUserInfo: $trr)
    }
}
