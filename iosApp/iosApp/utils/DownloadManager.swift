//
//  DownloadManager.swift
//  iosApp
//
//  Created by Михаил Хари on 28.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

class DownloadManager {
    var isDownloading = false

    let imagePathServerString : String
    let resultUiImage : ((UIImage) -> Void)
    
    init(_ imagePathString : String, resultUiImage : ((UIImage) -> Void)?){
        self.imagePathServerString = imagePathString
        self.resultUiImage = resultUiImage!
        
        let nameFile = getNameFile()
        
        if(nameFile != nil && !nameFile.isEmpty){
            let res1 = checkFile(nameFile)
            
            if res1 != nil{
                DispatchQueue.main.async {
                    self.resultUiImage(res1!)
                }
            }else{
                downloadFile(nameFile)
            }
        }
    }
    
    func getNameFile() -> String{
        var st1 : String = String( imagePathServerString[..<self.imagePathServerString.firstIndex(of: "&")!])
        let st2 = String(st1[st1.range(of: "path=")!.lowerBound...])
        let st3 = String(st2[st2.index(st2.startIndex, offsetBy: 5)...])
        return st3
    }
    
    func checkFile(_ fileName : String) -> UIImage?{
        if fileName == nil || fileName.isEmpty {
            return nil
        }
        
        let docsUrl = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first

        let destinationUrl = docsUrl?.appendingPathComponent(fileName)
        if let destinationUrl = destinationUrl {
            if (FileManager().fileExists(atPath: destinationUrl.path)) {
                do {
                    let data = try! Data.init(contentsOf: destinationUrl)
                    let photo = UIImage.init(data: data)
                    
                    return photo
                } catch {
                    return nil
                }
            } else {
                return nil
            }
        } else {
            return nil
        }
    }

    func downloadFile(_ fileName : String){
        if(imagePathServerString == nil || imagePathServerString.isEmpty){
            return
        }
        
        isDownloading = true
        
        let docsUrl = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first
        let destinationUrl = docsUrl?.appendingPathComponent(fileName)
        
        if let destinationUrl = destinationUrl {
            
            if let urlString = imagePathServerString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed), let url = URL(string: urlString) {
                
                let urlRequest = URLRequest(url: url)
                
                let dataTask = URLSession.shared.dataTask(with: urlRequest) { (data, response, error) in
                    if let error = error {
                        print("Request error: ", error)
                        self.isDownloading = false
                        return
                    }
                    
                    guard let response = response as? HTTPURLResponse else { return }
                    
                    if response.statusCode == 200 {
                        guard let data = data else {
                            self.isDownloading = false
                            return
                        }
                        
                        DispatchQueue.main.async {
                            let strBase64 : String = String(decoding: data, as: UTF8.self)
                            
                            let dataDecoded : NSData = NSData(base64Encoded: strBase64, options: NSData.Base64DecodingOptions(rawValue: 0))!
                            let decodedimage : UIImage = UIImage(data: dataDecoded as Data)!
                
                            if let data = decodedimage.pngData() {
                                try? data.write(to: destinationUrl)
                                let data = try! Data.init(contentsOf: destinationUrl)
                                let photo = UIImage.init(data: data)

                                DispatchQueue.main.async {
                                    self.resultUiImage(photo!)
                                }
                            }
                        }
                        
                    }
                }
                dataTask.resume()
            }
        }
    }
    
    
//
//    func deleteFile() {
//        let docsUrl = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first
//
//        let destinationUrl = docsUrl?.appendingPathComponent("myVideo.mp4")
//        if let destinationUrl = destinationUrl {
//            guard FileManager().fileExists(atPath: destinationUrl.path) else { return }
//            do {
//                try FileManager().removeItem(atPath: destinationUrl.path)
//                self.isDownloaded = false
//                print("File deleted successfully")
//            } catch let error {
//                print("Error while deleting video file: ", error)
//            }
//        }
//    }
}
