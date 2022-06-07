//
//  LoginUiView.swift
//  iosApp
//
//  Created by Михаил Хари on 25.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import AnyFormatKit
import AnyFormatKitSwiftUI

let lightGreyColor = Color(red: 239.0/255.0, green: 243.0/255.0, blue: 244.0/255.0, opacity: 1.0)

struct AlertLogind{
    let titel : String
    let text: String
}

struct LoginUiView: View {
    @ObservedObject var loginPresenter = LoginPresenter()
    
    var body: some View {
        
        NavigationView {
            
            ZStack {
                VStack {
                    if self.loginPresenter.nextPage == "Main" {
                        MainUIView()
                    }else{
                        Page(loginP: loginPresenter)
                    }
                    
                }
                .padding()
                
               
                if(self.loginPresenter.showDialog == "loading"){
                    LoadingView()
                }
                if (self.loginPresenter.alertLogind != nil){
                    ResorePassword(loginP: loginPresenter)
                }
                
            }
            .alert(item : $loginPresenter.valueShowAlert){ show in
                Alert(title: Text("Внимание!"), message: Text(show.name), dismissButton: .cancel())
            }
            
        }
    }
}

struct ResorePassword : View {
    @ObservedObject var loginP: LoginPresenter
    
    var body : some View {
        return  ZStack{
            Color("black_bg")
            
            ZStack{
                Color(.white)
                    .cornerRadius(5)
                    .overlay(
                        RoundedRectangle(cornerRadius: 5)
                            .stroke(Color("black_bg2"), lineWidth: 2)
                    )
                
                VStack{
                    Text(loginP.alertLogind!.titel)
                        .font(.title)
                    HStack{
                        Image("new_releases_symbol")
                            .foregroundColor(Color("color_primary"))
                        Text(loginP.alertLogind!.text)
                            .font(.title2)
                    }
                    HStack{
                        Button("Закрыть") {
                            loginP.alertLogind = nil
                        }
                             .foregroundColor(.white)
                             .padding()
                             .frame(maxWidth: .infinity)
                             .background(Color("color_primary"))
                             .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        Button("OK") {
                            loginP.alertLogind = nil
                            loginP.restoreAlertClick()
                        }
                             .foregroundColor(.white)
                             .padding()
                             .frame(maxWidth: .infinity)
                             .background(Color("color_primary"))
                             .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))

                    }
                    .padding(.horizontal, 16.0)
                    //.fixedSize(horizontal: false, vertical: true)
                }
            }
            .padding(.horizontal, 16.0)
            .frame(height: 200.0)
            
        }
        .ignoresSafeArea()
    }
}

struct Page : View {
    @ObservedObject var loginP: LoginPresenter
    
    var body: some View{
        return VStack {
            Spacer()
                .frame(height: 60.0)
            Image("ico_root")
                .resizable(resizingMode: .stretch)
                .frame(width: 75.0, height: 75.0)
            
            Text("Введите учетные данные")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Color("color_primary"))
                .padding(.top, 16.0)
            
            FormatStartTextField(
                unformattedText: $loginP.username,
                formatter: PlaceholderTextInputFormatter(textPattern: "+7(***) *** ** **", patternSymbol: "*")
            )
                .padding()
                .padding(.horizontal, 16.0)
                .background(lightGreyColor)
                .cornerRadius(5.0)
                .keyboardType(.numberPad)
            
            
            SecureField("Пароль", text: $loginP.password)
                .padding()
                .padding(.horizontal, 16.0)
                .background(lightGreyColor)
                .cornerRadius(5.0)
            
            
            HStack {
                Toggle(isOn: $loginP.togleState) {}
                .labelsHidden()
                TextField("Запомнить пароль", text: .constant(""))
            }
            .padding(.top, 16.0)
            
            
            Button("Войти") {
                self.loginP.onLoginClick()
            }
            .padding(.all, 12)
            .foregroundColor(.white)
            .background(Color("color_primary"))
            .cornerRadius(5.0)
            
            
            HStack {
                Spacer()
                Button("Забыли пароль?") {
                    
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                .padding(.trailing, 16.0)
            }
            .padding(.top, 16.0)
            
            
            Spacer()
            
            VStack {
                Button("Как зарегистрироваться?") {
                    
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                
                Button("Написать в техподдержку") {
                    
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                .padding(.top, 16.0)
            }
        }
        
        
    }
}

struct LoadingView : View {
    var body: some View{
        return ZStack{
            Color("black_bg")
            ProgressView("Данные обновляются, подождите…")
                .progressViewStyle(CircularProgressViewStyle(tint: Color.white))
                .foregroundColor(.white)
        }
        .ignoresSafeArea()
    }
}

struct LoginUiView_Previews: PreviewProvider {
    static var previews: some View {
        LoginUiView()
    }
}

