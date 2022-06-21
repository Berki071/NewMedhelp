//
//  ProfileTitle.swift
//  iosApp
//
//  Created by Михаил Хари on 21.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ProfileTitle: View {
    var item : VisitResponseIos
    
    var body: some View {
        HStack{
            Text(item.nameServices!)
                .multilineTextAlignment(.leading)
                .padding(8.0)
                .frame(maxWidth: .infinity)
                .background(Color("black_bg3"))
                .cornerRadius(8)
        }
        .padding(8.0)
        .frame(maxWidth: .infinity)
    }
}

struct ProfileTitle_Previews: PreviewProvider {
    @State static private var ite = VisitResponseIos(servName : "Title Number One", nameSotr : "", date : "", time : "", branch : "", cost : 0 )
    
    static var previews: some View {
        ProfileTitle(item : ite)
    }
}
