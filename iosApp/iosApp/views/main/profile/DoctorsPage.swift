//
//  DoctorsPage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DoctorsPage: View {
    @ObservedObject var mainPresenter = DoctorsPresenter()

    
    var body: some View {
    
        ZStack{
                
            VStack{
                List(mainPresenter.doctorListForRecy) {
                    DoctorsItem(item: $0)
                        .listRowInsets(EdgeInsets(top: 0, leading: -1, bottom: 0, trailing: 0))
                        .listRowSeparator(.hidden)
                }
                .listStyle(PlainListStyle())
            }
            .padding(.top, 45.0)
            .frame(maxWidth: .infinity)
            .background(.white)

            VStack{
                DropdownSelector(placeholder: "Все", options: mainPresenter.categoryList, onOptionSelected : { option in
                    mainPresenter.selctSpinnerItem(option)
                })
                Spacer()
            }
           

            
            
            if(self.mainPresenter.showDialogLoading == true){
                LoadingView()
            }
            
            if(self.mainPresenter.isShowAlertRecomend != nil){
                StandartAlert(dataOb: mainPresenter.isShowAlertRecomend!)
            }

        }
    }
}

struct DoctorsPage_Previews: PreviewProvider {
    static var previews: some View {
        
        DoctorsPage()
    }
}
