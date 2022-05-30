//
//  LoginUiView.swift
//  iosApp
//
//  Created by Михаил Хари on 25.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct LoginUiView: View {
    var body: some View {
        
        
        VStack {
            
            VStack {
                Spacer()
                    .frame(height: 60.0)
                Image("ico_root")
                    .resizable(resizingMode: .stretch)
                    .frame(width: 75.0, height: 75.0)
            }
            
            Text("Введите учетные данные")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Color("color_primary"))
                .padding(.top, 16.0)
            
            TextField("Логин пользователя", text: /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Value@*/.constant("")/*@END_MENU_TOKEN@*/)
                .padding(.horizontal, 16.0)
                
            TextField("Пароль", text: /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Value@*/.constant("")/*@END_MENU_TOKEN@*/)
                .padding(.horizontal, 16.0)
                .padding(.top, 16.0)
            
            
            HStack {
                Toggle(isOn: /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Is On@*/.constant(true)/*@END_MENU_TOKEN@*/) {}
                .labelsHidden()
                TextField("Запомнить пароль", text: /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Value@*/.constant("")/*@END_MENU_TOKEN@*/)
            }
            .padding(.horizontal, 16.0)
            .padding(.top, 16.0)
            
            
            Button("Войти") {
                /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Action@*/ /*@END_MENU_TOKEN@*/
            }
            .padding(.all, 12)
            .foregroundColor(.white)
            .background(Color("color_primary"))
            
            HStack {
                Spacer()
                Button("Забыли пароль?") {
                    /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Action@*/ /*@END_MENU_TOKEN@*/
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                .padding(.trailing, 16.0)
            }
            .padding(.top, 16.0)
            
            
            Spacer()
            
            Button("Как зарегистрироваться?") {
                /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Action@*/ /*@END_MENU_TOKEN@*/
            }
            .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
            Button("Написать в техподдержку") {
                /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Action@*/ /*@END_MENU_TOKEN@*/
            }
            .padding(.top, 16.0)
            .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
        }
        
        
    }
}

struct LoginUiView_Previews: PreviewProvider {
    static var previews: some View {
        LoginUiView()
    }
}
