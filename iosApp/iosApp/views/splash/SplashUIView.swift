//
//  SplashUIView.swift
//  iosApp
//
//  Created by Михаил Хари on 26.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct AlertAttention: Identifiable {
    var id: String { text }
    var titel: String = "Внимание!"
    let text: String
}

struct SplashUIView: View {
    @ObservedObject var splashPresenter = SplashPresenter()
    
    
    var body: some View {
        
        if(self.splashPresenter.nextPage != ""){
            if(self.splashPresenter.nextPage == "Login"){
                //self.splashPresenter.netConnection.stopMonitoring()
                LoginUiView()
            }else{
                
                MainUIView()
            }
        }else{
            
            VStack {
                Image("splash")
                    .resizable()
                    .ignoresSafeArea()
            }
            .alert(item : $splashPresenter.selectedShow){ show in
                Alert(title: Text(show.titel), message: Text(show.text), dismissButton: .cancel())
            }
        }
    }
    
}

struct SplashUIView_Previews: PreviewProvider {
    
    static var previews: some View {
        SplashUIView()
    }
}
