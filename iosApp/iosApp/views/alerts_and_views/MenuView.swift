//
//  MenuView.swift
//  iosApp
//
//  Created by Михаил Хари on 08.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import SwiftUIPager

struct MenuView: View {
    var selectitem : Binding<Int>
    var selectMenuAlert : Binding<Int>
    var showMenu : Binding<Bool>
    var listBonuses : [BonusesItemIos]? = nil
    var clickBonus : (() -> Void)?

    var bonuses : String? = nil
    var curentUserInfo : UserResponse
    var allUsers : [UserResponse]
    
    var sharePreferenses : SharedPreferenses
    
    @ObservedObject var page: Page = .first()
    
    init(selectitem : Binding<Int>, selectMenuAlert: Binding<Int>, showMenu : Binding<Bool>,listBonuses: [BonusesItemIos]?,clickBonus: (() -> Void)?){
        self.selectitem = selectitem
        self.selectMenuAlert = selectMenuAlert
        self.showMenu = showMenu
        self.listBonuses = listBonuses
        self.clickBonus = clickBonus
        
        sharePreferenses = SharedPreferenses()
        
        curentUserInfo = sharePreferenses.currentUserInfo ?? UserResponse()
        allUsers = sharePreferenses.usersLogin ?? []
    
        self.page.update(.move(increment: getSelectItmIndex(item: curentUserInfo)))
        
        bonuses = self.getSumBonuses(listBonuses: listBonuses)
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
                    VStack{
                        Pager(page: page,
                              data: allUsers,
                              id: \.self,
                              content: { index in
                            let curName = (index.name ?? " ") + " " + (index.patronymic ?? " ")
                           // let _ = testSelectedUser(item : index)
                            
                            HStack{
                                VStack{
                                    VStack(alignment: .center){
                                        Text(stringToInitials(str: curName))
                                            .font(.title)
                                            .foregroundColor(Color.white)

                                    }
                                    .frame(width: 80.0, height: 80.0)
                                    .background(getColor(idex: getSelectItmIndex(item: index)))
                                    .cornerRadius(40)

                                    Text(curName)
                                        .foregroundColor(Color.white)
                                }
                            }
                            .frame(width: 300.0, height: 120.0)
                        })
                            .itemAspectRatio(0.9)
                            .itemSpacing(60)
                            .interactive(scale: 0.7)
                            .onPageChanged({ pageIndex in
                                let tt : Int = pageIndex
                                let _ = testSelectedUser(item : tt)
                                showMenu.wrappedValue = false
                                })
                            .frame(height: 130.0)
                            

                        if(bonuses != nil) {
                            Text("Бонусная карта:  \(bonuses!) \u{20BD}")
                                .underline()
                                .foregroundColor(Color.white)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    self.clickBonus?()
                                }
                        }

                        Spacer()
                            .frame(height: 10.0)

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
                
                VStack{
                    Color("transparent")
                }
                .frame(width: 70.0)
                .contentShape(Rectangle())
                .onTapGesture {
                    showMenu.wrappedValue = false
                }
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
    
    func getSumBonuses(listBonuses: [BonusesItem]?) -> String?{
        if(listBonuses == nil ){
            return nil
        }
        
        if listBonuses!.count == 0 {
            return "0"
        }
        
        var sum : Int32 = 0
        listBonuses!.forEach(){i in
            if (i.status == "popoln"){
                sum += i.value;
            }else if (i.status == "snyatie"){
                sum -= i.value;
            }
        }
        
        let tmp : Int = Int(sum)
        
        return String(tmp)
    }
    
    func getColor(idex : Int) -> Color{
        switch(idex%5){
        case 0 : return Color("1BgC")
        case 1 : return Color("2BgC")
        case 2 : return Color("3BgC")
        case 3 : return Color("4BgC")
        case 4 : return Color("5BgC")
        default : return Color("1BgC")
        }
    }
    func getSelectItmIndex(item : UserResponse) -> Int{
        return allUsers.firstIndex(where: {$0.idUser == item.idUser}) ?? 0
    }
    

    func testSelectedUser(item : Int){
        sharePreferenses.currentUserInfo = allUsers[item]
    }
}

struct MenuView_Previews: PreviewProvider {
    @State static private var na = 1
    @State static private var no = false
    @State static private var ni = 0
    @State static private var trr : UserResponse = UserResponse()
    //selectMenuAlert нажатие на элемент где реакция показ алерта
    
    static var previews: some View {
        MenuView(selectitem: $na, selectMenuAlert: $ni, showMenu: $no, listBonuses: nil, clickBonus: nil)
    }
}
