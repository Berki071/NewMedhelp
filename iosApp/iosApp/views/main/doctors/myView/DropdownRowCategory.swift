//
//  DropdownRow.swift
//  iosApp
//
//  Created by Михаил Хари on 22.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DropdownRowCategory: View {
    var option: CategoryResponse
    var onOptionSelected: ((_ option: CategoryResponse) -> Void)?

    
    var body: some View {
        Button(action: {
            if let onOptionSelected = self.onOptionSelected {
                onOptionSelected(self.option)
            }
        }) {
            HStack {
                Text(option.title!)
                    .font(.system(size: 14))
                    .foregroundColor(Color.black)
                Spacer()
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 5)
    }
}

struct DropdownRow_Previews: PreviewProvider {
    static var previews: some View {
        let tmpCat = CategoryResponse(title: "testTitle")
        
        DropdownRowCategory(option : tmpCat)
    }
}
