//
//  Dropdown.swift
//  iosApp
//
//  Created by Михаил Хари on 22.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DropdownCategory: View {
    var options: [CategoryResponse]
    var onOptionSelected: ((_ option: CategoryResponse) -> Void)?

    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                ForEach(self.options, id: \.self) { option in
                    DropdownRowCategory(option: option, onOptionSelected: self.onOptionSelected)
                        
                }
            }
        }
        .frame(height: getHeightScroll())
        .padding(.vertical, 5)
        .background(Color("lightGray"))
        .cornerRadius(0)
        .overlay(
            RoundedRectangle(cornerRadius: 0)
                .stroke(Color.gray, lineWidth: 1)
        )
    }
    
    func getHeightScroll() -> CGFloat {
        let maxH = UIScreen.main.bounds.size.height - 140
        let maxByCount =  CGFloat(28 * options.count)
        
        if(maxByCount > maxH){
            return maxH
        }else{
            return maxByCount
        }
    }
}

struct Dropdown_Previews: PreviewProvider {
    static var previews: some View {
        let cat1 = CategoryResponse(title: "cat1")
        let cat2 = CategoryResponse(title: "cat2")
        let cat3 = CategoryResponse(title: "cat3")
        let cat4 = CategoryResponse(title: "cat4")
        let cat5 = CategoryResponse(title: "cat5")
        let arr = [cat1,cat2,cat3,cat4,cat5]
        
        DropdownCategory(options : arr)
    }
}
