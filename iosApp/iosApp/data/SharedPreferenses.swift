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
    let CENTER_INFO_KEY="CENTER_INFO_KEY"
    let YANDEX_STORE_KEY = "YANDEX_STORE_KEY"
    
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
    
    var usersLogin : [UserResponse]?{
        get{
            let res = defaults.string(forKey: USERS_LOGIN_KEY)
            
            if res == nil {
                return [UserResponse]()
            }else{
                do{
                    let tmp = try MUtils.companion.stringToUsersLogin(str: res!)
                    return tmp
                }catch{
                    print("Неожиданная ошибка: \(error).")
                    return [UserResponse]()
                }
            }
            
            
            //let tmp = defaults.object(forKey: USERS_LOGIN_KEY) as? [UserResponse] ?? [UserResponse]()
            //return tmp
        }
        
        set ( nVal){
            
            if nVal != nil{
                do{
                    let str : String? = try MUtils.companion.usersLoginToString(cl: nVal!)
                    defaults.set(str, forKey: USERS_LOGIN_KEY)
                }catch{
                    print("Неожиданная ошибка: \(error).")
                }
            }else{
                defaults.set(nil, forKey: USERS_LOGIN_KEY)
            }
            
//            do{
//                let encodedData = try NSKeyedArchiver.archivedData(withRootObject: nVal, requiringSecureCoding: false)
//                defaults.set(encodedData, forKey: USERS_LOGIN_KEY)
//            }catch{
//
//            }
        }
    }
    
    var currentUserInfo : UserResponse?{
        get{
            let res = defaults.string(forKey: CURRENT_USER_INFO_KEY)
            
            if res == nil {
                return nil
            }else{
                do{
                    let tmp = try MUtils.companion.stringToUserResponse(str: res!) as UserResponse
                    return tmp
                }catch{
                    print("Неожиданная ошибка: \(error).")
                    return nil
                }
            }
        }
        
        set(nVal){
            if nVal != nil{
                do{
                    var str : String? = try MUtils.companion.userResponseToString(cl: nVal!)
                    defaults.set(str, forKey: CURRENT_USER_INFO_KEY)
                }catch{
                    print("Неожиданная ошибка: \(error).")
                }
            }else{
                defaults.set(nil, forKey: CURRENT_USER_INFO_KEY)
            }
        }
    }
    
    var centerInfo : CenterResponse?{
        set(nVal){
            if nVal != nil{
                do{
                    var str : String? = try MUtils.companion.centerResponseToString(cl: nVal!)
                    defaults.set(str, forKey: CENTER_INFO_KEY)
                }catch{
                    print("Неожиданная ошибка: \(error).")
                }
            }else{
                defaults.set(nil, forKey: CENTER_INFO_KEY)
            }
            
        }
        
        get{
            
            let res : String? = defaults.string(forKey: CENTER_INFO_KEY)
            
            if res == nil {
                return nil
            }else{
                do{
                    let tmp = try MUtils.companion.stringToCenterResponse(str: res!) as CenterResponse
                    return tmp
                }catch{
                    print("Неожиданная ошибка: \(error).")
                    return nil
                }
            }
        }
    }
    
    var yandexStoreIsWorks : Bool{
        get{
            return defaults.bool(forKey: YANDEX_STORE_KEY)
        }
        
        set ( nVal){
            defaults.set(nVal, forKey: YANDEX_STORE_KEY)
        }
    }
    
}
