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

struct LoginUiView: View {
    @ObservedObject var loginPresenter = LoginPresenter()
    
    var body: some View {
        if self.loginPresenter.nextPage == "Main" {
            MainUIView()
        }else{
            NavigationView {
                ZStack {
                    VStack {
                        Page(loginP: loginPresenter)
                    }
                    .padding()
                    
                    if (self.loginPresenter.alertLogind != nil){
                        //ResorePassword(alertLogind: loginPresenter.alertLogind!)
                        StandartAlert(dataOb: loginPresenter.alertLogind!)
                    }
                    
                    if(self.loginPresenter.showDialogSupportMSg){
                        SentToSupportAlert(loginP: loginPresenter)
                    }
                    
                    if(self.loginPresenter.showDialog == "loading"){
                        LoadingView()
                    }
                    
                    
                }
                .alert(item : $loginPresenter.valueShowAlert){ show in
                    Alert(title: Text(show.titel), message: Text(show.text), dismissButton: .cancel())
                }
                
            }
        }
    }
}


struct SentToSupportAlert : View{
    @ObservedObject var loginP: LoginPresenter
    
    @State private var email = ""
    @State private var msg = ""
    
    var body: some View{
        return ZStack{
            Color("black_bg")
            
            ZStack{
                Color(.white)
                    .cornerRadius(5)
                    .overlay(
                        RoundedRectangle(cornerRadius: 5)
                            .stroke(Color("black_bg2"), lineWidth: 2)
                    )
                
                VStack{
                    
                    ZStack{
                        Color("color_primary")
                            .cornerRadius(5)
                        Text("Техническая поддержка")
                            .font(.title3)
                            .foregroundColor(Color.white)
                    }
                    .frame(height: 50.0)
                    
                    
                    
                    VStack {
                        FormatStartTextField(
                            unformattedText: $loginP.username,
                            formatter: PlaceholderTextInputFormatter(textPattern: "+7(***) *** ** **", patternSymbol: "*")
                        )
                            .padding()
                            .padding(.horizontal, 2.0)
                            .background(lightGreyColor)
                            .cornerRadius(5.0)
                            .keyboardType(.numberPad)
                        
                        TextField("Email для ответа", text: $email)
                            .padding(.horizontal, 18.0)
                            .frame(height: 40.0)
                            .overlay(
                                RoundedRectangle(cornerRadius: 5)
                                    .stroke(Color("color_primary"), lineWidth: 2)
                            )
                        
                        
                        TextEditor(text: $msg)
                            .foregroundColor(Color.blue)
                            .padding()
                            .frame(height: 110.0)
                            .overlay(
                                RoundedRectangle(cornerRadius: 5)
                                    .stroke(Color("color_primary"), lineWidth: 2)
                            )
                            .overlay(
                                VStack(alignment: .leading){
                                    HStack {
                                        if msg.isEmpty {
                                            Text("Сообщение")
                                                .font(.title3)
                                                .fontWeight(.light)
                                                .foregroundColor(Color(red: 0.769, green: 0.769, blue: 0.777))
                                                .padding(.horizontal, 18.0)
                                            
                                            Spacer()
                                        }
                                        
                                    }
                                    
                                })
                        
                    }
                    .padding(.horizontal, 16.0)
                    .padding(.vertical, 8.0)
                    
                    
                    Spacer()
                    
                    HStack{
                        Button("Закрыть") {
                            self.loginP.showMsgSupporAlert(false)
                        }
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color("color_primary"))
                        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        Button("Отправить") {
                            self.loginP.sendMsgToSupport(email, msg)
                            self.loginP.showMsgSupporAlert(false)
                        }
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color("color_primary"))
                        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                        
                    }
                    .padding(.horizontal, 16.0)
                    .padding(.bottom, 16.0)
                }
            }
            .padding(.horizontal, 16.0)
            .frame(height: 400.0)
            
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
                    self.loginP.showAlertRestorePas()
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                .padding(.trailing, 16.0)
            }
            .padding(.top, 16.0)
            
            
            Spacer()
            
            VStack {
                Button("Как зарегистрироваться?") {
                    loginP.showAlert("Для регистрации в мобильном приложении обратитесь в медицинский центр и Вам вышлют пароль", titleM: "Хотите зарегистрироваться?")
                }
                .foregroundColor(Color(red: 0.082, green: 0.396, blue: 0.753))
                
                Button("Написать в техподдержку") {
                    loginP.showMsgSupporAlert(true)
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

