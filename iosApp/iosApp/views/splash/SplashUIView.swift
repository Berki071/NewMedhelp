//
//  SplashUIView.swift
//  iosApp
//
//  Created by Михаил Хари on 26.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct SplashUIView: View {
    
    @ObservedObject var splashPresenter = SplashPresenter()
    
    var body: some View {
        VStack {
            if(self.splashPresenter.nextPage != ""){
                if(self.splashPresenter.nextPage == "Login"){
                    LoginUiView()
                }else{
                    MainUIView()
                }
            }else{
        
                Image("splash")
                    .resizable()
                    .ignoresSafeArea()
                
            }
        }
    }
}

struct SplashUIView_Previews: PreviewProvider {
    
    static var previews: some View {
        SplashUIView()
    }
}
