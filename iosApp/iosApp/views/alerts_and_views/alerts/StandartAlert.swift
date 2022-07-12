//
//  StandartAlert.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct StandartAlertData{
    let titel : String
    let text: String
    let isShowCansel : Bool
    let someFuncOk: () -> Void
    let someFuncCancel: () -> Void
}

struct StandartAlert: View {
    var dataOb: StandartAlertData
    
    var body: some View {
        ZStack{
           Color("black_bg")
           
           ZStack{
               Color(.white)
                   .cornerRadius(5)
                   .overlay(
                       RoundedRectangle(cornerRadius: 5)
                           .stroke(Color("black_bg2"), lineWidth: 2)
                   )
               
               VStack{
                       Text(dataOb.titel)
                       .font(.headline)
                       .multilineTextAlignment(.center)
                       
                       HStack{
                           Image("new_releases_symbol")
                               .foregroundColor(Color("color_primary"))
                           Text(dataOb.text)
                               .font(.subheadline)
                               .multilineTextAlignment(.center)
                       }
                       .padding(/*@START_MENU_TOKEN@*/.all/*@END_MENU_TOKEN@*/)
                       
                   HStack{
                       if(dataOb.isShowCansel){
                           Button(action: {
                               dataOb.someFuncCancel()
                           }) {
                               Text("Закрыть")
                                 .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                 .foregroundColor(Color.white)
                                 .background(Color("color_primary"))
                                 .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                           }
                       }
                       Button(action: {
                           dataOb.someFuncOk()
                       }) {
                           Text("OK")
                             .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                             .foregroundColor(Color.white)
                             .background(Color("color_primary"))
                             .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                       }
                       //.padding()
                   }
                   .padding(.horizontal, 16.0)
               }
           }
           .padding(.horizontal, 16.0)
           //.frame(height: 200.0)
           
       }
       .ignoresSafeArea()
    }
}

struct StandartAlert_Previews: PreviewProvider {
    static let dataOb =  StandartAlertData(titel: "title", text: "text", isShowCansel: true,
                                           someFuncOk: {() -> Void in }, someFuncCancel: {() -> Void in })
    
    static var previews: some View {
        StandartAlert(dataOb: dataOb)
    }
}
