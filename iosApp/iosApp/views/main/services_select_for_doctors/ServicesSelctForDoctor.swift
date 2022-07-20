//
//  ServicesSelctForDoctor.swift
//  iosApp
//
//  Created by Михаил Хари on 18.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ServicesSelctForDoctor: View {
    @StateObject var mainPresenter : ServicesSelctForDoctorPresenter
    @State var goToMainView = false
    @State var goToRecord: ServiceResponseIos? = nil
    
    
    
    init(selectDocttorForRecord: AllDoctorsResponseIos){
        _mainPresenter = StateObject(wrappedValue: ServicesSelctForDoctorPresenter(selectDocttorForRecord: selectDocttorForRecord))
    }
    
    var body: some View {
        if(goToMainView){
            MainUIView(startPage: 1)
        }else if(goToRecord != nil){
            RecodPage(selectServis: goToRecord, selctDoctor: self.mainPresenter.selectDocttorForRecord, backPage: "ServicesSelctForDoctor")
        }else{
            
            ZStack{
                
                VStack{
                    if(self.mainPresenter.showDialogErrorScreen == false){
                        
                        if self.mainPresenter.showEmptyScreen{
                            Text("Услуги отсутствуют")
                        }else{
                            
                            List(mainPresenter.serviceListForRecy) {
                                ServicesItem(item: $0, clickITem: {(i : ServiceResponseIos) -> Void in
                                    goToRecord = i
                                })
                                    .listRowInsets(EdgeInsets(top: 0, leading: -1, bottom: 0, trailing: 0))
                                    .listRowSeparator(.hidden)
                            }
                            .listStyle(PlainListStyle())
                        }
                    }
                }
                .padding(.top, 89.0)
                .frame(maxWidth: .infinity)
                .background(.white)
                
                VStack(spacing: 0){
                    MyToolBar(title1: "Прейскурант на услуги", isShowSearchBtn: true, clickHumburger: {() -> Void in   //44.0
                        //self.clickButterMenu?() MainUIView
                        self.goToMainView = true
                    } , strSerch: self.$mainPresenter.textSearch, isShowImageFreeLine: false)
                    
                    DropdownSelectorCategory(placeholder: "Все", options: mainPresenter.categoryList, onOptionSelected : { option in
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
                
                if(self.mainPresenter.isShowStandartAlert != nil){
                    StandartAlert(dataOb: mainPresenter.isShowStandartAlert!)
                }
                
            }
        }
    }
}

struct ServicesSelctForDoctor_Previews: PreviewProvider {
    static private var tmp = AllDoctorsResponseIos(fio_doctor: "fio Doc", titleSpec: "doc Spec")
    
    static var previews: some View {
        ServicesSelctForDoctor(selectDocttorForRecord: tmp)
    }
}
