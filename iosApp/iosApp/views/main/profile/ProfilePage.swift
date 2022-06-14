//
//  ProfilePage.swift
//  iosApp
//
//  Created by Михаил Хари on 10.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ProfilePage: View {
    @ObservedObject var mainPresenter = ProfilePresenter()
    
    
    var body: some View {
        ZStack{
            //Color("color_primary")

            
            VStack{
                HStack {
                    Image("ico_root")
                        .resizable(resizingMode: .stretch)
                        .frame(width: 100.0, height: 72.0)
                    VStack(alignment: .leading){
                        HStack{
                            Image("account_balance-account_balance_symbol")
                                .foregroundColor(.white)
                            Text(/*@START_MENU_TOKEN@*/"Placeholder"/*@END_MENU_TOKEN@*/)
                                .foregroundColor(Color.white)
                        }
                        HStack{
                            Image("call-call_symbol")
                                .foregroundColor(.white)
                            Text("Placeholder")
                                .foregroundColor(Color.white)
                        }
                        HStack{
                            Image("web-web_symbol")
                                .foregroundColor(.white)
                            Text("Placeholder")
                                .foregroundColor(Color.white)
                        }
                    }
                    .padding(.leading, 16.0)
                    
                    Spacer()
                    
                }
                .padding([.leading, .bottom, .trailing], 16.0)
                .frame(maxWidth: .infinity)
                .background(Color("color_primary"))
                
                
                List {
                    /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Content@*/Text("Content")/*@END_MENU_TOKEN@*/
                }
            }
            .background(.white)
            
            
            
        }
        //.edgesIgnoringSafeArea(.all)
        
    }
        
}

struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage()
    }
}
