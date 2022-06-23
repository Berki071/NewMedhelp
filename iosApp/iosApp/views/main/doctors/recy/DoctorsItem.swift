//
//  DoctorsItem.swift
//  iosApp
//
//  Created by Михаил Хари on 22.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DoctorsItem: View {
    var item : AllDoctorsResponseIos
    
    var body: some View {
        ZStack{
            HStack(spacing: 0){
                
                Image("sh_doc")
                    .resizable(resizingMode: .stretch)
                    .padding(8.0)
                    .frame(width: 56.0, height: 56.0)

                VStack{
                    HStack{
                        Text(item.fio_doctor!)
                            .fontWeight(.bold)
                        Spacer()
                    }
                    
                    HStack{
                        Text(item.name_specialties!)
                        Spacer()
                    }
                }
                .frame(maxWidth: .infinity)
                
            }
            
        }
        .overlay(
            RoundedRectangle(cornerRadius: 0)
                .stroke(Color("black_bg25"), lineWidth: 1)
        )
    }
}

struct DoctorsItem_Previews: PreviewProvider {
    static var previews: some View {
        let tmp = AllDoctorsResponseIos(fio_doctor: "Recardo", titleSpec: "vain doctor")
        
        DoctorsItem(item: tmp)
    }
}
