package tta.astrologerapp.talktoastro

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder


/**

 * Created by Vivek Singh on 7/19/20.

 */
class BackgroundService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}