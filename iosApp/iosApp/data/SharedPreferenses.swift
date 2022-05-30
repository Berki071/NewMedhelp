//
//  SharedPreferenses.swift
//  iosApp
//
//  Created by Михаил Хари on 27.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class SharedPreferenses{
    let CURRENT_LOGIN_KEY="CURRENT_LOGIN_KEY"
    let CURRENT_PASSWORD_KEY="CURRENT_PASSWORD_KEY"
    let USERS_LOGIN_KEY="USERS_LOGIN_KEY"
    let CURRENT_USER_INFO_KEY="CURRENT_USER_INFO_KEY"
    
    let defaults : UserDefaults
    
    init(){
        defaults = UserDefaults.standard
    }
    
    var currentLogin : String?{
        get{
            return defaults.string(forKey: CURRENT_LOGIN_KEY)
        }
        
        set ( nVal){
            defaults.set(nVal, forKey: CURRENT_LOGIN_KEY)
        }
    }
    
    var currentPassword : String?{
        get{
            return defaults.string(forKey: CURRENT_PASSWORD_KEY)
        }
        
        set ( nVal){
            defaults.set(nVal, forKey: CURRENT_PASSWORD_KEY)
        }
    }
    
    var usersLogin : [UserResponse]{
        get{
            let tmp = defaults.object(forKey: USERS_LOGIN_KEY) as? [UserResponse] ?? [UserResponse]()
            
            //let decodedTeams = try NSKeyedUnarchiver.unarchiveTopLevelObjectWithData(tmp) as! [UserResponse]
            return tmp
            
            
        }
        
        set ( nVal){
            do{
                let encodedData = try NSKeyedArchiver.archivedData(withRootObject: nVal, requiringSecureCoding: false)
                defaults.set(encodedData, forKey: USERS_LOGIN_KEY)
            }catch{
                
            }
        }
    }
    
    var currentUserInfo : UserResponse?{
        get{
            return defaults.object(forKey: CURRENT_USER_INFO_KEY) as? UserResponse ?? nil
        }
        
        set ( nVal){
          //  defaults.set(nVal, forKey: CURRENT_USER_INFO_KEY)
            
            do{
                let encodedData = try NSKeyedArchiver.archivedData(withRootObject: nVal, requiringSecureCoding: false)
                defaults.set(encodedData, forKey: CURRENT_USER_INFO_KEY)
            }catch{
}
        }
    }
}
