//
//  DropdownSelectorStringCanselation.swift
//  iosApp
//
//  Created by Михаил Хари on 14.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DropdownSelectorStringCanselation: View {
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
                           .foregroundColor(Color.black)

                       Spacer()

                       Image(systemName: self.shouldShowDropdown ? "arrowtriangle.up.fill" : "arrowtriangle.down.fill")
                           .resizable()
                           .frame(width: 9, height: 5)
                           .font(Font.system(size: 9, weight: .medium))
                           .foregroundColor(Color("color_primary"))
                   }
               }
               .padding(.horizontal)
               .frame(width: .infinity, height: self.buttonHeight)
               .overlay(
                   RoundedRectangle(cornerRadius: 8)
                       .stroke(Color("black_bg3"), lineWidth: 2)
               )
               //.background(Color("color_primary"))
               .overlay(
                   VStack {
                       if self.shouldShowDropdown  {
                           Spacer(minLength: buttonHeight )  //+10
                           DropdownStringCanseletion(options: self.options, onOptionSelected: { option in
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

struct DropdownSelectorStringCanselation_Previews: PreviewProvider {
    static var previews: some View {
        let hint = "hint my"
        
        let cat1 = "cat1"
        let cat2 = "cat2"
        let cat3 = "cat3"
        let arr = [cat1,cat2,cat3]
        
        DropdownSelectorStringCanselation(placeholder : hint, options : arr)
    }
}
