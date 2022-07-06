//
//  AnalisePrices.swift
//  iosApp
//
//  Created by Михаил Хари on 23.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct AnalisePrices: View {
    @StateObject var mainPresenter = AnalisePresenter()
    
    
    var clickButterMenu: (() -> Void)?
    
    init(clickButterMenu: (() -> Void)?){
        self.clickButterMenu = clickButterMenu
    }
    
    
    var body: some View {
        
        ZStack{
            
            VStack{
                if(self.mainPresenter.showDialogErrorScreen == false){
                    List(mainPresenter.analisListForRecy) {
                        AnaliseItem(item: $0)
                            .listRowInsets(EdgeInsets(top: 0, leading: -1, bottom: 0, trailing: 0))
                            .listRowSeparator(.hidden)
                    }
                    .listStyle(PlainListStyle())
                }
            }
            .padding(.top, 89.0)
            .frame(maxWidth: .infinity)
            .background(.white)
            
            VStack(spacing: 0){
                MyToolBar(title1: "Прейскурант на анализы", isShowSearchBtn: true, clickHumburger: {() -> Void in   //44.0
                    self.clickButterMenu?()
                }, strSerch: self.$mainPresenter.textSearch)
                
                DropdownSelectorString(placeholder: "Все", options: mainPresenter.categoryList, onOptionSelected : { option in
                    self.mainPresenter.selectedOption = option
                    self.mainPresenter.filterList()
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
                        self.mainPresenter.getAnalisePrice()
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

struct AnalisePrices_Previews: PreviewProvider {
    static var previews: some View {
        AnalisePrices(clickButterMenu: nil)
    }
}
