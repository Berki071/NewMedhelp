//
//  MyToolBar.swift
//  iosApp
//
//  Created by Михаил Хари on 30.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MyToolBar: View {
    @State var title : String
    @State var isShowSearchBtn : Bool
    var clickHumburger : (() -> Void)?
    var textSearch: Binding<String>?
    
    @State private var isEditing = false
    @State var isShowSearchView = false
    var isShowHumburgerBtn : Bool = true
    
    var isShowImageFreeLine: Bool
   
 

    init(title1 : String, isShowSearchBtn : Bool, clickHumburger : @escaping () -> Void, strSerch : Binding<String>?, isShowImageFreeLine: Bool = true){
        self.title = title1
        self.isShowSearchBtn = isShowSearchBtn
        self.clickHumburger = clickHumburger
        self.textSearch = strSerch
        self.isShowImageFreeLine = isShowImageFreeLine

    }
    init(title1 : String, clickHumburger : @escaping () -> Void, isShowImageFreeLine: Bool){
        self.title = title1
        self.clickHumburger = clickHumburger
        self.isShowSearchBtn = false
        self.isShowImageFreeLine = isShowImageFreeLine
    }
    init(title1 : String){
        self.title = title1
        self.isShowSearchBtn = false
        self.isShowHumburgerBtn = false
        self.isShowImageFreeLine = true
    }
    
    
    var body: some View {
        ZStack{
            
            if(!isShowSearchView){
                
                HStack{
                    let tp = self.isShowHumburgerBtn
                    if (self.isShowHumburgerBtn == true) {
                        Button {
                            self.clickHumburger?()
                        } label: {
                            if(isShowImageFreeLine){
                                Image(systemName: "line.horizontal.3")
                                    .foregroundColor(Color.white)
                                    .padding(.leading, 12.0)
                                    .imageScale(.large)
                            }else{
                                Image(systemName: "arrow.left")
                                    .foregroundColor(Color.white)
                                    .padding(.leading, 12.0)
                                    .imageScale(.large)
                            }
                        }
                    }
                    
                    Spacer()
                    
                    if(isShowSearchBtn){
                        Button {
                            self.isShowSearchView = true
                        } label: {
                            Image("search")
                                .foregroundColor(Color.white)
                                .padding(.trailing, 12.0)
                                .imageScale(.large)
                        }
                    }
                    
                }
                
                HStack{
                    Spacer()
                    Text(title)
                        .font(.title2)
                        .foregroundColor(Color.white)
                    
                    Spacer()
                }
                
            }else{
                HStack {
                    
                    if let textSearch = textSearch {
                        TextField("Поиск ...", text: textSearch)
                            .padding(7)
                            .padding(.horizontal, 25)
                            .background(Color(.systemGray6))
                            .cornerRadius(8)
                            .overlay(
                                HStack {
                                    Image(systemName: "magnifyingglass")
                                        .foregroundColor(.gray)
                                        .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
                                        .padding(.leading, 8)
                                    
                                    if isEditing {
                                        Button(action: {
                                            self.textSearch?.wrappedValue =  ""
                                        }) {
                                            Image(systemName: "multiply.circle.fill")
                                                .foregroundColor(.gray)
                                                .padding(.trailing, 8)
                                        }
                                    }
                                }
                            )
                            .padding(.leading, 7)
                            .onTapGesture {
                                self.isEditing = true
                            }
                    }
                    
                   // if isEditing {
                        Button(action: {
                            self.textSearch?.wrappedValue = ""
                            self.isEditing = false
                            delayChangeShowSearchView()
                        }) {
                            Text("Отмена")
                                .foregroundColor(Color.white)
                                .padding(.all, 7.0)
                                .background(Color("black_bg25"))
                                .cornerRadius(4)
                        }
                        .padding(.trailing, 7)
                        .transition(.move(edge: .trailing))
                        .animation(.default)
                        
                    //}
                }
            }
        }
        .frame(height: 44.0)
        .background(Color("color_primary"))
        
    }
    
    private func delayChangeShowSearchView() {
         // Delay seconds
         DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
             isShowSearchView = false
         }
     }
}

struct MyToolBar_Previews: PreviewProvider {
    @State static private var na = "TEstTitle"
    @State static private var na2 : String = "" //$na2
    
    static var previews: some View {
        MyToolBar(title1: na, isShowSearchBtn: true, clickHumburger: {() -> Void in }, strSerch: $na2, isShowImageFreeLine: false )
        //MyToolBar(title1: "my test")
    }
}
