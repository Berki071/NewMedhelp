package com.medhelp.shared.network

import com.medhelp.newmedhelp.model.SimpleResBoolean
import com.medhelp.newmedhelp.model.SimpleResponseBoolean
import com.medhelp.shared.model.CenterList
import com.medhelp.shared.model.CurrentUserInfoList
import com.medhelp.shared.model.SettingsAllBaranchHospitalList
import com.medhelp.shared.model.UserList
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json


class NetworkManager {
    companion object {
        const val ID_CENTER = "id_center"
        const val ID_BRANCH = "ID_BRANCH"
        const val ID_BRANCH_NEW = "ID_BRANCH_NEW"
        const val ID_DOCTOR = "id_doctor"
        const val ID_SPEC = "id_spec"
        const val ID_SERVICE = "id_service"
        const val ID_USER = "id_user"
        const val ID_USER_NEW = "id_user_new"
        const val ID_FCM = "ID_FCM"
        const val USERNAME = "username"
        const val USERNAME2 = "USERNAME2"
        const val PASSWORD = "password"
        const val MESSAGE = "message"
        const val ADM_DATE = "date"
        const val ADM_TIME = "adm"
        const val AUTH = "Authorization"
        const val ID_ZAPISI = "id_zapisi"
        const val DATATODAY = "datatoday"
        const val DATE = "date"
        const val TIME = "time"
        const val DURATION = "duration"
        const val RATING = "RATING"
        const val ID_ROOM = "ID_ROOM"
        const val TYPE = "TYPE"
        const val ID_KL = "id_kl"
        const val ID_FILIAL = "id_filial"
        const val HASH = "hashkey"
        const val VERSION_CODE = "version_code"
        const val AMOUNT = "AMOUNT"
        const val QUANTITY = "quantity"
        const val DB_NAME = "db_name"
        const val FROM = "FROM"
        const val TO = "TO"
        const val INN = "INN"
        const val SPEC = "SPEC"

        const val BASE_URL_LOCAL = "https://oneclick.tmweb.ru/medhelp_main/v1/"
        const val BASE_URL_CENTER = "https://oneclick.tmweb.ru/medhelp_client/v1/"
    }

    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
            }
            serializer = KotlinxSerializer(json)
        }
    }

    @Throws(Exception::class)  suspend fun doLoginApiCall(signature: String, username: String, password: String): UserList {
        return httpClient.post(Url(BASE_URL_LOCAL + "login")) {
            headers {
                append(AUTH, LocalEndPoint.API_KEY)
            }

            body= MultiPartFormDataContent(formData {
                append(USERNAME, username)
                append(PASSWORD, password)
                append(HASH, signature)
            })
        }
    }

    @Throws(Exception::class)  suspend fun getCenterApiCall(idCenter : String) : CenterList {
        return httpClient.get(Url(BASE_URL_LOCAL + "centres/"+idCenter)) {
            headers {
                append(AUTH, LocalEndPoint.API_KEY)
            }
        }
    }

    @Throws(Exception::class)  suspend fun getCurrentUserInfoInCenter(idUser: String, idBranch: String, h_Auth : String, h_dbName : String, h_idKl : String, h_idFilial : String) : CurrentUserInfoList {
        return httpClient.get(Url(BASE_URL_CENTER + "ClientInfoById/" + idUser + "/" + idBranch)) {
            headers {
                append(AUTH, h_Auth)
                append(DB_NAME, h_dbName)
                append(ID_KL, h_idKl)
                append(ID_FILIAL, h_idFilial)
            }
        }
    }

    @Throws(Exception::class)  suspend fun  getAllHospitalBranch(idCenter : String) : SettingsAllBaranchHospitalList {
        return httpClient.post(Url(LocalEndPoint.BASE_URL + "allfilial/" + idCenter)) {
            headers {
                append(AUTH, LocalEndPoint.API_KEY)
            }
        }
    }

    @Throws(Exception::class)  suspend fun getBranchByIdService(idService: Int, h_Auth : String, h_dbName : String, h_idKl : String, h_idFilial : String) : SettingsAllBaranchHospitalList{
        return httpClient.get(Url( CenterEndPoint.BASE_URL + "FilialByIdYsl/" + idService.toString())) {
            headers {
                append(AUTH, h_Auth)
                append(DB_NAME, h_dbName)
                append(ID_KL, h_idKl)
                append(ID_FILIAL, h_idFilial)
            }
        }
    }

    @Throws(Exception::class) suspend fun getBranchByIdServiceIdDoc(idService: Int, idDoc: Int, h_Auth : String, h_dbName : String, h_idKl : String, h_idFilial : String) : SettingsAllBaranchHospitalList{
        return httpClient.get(Url( CenterEndPoint.BASE_URL + "FilialByIdYslIdSotr/" + idService+ "/" + idDoc)) {
            headers {
                append(AUTH, h_Auth)
                append(DB_NAME, h_dbName)
                append(ID_KL, h_idKl)
                append(ID_FILIAL, h_idFilial)
            }
        }
    }

    @Throws(Exception::class) suspend fun sendLogToServer(type: String, log: String, versionCode: String,  idUSer: String, idBranch: String, idCenter: String) : SimpleResponseBoolean {
        return httpClient.post(Url(LocalEndPoint.BASE_URL + "LogDataInsert/" + idUSer + "/" + idCenter + "/" + idBranch + "/" + type + "/kl/" + versionCode)) {
            headers {
                append(AUTH, LocalEndPoint.API_KEY)
            }

            body= MultiPartFormDataContent(formData {
                append("log", log)
            })
        }
    }

    @Throws(Exception::class) suspend fun sendFcmId (idUser: String, idFilial: String, idFcm: String, h_Auth : String, h_dbName : String, h_idKl : String, h_idFilial : String) : SimpleResBoolean {
        return httpClient.get(Url( CenterEndPoint.BASE_URL + "UpdateFCMuser/" + idUser + "/" + idFilial + "/" + idFcm )) {
            headers {
                append(AUTH, h_Auth)
                append(DB_NAME, h_dbName)
                append(ID_KL, h_idKl)
                append(ID_FILIAL, h_idFilial)
            }
        }
    }


}

