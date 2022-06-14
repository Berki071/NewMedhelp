//
//  MenuView.swift
//  iosApp
//
//  Created by Михаил Хари on 08.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MenuView: View {
    @Binding var selectitem : Int
    @Binding var selectMenuAlert : Int
    @Binding var showMenu : Bool
    
    var body: some View {
        ZStack{
            
            Color("black_bg")
            
            HStack{
                VStack(alignment: .leading) {
                    Spacer()
                        .frame(height: 90.0)
                    
                    ZStack(alignment: .leading){
                        if(selectitem == 0){
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
                        selectitem = 0
                        showMenu=false
                    }
                    
                    ZStack(alignment: .leading){
                        if(selectitem == 1){
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
                        selectitem = 1
                        showMenu=false
                    }
                    
                    ZStack(alignment: .leading){
                        if(selectitem == 2){
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
                        selectitem = 2
                        showMenu=false
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
                        //selectitem = 3
                        selectMenuAlert=1
                        showMenu=false
                    }
                                
                    Spacer()
                }
                
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(.white)
                
                Spacer()
                    .frame(width: 70.0)
    
            }
            
        }
        .edgesIgnoringSafeArea(.all)
        .frame(maxWidth: .infinity)
        
    }
}

struct MenuView_Previews: PreviewProvider {
    @State static private var na = 1
    @State static private var no = false
    @State static private var ni = 0
    
    //selectMenuAlert нажатие на элемент где реакция показ алерта
    
    static var previews: some View {
        MenuView(selectitem: $na, selectMenuAlert: $ni, showMenu: $no)
    }
}
