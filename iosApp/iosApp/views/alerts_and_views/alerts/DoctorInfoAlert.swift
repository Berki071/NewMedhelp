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
    let someFuncOk: () -> Void
}

struct DoctorInfoAlert: View {
    var dataOb : DoctorInfoAlertData
    @State var iuImageLogo : UIImage =  UIImage(named: "sh_doc")!
    
    init(dataOb : DoctorInfoAlertData){
        self.dataOb = dataOb
        
        initImage()
    }
    
    var body: some View {
        
        ZStack{
            Color("black_bg")
            
            ZStack{
                VStack(alignment: .leading){
                    HStack {
                        Spacer()
                    }
                    
                    HStack{
                        Spacer()
                        Image(uiImage: self.iuImageLogo)
                            .frame(width: 150.0, height: 150.0)
                        Spacer()
                    }
                    
                    
                    HStack{
                        Text("ФИО: ")
                        Text(dataOb.infoDoctor.fio_doctor ?? "")
                    }
                    
                    HStack{
                        Text("Стаж: ")
                        Text(dataOb.infoDoctor.experience ?? "")
                    }
                    
                    HStack{
                        Text("Специальность: ")
                        Text(dataOb.infoDoctor.name_specialties ?? "")
                    }
                    
                    HStack{
                        Text("Дополнительно: ")
                        Text(dataOb.infoDoctor.dop_info ?? "")
                    }
                    
                    HStack{
                        Button(action: {
                            //dataOb.someFuncOk()
                        }) {
                            Text("OK")
                                .frame(minWidth: 100, maxWidth: .infinity, minHeight: 44, maxHeight: 44, alignment: .center)
                                .foregroundColor(Color.white)
                                .background(Color("color_primary"))
                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        }
                        //.padding()
                    }
                    
                }
                .padding(16.0)
            }
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color("black_bg3"), lineWidth: 3)
            )
            .padding(.horizontal, 8.0)
        }
    }
    
    func initImage() {
        let sharePreferenses = SharedPreferenses()
        
        let currentUserInfo = sharePreferenses.currentUserInfo
        
        if(currentUserInfo != nil && dataOb.infoDoctor.image_url != nil){
            let imagePathString = dataOb.infoDoctor.image_url! + "&token=" + currentUserInfo!.apiKey!
            
            DownloadManager(imagePathString,  resultUiImage: { (tmp : UIImage) -> Void in

                self.iuImageLogo = tmp
            })
        }
    }
}

struct DoctorInfoAlert_Previews: PreviewProvider {
    let someFuncCancel = {() -> Void in }
    let item = AllDoctorsResponseIos(fio_doctor: "fio_doctor", titleSpec: "titleSpec")
    
    let tmp = DoctorInfoAlertData(infoDoctor : item, someFuncOk : someFuncCancel)
    
    static var previews: some View {
        DoctorInfoAlert(dataOb: tmp)
    }
}
