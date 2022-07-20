//
//  DoctorsItem.swift
//  iosApp
//
//  Created by Михаил Хари on 22.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DoctorsItem: View {
    var item : AllDoctorsResponseIos
    @ObservedObject var mainPresenter : DoctorsItemPresenter
    @Binding var doctorInfoAlertData : DoctorInfoAlertData?
    @Binding var selectDocttorForRecord: AllDoctorsResponseIos?
    
    init(item : AllDoctorsResponseIos,  doctorInfoAlertData : Binding<DoctorInfoAlertData?>, selectDocttorForRecord: Binding<AllDoctorsResponseIos?> ){
        self.item = item
        self._doctorInfoAlertData = doctorInfoAlertData
        self._selectDocttorForRecord = selectDocttorForRecord
        mainPresenter = DoctorsItemPresenter(item: item)
    }
    
    var body: some View {
        ZStack{
            HStack(spacing: 0){
                
                Image(uiImage: self.mainPresenter.iuImageLogo)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .padding(8.0)
                    .frame(width: 56.0, height: 56.0)

                VStack{
                    HStack{
                        Text(item.fio_doctor!)
                            .fontWeight(.bold)
                        Spacer()
                    }
                    
                    HStack{
                        Text(item.name_specialties!)
                        Spacer()
                    }
                }
                .frame(maxWidth: .infinity)
                
            }
            
        }
        .overlay(
            RoundedRectangle(cornerRadius: 0)
                .stroke(Color("black_bg25"), lineWidth: 1)
        )
        .contentShape(Rectangle())
        .onTapGesture {
            let tmp = DoctorInfoAlertData(infoDoctor: item, someFuncCancel: {() -> Void in
                self.doctorInfoAlertData = nil
            }, selectDocttorForRecord: $selectDocttorForRecord)
       
            self.doctorInfoAlertData = tmp
        }
    }
}

struct DoctorsItem_Previews: PreviewProvider {
    static let tmp = AllDoctorsResponseIos(fio_doctor: "Recardo", titleSpec: "vain doctor")
    @State static private var tmp1: DoctorInfoAlertData? = nil
    @State static private var tmp2: AllDoctorsResponseIos? = nil
    
    static var previews: some View {
        
        DoctorsItem(item: tmp, doctorInfoAlertData: $tmp1, selectDocttorForRecord: $tmp2)
    }
}
