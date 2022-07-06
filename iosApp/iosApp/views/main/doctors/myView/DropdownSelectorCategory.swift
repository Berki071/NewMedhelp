//
//  DropdownSelector.swift
//  iosApp
//
//  Created by Михаил Хари on 22.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DropdownSelectorCategory: View {
    @State private var shouldShowDropdown = false
    @State private var selectedOption: CategoryResponse? = nil
    var placeholder: String
    var options: [CategoryResponse]
    var onOptionSelected: ((_ option: CategoryResponse) -> Void)?
    private let buttonHeight: CGFloat = 45
    
    var body: some View {
        Button(action: {
                   self.shouldShowDropdown.toggle()
               }) {
                   HStack {
                       Text(selectedOption == nil ? placeholder : selectedOption!.title!)
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
                           DropdownCategory(options: self.options, onOptionSelected: { option in
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

struct DropdownSelector_Previews: PreviewProvider {
    static var previews: some View {
        let hint = "hint my"
        
        let cat1 = CategoryResponse(title: "cat1")
        let cat2 = CategoryResponse(title: "cat2")
        let cat3 = CategoryResponse(title: "cat3")
        let arr = [cat1,cat2,cat3]
        
        DropdownSelectorCategory(placeholder : hint, options : arr)
    }
}
