//
//  BalanceListAlert.swift
//  iosApp
//
//  Created by Михаил Хари on 12.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct BonusesListAlertData{
    var listBonuses : [BonusesItemIos] = []
    let someFuncOk: () -> Void
}

struct BonusesListAlert: View {
    var dataOb : BonusesListAlertData
    
    init(dataOb : BonusesListAlertData){
        self.dataOb = dataOb
    }
    
    var body: some View {
        ZStack{
            Color("black_bg")
            
            ZStack{
                VStack{
                    MyToolBar(title1: "Бонусный баланс")
                    
                    List{
                        ForEach(dataOb.listBonuses , id: \.self) {  group in
                            ZStack{
                                HStack{
                                    if(group.status == "popoln"){
                                        Text(group.date!)
                                            .foregroundColor(Color("light_green"))
                                        
                                        Spacer()
                                        Text("+" + String(group.value))
                                            .foregroundColor(Color("light_green"))
                                    }else{
                                        Text(group.date!)
                                            .foregroundColor(Color("red"))
                                        Spacer()
                                        Text("-" + String(group.value))
                                            .foregroundColor(Color("red"))
                                    }
                                }
                                .padding()
                                
                            }
                            .overlay(
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color("black_bg3"), lineWidth: 1)
                            )
                            .listRowSeparator(.hidden)
                            .listRowInsets(EdgeInsets())
                            .padding(.horizontal, 8.0)
                        }
                    }
                    .listStyle(PlainListStyle())
                    
                    HStack{
                        Color(.black)
                    }
                    .frame(height: 2.0)
                    
                    HStack{
                        Spacer()
                        Text("Итого: ")
                            .font(.title3)
                            .fontWeight(.bold)
                        Text(self.getSumBonuses(listBonuses: self.dataOb.listBonuses) ?? "0")
                            .font(.title3)
                            .fontWeight(.bold)
                    }
                    .padding(.trailing)
                    
                    Button(action: {
                        dataOb.someFuncOk()
                    }) {
                        Text("Закрыть")
                            .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                            .foregroundColor(Color.white)
                            .background(Color("color_primary"))
                            .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                    }
                    .padding(8.0)
                }
            }
            .background(Color(.white))
            .cornerRadius(6)
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color("black_bg3"), lineWidth: 3)
            )
            .padding(.horizontal, 8.0)
            .padding(.vertical, 80.0)
        }
        .ignoresSafeArea()
    }
    
    
    func getSumBonuses(listBonuses: [BonusesItemIos]?) -> String?{
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
}

struct BalanceListAlert_Previews: PreviewProvider {
    static var listBonuses : [BonusesItemIos] = [BonusesItemIos(date: "44488555", status: "popoln")]
    
    static let tmp : BonusesListAlertData = BonusesListAlertData(listBonuses: listBonuses, someFuncOk: { () -> Void in
        
    })
    
    static var previews: some View {
        BonusesListAlert(dataOb: tmp)
    }
}
