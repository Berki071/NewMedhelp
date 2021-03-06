//
//  ProfileItem.swift
//  iosApp
//
//  Created by Михаил Хари on 17.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ProfileItem: View {
    var item : VisitResponseIos
    @Binding var isShowAlertRecomend : StandartAlertData?
    @ObservedObject var mainPresenter : ProfileItemPresenter
    var isUpcoming : Bool
    var clickConfirmBtn: ((VisitResponseIos) -> Void)?
    var clickIAmHere: ((VisitResponseIos) -> Void)?
    var clickReceptionCancel: ((VisitResponseIos) -> Void)?
    
    
    init(item : VisitResponseIos, isShowAlertRecomend : Binding<StandartAlertData?>, isUpcoming: Bool, timeAndDateServer: Date?, clickConfirmBtn:  ((VisitResponseIos) -> Void)?, clickIAmHere: ((VisitResponseIos) -> Void)?, clickReceptionCancel: ((VisitResponseIos) -> Void)?){
        self.item = item
        self._isShowAlertRecomend = isShowAlertRecomend
        self.isUpcoming = isUpcoming
        self.clickConfirmBtn = clickConfirmBtn
        self.clickIAmHere = clickIAmHere
        self.clickReceptionCancel = clickReceptionCancel
        
        mainPresenter = ProfileItemPresenter(item: item, timeAndDateServer: timeAndDateServer)
    }
    
    var body: some View {
        if(item.idRecord == 0){
            ProfileTitle(item : item)
        }else{
            
            ZStack{
                
                VStack{
                    HStack {
                        Spacer()
                    }
                    
                    HStack(alignment: .top){
                        
                        Image(uiImage: self.mainPresenter.iuImageLogo)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .padding(8.0)
                            .frame(width: 72.0, height: 72.0)
                        
                        
                        VStack(alignment: .leading){
                            HStack {
                                Spacer()
                            }
                            
                            HStack() {
                                Text(item.nameServices!)
                                    .font(.callout)
                                    .fontWeight(.bold)
                                
                                    .multilineTextAlignment(.center)
                                    .lineLimit(2)
                                    .fixedSize(horizontal: false, vertical: true)
                                //.frame(maxWidth: .infinity, horizontal: false)
                            }
                            .frame(maxWidth: .infinity)
                            
                            if(item.idRecord != 0){
                                
                                HStack(alignment: .top){
                                    Text("Врач:")
                                        .fontWeight(.bold)
                                    Spacer()
                                    Text(item.nameSotr!)
                                }
                                .padding(.top, 3.0)
                                .font(.footnote)
                                HStack(alignment: .top){
                                    Text("Дата приема:")
                                        .fontWeight(.bold)
                                    Spacer()
                                    Text(item.dateOfReceipt!)
                                }
                                .padding(.top, 3.0)
                                .font(.footnote)
                                HStack(alignment: .top){
                                    Text("Время приема:")
                                        .fontWeight(.bold)
                                    Spacer()
                                    Text(item.timeOfReceipt!)
                                }
                                .padding(.top, 3.0)
                                .font(.footnote)
                                HStack(alignment: .top){
                                    Text("Филиал:")
                                        .fontWeight(.bold)
                                    Spacer()
                                    Text(item.nameBranch!)
                                }
                                .padding(.top, 3.0)
                                .font(.footnote)
                                
                                HStack(alignment: .top){
                                    Text("Цена:")
                                        .fontWeight(.bold)
                                    Spacer()
                                    Text(item.getPriceString() + "р")
                                }
                                .padding(.top, 3.0)
                                .font(.footnote)
                                
                                if(item.comment != nil && !item.comment!.isEmpty){
                                    HStack{
                                        Text("Рекомендации перед приемом")
                                            .underline()
                                            .font(.footnote)
                                            .fontWeight(.bold)
                                        Image("new_releases_symbol")
                                            .foregroundColor(Color("color_primary"))
                                        
                                    }
                                    .padding(.top, 4.0)
                                    .padding(.bottom, 8.0)
                                    .contentShape(Rectangle())
                                    .onTapGesture {
                                        let alertLogind = StandartAlertData(titel: "Рекомендации перед приемом", text: item.comment!, isShowCansel: false ,someFuncOk: {() -> Void in self.isShowAlertRecomend = nil},someFuncCancel: {() -> Void in})
                                        
                                        self.isShowAlertRecomend = alertLogind
                                    }
                                    
                                    
                                } else {
                                    Spacer()
                                        .frame(height: 16.0)
                                }
                            }
                        }
                        .padding(.trailing, 8.0)
                        
                    }
                    .frame(maxWidth: .infinity)
                    
                    if(isUpcoming){
                        HStack{
                            if(item.call == "нет" && self.mainPresenter.isTheTimeConfirm(item.getTimeAndDateInDateFormat())){
                                Button("Подтвердить") {
                                    self.clickConfirmBtn?(item)
                                }
                                .frame(maxWidth: .infinity)
                                .frame(height: 15.0)
                                .padding(.all, 12)
                                .foregroundColor(.white)
                                .background(Color("color_primary"))
                                .cornerRadius(5.0)
                            }
                            
                            if(self.mainPresenter.isTheTimeCancel(item.getTimeAndDateInDateFormat())){
                                Button("Отменить") {
                                    self.clickReceptionCancel?(item)
                                }
                                .frame(maxWidth: .infinity)
                                .frame(height: 15.0)
                                .padding(.all, 12)
                                .foregroundColor(.white)
                                .background(Color("color_primary"))
                                .cornerRadius(5.0)
                            }else{
                                
                                if(item.status == "wk" || item.status == "wkp"){
                                    Button("Я пришел") {
                                        self.clickIAmHere?(item)
                                    }
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 15.0)
                                    .padding(.all, 12)
                                    .foregroundColor(.white)
                                    .background(Color("color_primary"))
                                    .cornerRadius(5.0)
                                }
                            }
                        }
                        .padding(.horizontal, 8.0)
                        .padding(.bottom, 12.0)
                        
                        //                        HStack{
                        //
                        //
                        //                            //Button("Оплатить") {
                        //                            //
                        //                            //}
                        //                            //.frame(maxWidth: .infinity)
                        //                            //.frame(height: 15.0)
                        //                            //.padding(.all, 12)
                        //                            //.foregroundColor(.white)
                        //                            //.background(Color("color_primary"))
                        //                            //.cornerRadius(5.0)
                        ////                            Button("Перенести") {
                        ////
                        ////                            }
                        ////                            .frame(maxWidth: .infinity)
                        ////                            .frame(height: 15.0)
                        ////                            .padding(.all, 12)
                        ////                            .foregroundColor(.white)
                        ////                            .background(Color("color_primary"))
                        ////                            .cornerRadius(5.0)
                        //
                        //                        }
                        //                        .padding(.horizontal, 8.0)
                        //                        .padding(.bottom, 8.0)
                        //                    }
                        
                        
                        
                        
                    }
                    
                    
                    
                }
                .overlay(
                    RoundedRectangle(cornerRadius: 6)
                        .stroke(Color("black_bg3"), lineWidth: 3)
                )
                .padding(.horizontal, 8.0)
                .padding(.vertical, 4.0)
                
            }
        }
        
        
    }
}

struct ProfileItem_Previews: PreviewProvider {
    @State static private var ite = VisitResponseIos(servName : "name Service", nameSotr : "name sotr", date : "20.06.2022", time : "15:00", branch : "branch1", cost : 1999 )
    @State static private var isShowAlertRecomen: StandartAlertData? = nil
    
    static var previews: some View {
        
        ProfileItem(item : ite, isShowAlertRecomend : $isShowAlertRecomen, isUpcoming: true, timeAndDateServer: nil, clickConfirmBtn: nil,
                    clickIAmHere: nil, clickReceptionCancel: nil)
    }
}
