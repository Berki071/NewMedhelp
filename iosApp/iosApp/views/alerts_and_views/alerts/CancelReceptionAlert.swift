//
//  CancelReceptionAlert.swift
//  iosApp
//
//  Created by Михаил Хари on 13.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CancelReceptionAlert: View {
    let dataOb: ShowDialogCancelReceptionData
  
    let arrayCanselReason = ["Не нуждаюсь в приеме","Неправильная запись (Не записывался)","Заболел","Уехал из города","Перезапишусь сам позже","Выздоровел"]
    
    @State var selectedOption = ""
    @State var showErrorSelect = false
    
    var body: some View {
        ZStack{
            Color("black_bg")
            
            ZStack{
                VStack{
                    Text("Подтверждение отмены")
                        .font(.headline)
                        .multilineTextAlignment(.center)
                        .padding(.top)
                    
                    Spacer()
                        .frame(height: 70.0)
                    
                    if(showErrorSelect){
                    HStack{
                        Text("Необходимо указать причину")
                            .font(.body)
                            .foregroundColor(Color.red)
                            .multilineTextAlignment(.center)
                            .padding(.top)
                        Spacer()
                    }
                    .padding(.leading)
                    }
                    
                    HStack{
                       
                        Button(action: {
                            self.dataOb.someFuncCancel()
                        }) {
                            Text("Нет")
                                .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                .foregroundColor(Color.white)
                                .background(Color("color_primary"))
                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        }
                        
                        Button(action: {
                            if(selectedOption.isEmpty){
                                self.showErrorSelect = true
                            }else{
                                self.dataOb.someFuncOk(selectedOption)
                            }
                        }) {
                            Text("Да")
                                .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                .foregroundColor(Color.white)
                                .background(Color("color_primary"))
                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        }
                        //.padding()
                    }
                    .padding([.leading, .bottom, .trailing])
                }
            }
            //.frame(height: 200.0)
            .background(Color(.white))
            .cornerRadius(6)
            .padding([.leading, .trailing], 16.0)
            .padding(.top, 20.0)
            
            DropdownSelectorStringCanselation(placeholder: "Выберите причину отмены из списка", options: arrayCanselReason, onOptionSelected : { option in
                self.selectedOption = option
            })
                .padding(.horizontal, 30)
            
        }
        .ignoresSafeArea()
    }
}

struct CancelReceptionAlert_Previews: PreviewProvider {
    static var item = VisitResponseIos()
    static let dataOb =  ShowDialogCancelReceptionData(item: item, someFuncOk: {(String) -> Void in }, someFuncCancel: {() -> Void in })
    
    static var previews: some View {
        CancelReceptionAlert(dataOb: dataOb)
    }
}
