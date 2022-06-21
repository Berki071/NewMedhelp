//
//  DoctorsPage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct DoctorsPage: View {
    var body: some View {
        ZStack{
            VStack(spacing: 0){
                
                HStack {
                    
                    
                    Spacer()
                }
                .frame(maxWidth: .infinity)
                .background(Color("color_primary"))
                
            }
            .background(.white)
            
//            
//            if(self.mainPresenter.showDialogLoading == true){
//                LoadingView()
//            }
//            
//            if(isShowAlertRecomend != nil){
//                StandartAlert(dataOb: isShowAlertRecomend!)
//            }

        }
    }
}

struct DoctorsPage_Previews: PreviewProvider {
    static var previews: some View {
        DoctorsPage()
    }
}
