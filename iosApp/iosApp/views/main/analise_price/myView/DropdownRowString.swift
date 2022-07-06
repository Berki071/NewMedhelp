//
//  DropdownRowString.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DropdownRowString: View {
    var option: String
    var onOptionSelected: ((_ option: String) -> Void)?
    
    var body: some View {
        Button(action: {
            if let onOptionSelected = self.onOptionSelected {
                onOptionSelected(self.option)
            }
        }) {
            HStack {
                Text(option)
                    .font(.system(size: 14))
                    .foregroundColor(Color.black)
                Spacer()
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 5)
    }
}

struct DropdownRowString_Previews: PreviewProvider {
    static var previews: some View {
        let tmpCat = "testTitle"
        DropdownRowString(option: tmpCat)
    }
}
