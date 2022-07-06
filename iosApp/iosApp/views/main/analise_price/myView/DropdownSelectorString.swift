//
//  DropdownSelectorString.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DropdownSelectorString: View {
    
    @State private var shouldShowDropdown = false
    @State private var selectedOption: String? = nil
    var placeholder: String
    var options: [String]
    var onOptionSelected: ((_ option: String) -> Void)?
    private let buttonHeight: CGFloat = 45
    
    var body: some View {
        Button(action: {
                   self.shouldShowDropdown.toggle()
               }) {
                   HStack {
                       Text(selectedOption == nil ? placeholder : selectedOption!)
                           .font(.system(size: 14))
                           .foregroundColor(Color.white)

                       Spacer()

                       Image(systemName: self.shouldShowDropdown ? "arrowtriangle.up.fill" : "arrowtriangle.down.fill")
                           .resizable()
                           .frame(width: 9, height: 5)
                           .font(Font.system(size: 9, weight: .medium))
                           .foregroundColor(Color.white)
                   }
               }
               .padding(.horizontal)
               .frame(width: .infinity, height: self.buttonHeight)
               .overlay(
                   RoundedRectangle(cornerRadius: 0)
                       .stroke(Color.white, lineWidth: 1)
               )
               .background(Color("color_primary"))
               .overlay(
                   VStack {
                       if self.shouldShowDropdown  {
                           Spacer(minLength: buttonHeight )  //+10
                           DropdownString(options: self.options, onOptionSelected: { option in
                               shouldShowDropdown = false
                               selectedOption = option
                               self.onOptionSelected?(option)
                           })
                       }
                   }, alignment: .topLeading
               )
               .background(
                   RoundedRectangle(cornerRadius: 5).fill(Color.white)
               )
    }
}

struct DropdownSelectorString_Previews: PreviewProvider {
    static var previews: some View {
        let hint = "hint my"
        
        let cat1 = "cat1"
        let cat2 = "cat2"
        let cat3 = "cat3"
        let arr = [cat1,cat2,cat3]
        
        DropdownSelectorString(placeholder : hint, options : arr)
    }
}
