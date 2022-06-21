package com.medhelp.medhelp.bg.service

import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.os.Looper
import com.medhelp.medhelp.bg.notifiaction.SimpleNotificationForFCM
import android.app.NotificationManager
import android.os.Handler
import com.medhelp.medhelp.data.pref.PreferencesManager
import timber.log.Timber
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            if (remoteMessage.notification != null) {
                //если открыта страница то она обновиться без уведомлений
                if (remoteMessage.data.size > 0) {
                    val type_message = remoteMessage.data["type_message"]
                    val id_kl = remoteMessage.data["id_kl"]
                    val id_filial = remoteMessage.data["id_filial"]
                    if (type_message != null && id_kl != null && id_filial != null) {
                        val simpleNotificationForFCM = SimpleNotificationForFCM(
                            applicationContext, getSystemService(
                                NOTIFICATION_SERVICE
                            ) as NotificationManager
                        )
                        simpleNotificationForFCM.showData(
                            remoteMessage.notification!!.title, remoteMessage.notification!!
                                .body, type_message, id_kl, id_filial
                        )
                    }
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }


    private var countFcmSend=0
    private fun sendRegistrationToServer(token: String) {
        val prefManager = PreferencesManager(applicationContext)
        val networkManager = NetworkManager()
        val list = prefManager.usersLogin
        if (list == null || list.size == 0) {
            return
        }

        val mainScope = MainScope()

        for (i in list) {
            mainScope.launch {
                kotlin.runCatching {
                    networkManager.sendFcmId(i.idUser.toString(), i.idBranch.toString(), token, prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
                }
                    .onSuccess {
                        mainScope.cancel()
                    }.onFailure {
                        Timber.tag("my").e(getMessageForError(it, "MyFirebaseMessagingService\$sendFcmToken"))
                        mainScope.cancel()
                    }
            }
        }
    }

}