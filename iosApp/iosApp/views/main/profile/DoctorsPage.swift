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
    @StateObject var mainPresenter = DoctorsPresenter()
    
    
    
    var clickButterMenu: (() -> Void)?
    
    init(clickButterMenu: (() -> Void)?){
        self.clickButterMenu = clickButterMenu
    }
    
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
            .padding(.top, 89.0)
            .frame(maxWidth: .infinity)
            .background(.white)
            
            VStack(spacing: 0){
                MyToolBar(title1: "Специалисты", isShowSearchBtn: true, clickHumburger: {() -> Void in   //44.0
                    self.clickButterMenu?()
                }, strSerch: self.$mainPresenter.textSearch)
                
                DropdownSelectorCategory(placeholder: "Все", options: mainPresenter.categoryList, onOptionSelected : { option in  //45
                    mainPresenter.selectedOption = option
                    mainPresenter.filterList()
                })
                Spacer()
            }

            if(self.mainPresenter.showDialogErrorScreen){
                VStack{
                    Text("Не удалось загрузить данные.\n Проверьте подключение \nк интернету и \nповторите попытку.")
                        .multilineTextAlignment(.center)
                    
                    Spacer()
                        .frame(height: 20.0)
                    
                    Button("Повторить") {
                        self.mainPresenter.showErrorScreen(false)
                        self.mainPresenter.getSpecialtyByCenter()
                    }
                    .padding(.all, 12)
                    .foregroundColor(.white)
                    .background(Color("color_primary"))
                    .cornerRadius(5.0)
                }
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
        
        DoctorsPage(clickButterMenu: nil)
    }
}
