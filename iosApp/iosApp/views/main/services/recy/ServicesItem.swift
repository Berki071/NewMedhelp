//
//  ServicesItem.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ServicesItem: View {
    var item : ServiceResponseIos
    
    var body: some View {
        ZStack{
            HStack{
                HStack{
                    Text(item.title!)
                    Spacer()
                }
                
                Text(String(describing: NSNumber.init(value: item.value)) + "р")
                    .foregroundColor(Color("textItemBlue"))
            }
            .padding(.all, 12.0)
            
        }
        .overlay(
            RoundedRectangle(cornerRadius: 0)
                .stroke(Color("black_bg25"), lineWidth: 1)
        )
    }
    
}

struct ServicesItem_Previews: PreviewProvider {
    static var previews: some View {
        let tmp = ServiceResponseIos(title: "Recardo", value: 100)
        
        ServicesItem(item: tmp)
    }
}
