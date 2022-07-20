//
//  DoctorInfoAlert.swift
//  iosApp
//
//  Created by Михаил Хари on 11.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DoctorInfoAlertData{
    let infoDoctor : AllDoctorsResponseIos
    let someFuncCancel: () -> Void
    var selectDocttorForRecord: Binding<AllDoctorsResponseIos?>
}

struct DoctorInfoAlert: View {
    var dataOb : DoctorInfoAlertData
    @State var iuImageLogo : UIImage
    
    init(dataOb : DoctorInfoAlertData){
        self.dataOb = dataOb
        self.iuImageLogo = dataOb.infoDoctor.iuImageLogo ?? UIImage(named: "sh_doc")!
    }
    
    var body: some View {
        
        ZStack{
            Color("black_bg")
            
            ZStack{
                VStack(alignment: .leading, spacing: 0){
                    HStack {
                        Spacer()
                    }
                    
                    HStack{
                        Spacer()
                        Image(uiImage: self.iuImageLogo)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 150.0, height: 150.0)
                        Spacer()
                    }
                    .padding(.bottom, 12.0)
                    
                    
                    HStack{
                        Text("ФИО: ")
                            .fontWeight(.bold)
                        Text(dataOb.infoDoctor.fio_doctor ?? "")
                    }
                    .padding(.bottom, 4.0)
                    
                    HStack{
                        Text("Стаж: ")
                            .fontWeight(.bold)
                        Text(dataOb.infoDoctor.experience ?? "")
                    }
                    .padding(.bottom, 4.0)
                    
                    HStack{
                        Text("Специальность: ")
                            .fontWeight(.bold)
                        Text(dataOb.infoDoctor.name_specialties ?? "")
                    }
                    .padding(.bottom, 4.0)
                    
                    VStack(spacing: 0){
                        HStack {
                            Spacer()
                        }
                        
                        HStack{
                            Text("Дополнительно: ")
                                .fontWeight(.bold)
                            Spacer()
                        }
                        if(dataOb.infoDoctor.dop_info != nil && !dataOb.infoDoctor.dop_info!.isEmpty){
                            ScrollView{
                                Text(dataOb.infoDoctor.dop_info ?? "")
                                
                            }
                            .frame(maxHeight: 200)
                        }
                    
                    }
                    .padding(.bottom, 16.0)
                    
                    HStack{
                        Button(action: {
                            dataOb.someFuncCancel()
                        }) {
                            Text("Закрыть")
                                .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                .foregroundColor(Color.white)
                                .background(Color("color_primary"))
                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        }
                        
                        Button(action: {
                            dataOb.selectDocttorForRecord.wrappedValue = dataOb.infoDoctor
                        }) {
                            Text("Записаться")
                                .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                .foregroundColor(Color.white)
                                .background(Color("color_primary"))
                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        }
                        
                    }
                    
                }
                .padding(16.0)
            }
            .background(Color(.white))
            .cornerRadius(6)
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color("black_bg3"), lineWidth: 3)
            )
            .padding(.horizontal, 8.0)
            
        }
        .ignoresSafeArea()
    }
}

struct DoctorInfoAlert_Previews: PreviewProvider {
    @State static private var tmp2: AllDoctorsResponseIos? = nil
    static let tmp = DoctorInfoAlertData(infoDoctor: AllDoctorsResponseIos(fio_doctor: "fio_doctor", titleSpec: "titleSpec"),
                                         someFuncCancel: {() -> Void in }, selectDocttorForRecord: $tmp2)
    
    static var previews: some View {
        DoctorInfoAlert(dataOb: tmp)
    }
}
