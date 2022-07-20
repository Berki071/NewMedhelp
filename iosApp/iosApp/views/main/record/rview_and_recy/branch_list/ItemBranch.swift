//
//  ItemBranch.swift
//  iosApp
//
//  Created by Михаил Хари on 19.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ItemBranch: View {
    var item : BranchLineItem
    var clickItem : (SettingsAllBranchHospitalResponse) -> Void
    
    init(item : BranchLineItem, clickItem : @escaping (SettingsAllBranchHospitalResponse) -> Void){
        self.item = item
        self.clickItem = clickItem
    }
    
    var body: some View {
        HStack{
            Spacer()
                .frame(width: 8.0)
            HStack{
                Spacer()
                Text(item.item1.nameBranch!)
                    .padding(.vertical, 7.0)
                Spacer()
            }
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color("black_bg25"), lineWidth: 1)
            )
            .background(item.item1.isFavorite ? Color("textSideMenu10") : Color("transparent"))
            .contentShape(Rectangle())
            .onTapGesture {
                clickItem(item.item1)
            }
            
            Spacer()
                .frame(width: 8.0)
            
            if(item.item2 != nil) {
                HStack{
                    Spacer()
                    Text(item.item2!.nameBranch!)
                        .padding(.vertical, 7.0)
                    Spacer()
                }
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(Color("black_bg25"), lineWidth: 1)
                )
                .background(item.item2!.isFavorite ? Color("textSideMenu10") : Color("transparent"))
                .contentShape(Rectangle())
                .onTapGesture {
                    clickItem(item.item2!)
                }
            }else{
                HStack{
                    Spacer()
                    Text(" ")
                    Spacer()
                }
            }
            
            Spacer()
                .frame(width: 8.0)
        }
        .frame(height: 40.0)
    }
}

struct ItemBranch_Previews: PreviewProvider {
    
   // let tt1 = SettingsAllBranchHospitalResponse()
    //tt1.nameBranch = "Name1"
    //let tt2 = SettingsAllBranchHospitalResponse()
    //tt2.nameBranch = "Name2"
    //let tttb = BranchLineItem(item1: SettingsAllBranchHospitalResponse(), item2: SettingsAllBranchHospitalResponse())

    static var previews: some View {
    
        ItemBranch(item: BranchLineItem(item1: SettingsAllBranchHospitalResponse(), item2: SettingsAllBranchHospitalResponse()), clickItem: {(SettingsAllBranchHospitalResponse) -> Void in})
    }
}
