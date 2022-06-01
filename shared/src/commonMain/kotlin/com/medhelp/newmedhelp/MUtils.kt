package com.medhelp.newmedhelp

import com.medhelp.shared.model.CenterResponse
import com.medhelp.shared.model.UserResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

class MUtils {
    companion object {
        fun getEncodeKey(word: String): String? {
            var tmp = ""
            val generator: Random = Random
            for (i in 0 until word.length) {
                if (word[i].code > 255) tmp += generator.nextInt(4095)
                    .toChar() else tmp += generator.nextInt(255)
                    .toChar()
            }
            return tmp
        }
        fun encodeDecodeWord(word: String, key: String): String? {

            var encodeStr = ""
            for (i in 0 until word.length) encodeStr += (word[i].toInt() xor key[i].toInt()).toChar()
            return encodeStr
        }

        @Throws(Exception::class)
        fun  userResponseToString (cl : UserResponse) : String{
            val json = Json.encodeToString(cl)
            return json
        }
        @Throws(Exception::class)
        fun  stringToUserResponse (str : String) : UserResponse {
            return Json.decodeFromString(str)
        }


        @Throws(Exception::class)
        fun  сenterResponseToString (cl : CenterResponse) : String{
            val json = Json.encodeToString(cl)
            return json
        }
        @Throws(Exception::class)
        fun  stringToCenterResponse (str : String) : CenterResponse {
            return Json.decodeFromString(str)
        }
    }
}