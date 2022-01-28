package tta.astrologerapp.talktoastro.chat.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.startService

class FCMInstanceIDService : FirebaseMessagingService(), AnkoLogger {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    override fun onNewToken(p0: String) {

        debug { "onNewToken" }

        // Fetch updated Instance ID token and notify our app's server of any changes.
        startService<RegistrationIntentService>()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.w("onMessageReceived", "Message Received from " + remoteMessage.from)
    }
}
