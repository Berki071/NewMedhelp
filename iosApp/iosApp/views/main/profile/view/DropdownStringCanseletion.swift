//
//  DropdownStringCanseletion.swift
//  iosApp
//
//  Created by Михаил Хари on 14.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DropdownStringCanseletion: View {
    var options: [String]
    var onOptionSelected: ((_ option: String) -> Void)?

    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                ForEach(self.options, id: \.self) { option in
                    DropdownRowStringCn(option: option, onOptionSelected: self.onOptionSelected)
                }
            }
        }
        .frame(height: CGFloat(28 * options.count))
        .padding(.vertical, 5)
        .background(Color("lightGray"))
        .cornerRadius(0)
        .overlay(
            RoundedRectangle(cornerRadius: 0)
                .stroke(Color.gray, lineWidth: 1)
        )
    }
}

struct DropdownStringCanseletion_Previews: PreviewProvider {
    static var previews: some View {
        let cat1 = "cat1"
        let cat2 = "cat2"
        let cat3 = "cat3"
        let cat4 = "cat4"
        let cat5 = "cat5"
        let arr = [cat1,cat2,cat3,cat4,cat5]
        
        DropdownStringCanseletion(options: arr)
    }
}
