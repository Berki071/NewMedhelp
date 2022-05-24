package com.medhelp.shared.network

interface LocalEndPoint {
    companion object {
        const val API_KEY = "AAAA2UBtySo:APA91bGOxg0DNY9Ojz-BD0d4bUr-GukFBdvCtivWVjqZ8ppEHtl-BIwmINKD3R_"

        //String BASE_URL = "https://oneclick.tmweb.ru/v1/";
        const val BASE_URL = "https://oneclick.tmweb.ru/medhelp_main/v1/"


        const val LOGIN = BASE_URL + "login"
        const val CENTER = BASE_URL + "centres/{"+NetworkManager.ID_CENTER+"}"
        const val SEND_RATING =
            BASE_URL + "ocenkapril/{date}/{" + NetworkManager.RATING + "}/{" + NetworkManager.MESSAGE + "}/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_CENTER + "}"
        const val GET_ALL_HOSPITAL_BRANCH =
            BASE_URL + "allfilial/{" + NetworkManager.ID_CENTER + "}"
        const val NEW_FAVORITE_BRANCH =
            BASE_URL + "SmenaLikeFilial/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_USER_NEW + "}/{" + NetworkManager.ID_BRANCH_NEW + "}/{" + NetworkManager.ID_CENTER + "}"
        const val SEND_LOG_TO_SERVER =
            BASE_URL + "LogDataInsert/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_CENTER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.TYPE + "}/kl/{" + NetworkManager.VERSION_CODE + "}"
        const val REQUEST_PASS =
            BASE_URL + "NewPWDMobileUser/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.AUTH + "}"
        const val NEWS_UPDATE = BASE_URL + "NewsUpdate"
        const val GET_USERS_TECH_FCM_ID = BASE_URL + "LoadFCMtech"
        const val SEND_MSG_TO_SUPPORT = BASE_URL + "SendMessageToTech"
    }
}