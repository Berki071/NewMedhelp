//
//  AnaliseItem.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct AnaliseItem: View {
    var item : AnalisePriceResponseIos
    
    var body: some View {
        ZStack{
            HStack{
                VStack{
                    HStack{
                        Text(item.title!)
                            .font(.callout)
                            .fontWeight(.bold)
                        Spacer()
                    }
                    HStack{
                        Text("Срок выполнения (дней): " + String(describing: NSNumber.init(value: item.time)))
                            .font(.footnote)
                        Spacer()
                    }
                }
                
                Text(String(describing: NSNumber.init(value: item.price)) + "р")
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

struct AnaliseItem_Previews: PreviewProvider {
    static var previews: some View {
        let tmp = AnalisePriceResponseIos(title: "anal", price: 100, time: 5)
        
        AnaliseItem(item: tmp)
    }
}
