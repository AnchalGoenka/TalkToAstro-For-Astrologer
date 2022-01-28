package tta.astrologerapp.talktoastro.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.notificationManager
import tta.astrologerapp.talktoastro.MainActivity
import tta.astrologerapp.talktoastro.MyBroadcastReceiver
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils


class FCMListenerService : FirebaseMessagingService(), AnkoLogger {

    val TAG = "FirebaseInstanceIDService"
    lateinit var chatSound: Uri
    lateinit var  msg :String
    val vibrationPattern = longArrayOf(500)

    companion object {
        val notification =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val mp = MediaPlayer.create(ApplicationUtil.getContext(), notification)
        var player = MediaPlayer.create(ApplicationUtil.getContext(), Settings.System.DEFAULT_RINGTONE_URI)
    }

    override fun onNewToken(token: String) {

        if (!TextUtils.isEmpty(token)){
            SharedPreferenceUtils.put(
                ApplicationConstant.DEVICETOKEN, token.toString(),
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )
        }
            saveTokenInPreference(token, applicationContext)
    }

    @SuppressLint("LongLogTag")
    private fun saveTokenInPreference(token: String, context: Context) {
        Log.i(TAG, "Refreshed token:" + token)
        Log.d("TOken>>>",token)
        SharedPreferenceUtils.put(
            ApplicationConstant.DEVICETOKEN, token.toString(),
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
    }

    override fun onMessageReceived(retmoteMessage: RemoteMessage) {

      /*  if (BaseApplication.instance.numRunningActivities == 0) {

            if (mp.isPlaying) {
                return
            }
            mp.start()

            // If the application is in the foreground handle both data and notification messages here.
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
            if (retmoteMessage == null) return;

            Log.d("onMessageReceived", "" + retmoteMessage.data);
            debug { "onMessageReceived for FCM from: ${retmoteMessage.from}" }

            // Check if message contains a data payload.
            if (retmoteMessage.data.isNotEmpty()) {
                debug { "Data Message Body: ${retmoteMessage.data}" }

                val payload = NotificationPayload(retmoteMessage.data)

                val client = BaseApplication.instance.basicClient.chatClient
                client?.handleNotification(payload)

                val type = payload.type

                // if (type == NotificationPayload.Type.UNKNOWN) return  // Ignore everything we don't support

                var title = "You Received chat"

                if (type == NotificationPayload.Type.NEW_MESSAGE)
                    title = "Twilio: New Message"
                if (type == NotificationPayload.Type.ADDED_TO_CHANNEL)
                    title = "Twilio: Added to Channel"
                if (type == NotificationPayload.Type.INVITED_TO_CHANNEL)
                    title = "Twilio: Invited to Channel"
                if (type == NotificationPayload.Type.REMOVED_FROM_CHANNEL)
                    title = "Twilio: Removed from Channel"

                // Set up action Intent
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("isFromChat", true);

                val cSid = payload.channelSid
                if (!"".contentEquals(cSid)) {
                    intent.putExtra(Constants.EXTRA_CHANNEL_SID, cSid)
                }

                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "tta_channel_id"
                val channelName: CharSequence = "TTA Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                 val notificationChannel =
                     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                         NotificationChannel(channelId, channelName, importance)
                     } else {
                         TODO("VERSION.SDK_INT < O")
                     }
                 notificationChannel.enableLights(true)
                 notificationChannel.lightColor = Color.RED
                 notificationChannel.enableVibration(true)
                 notificationChannel.vibrationPattern = longArrayOf(1000, 2000)
                 notificationManager.createNotificationChannel(notificationChannel)

                val notification = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(payload.body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setColor(Color.rgb(214, 10, 37))
                    .build()

                val soundFileName = payload.sound
                if (resources.getIdentifier(soundFileName, "raw", packageName) != 0) {
                    val sound = Uri.parse("android.resource://$packageName/raw/$soundFileName")
                    notification.defaults =
                        notification.defaults and Notification.DEFAULT_SOUND.inv()
                    notification.sound = sound
                    debug { "Playing specified sound $soundFileName" }
                } else {
                    notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
                    debug { "Playing default sound" }
                }

                notificationManager.notify(0, notification)
            }

            // Check if message contains a notification payload.
            if (retmoteMessage.notification != null) {
                //debug { "Notification Message Body: ${remoteMessage.notification.body}" }
                error { "We do not parse notification body - leave it to system" }
            }
        }*/
        /*val intent = Intent(this, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 *//* Request code *//*, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var builder: NotificationCompat.Builder? = null
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel =
                NotificationChannel("ID", "Name", importance)
            notificationManager.createNotificationChannel(notificationChannel)
            NotificationCompat.Builder(
                applicationContext,
                notificationChannel.id
            )
        } else {
            NotificationCompat.Builder(applicationContext)
        }

        builder = builder
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryLight_Dark))
            .setContentTitle(retmoteMessage.data.toString())
           // .setTicker(title)
            .setContentText(retmoteMessage.data.toString())
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.bubble_b,"ACcept",pendingIntent)
        notificationManager.notify(1, builder.build())*/

        if(retmoteMessage.data["title"] == "Incoming chat ..."){

            val chat_id = retmoteMessage.data["chat_id"].toString()
          //  BaseApplication.instance.showToast("chat_id before>>>>>>>> ${retmoteMessage.data["chat_id"].toString()}")
            ApplicationConstant.ChatNotification = true
            val intent = Intent(this, MainActivity::class.java)
           // intent.setAction(Intent.ACTION_MAIN);
          //  intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  }
            intent.putExtra("msg",retmoteMessage.data["msg"].toString())
            intent.putExtra("chatID",chat_id)
            intent.putExtra("title",retmoteMessage.data["title"])
            intent.putExtra("UserID",retmoteMessage.data["user_id"])
            intent. putExtra("Duration",retmoteMessage.data["duration"])
            intent.putExtra("userName",retmoteMessage.data["username"])
           // BaseApplication.instance.showToast("chat_id before>>>>>>>> ${retmoteMessage.data["chat_id"].toString()}")
            this.startActivity(intent)

           // BaseApplication.instance.showToast("duration before>>>>>>>> ${retmoteMessage.data["duration"].toString()}")
            showNotification(retmoteMessage)
        }else if(retmoteMessage.data["title"] == "Chat Ended !!"){
            ApplicationConstant.ChatNotification = false
            val intent = Intent(this, MainActivity::class.java)
            intent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   }
            this.startActivity(intent)
        }
        else if(retmoteMessage.data["title"] == "Missed chat request !"){
           // showNotification(retmoteMessage)
            showNotification1(retmoteMessage.data["title"],retmoteMessage.data["msg"].toString())
        }
        else{
             if (retmoteMessage.data.isNotEmpty()) {
               showNotification1(retmoteMessage.data["title"],retmoteMessage.data["msg"].toString())
             }else{
               showNotification1(retmoteMessage.notification?.title,retmoteMessage.notification?.body)
             }
        }

       // showNotification(retmoteMessage)
       // msg =retmoteMessage.data["msg"]


    }
    private fun showNotification1(title: String?, body: String?) {
        val channelId =  "astro"
        val notificationId = 2
        createNotificationChannel(channelId)

        val intent =Intent(this,MainActivity::class.java)
        intent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   }
        val pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationBuilder = NotificationCompat.Builder(this ,  channelId)
            .setSmallIcon(R.mipmap.ic_logo_tta1)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH or NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(vibrationPattern)
            .setColor(Color.rgb(214, 10, 37))
           // .setNotificationSilent()

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, notificationBuilder.build())
        }

    }


    @SuppressLint("InvalidWakeLockTag")
    fun showNotification(remoteMessage: RemoteMessage){
        var notificationBuilder: NotificationCompat.Builder? = null
        msg = remoteMessage.data["msg"].toString()

        var soundUri: Uri
        soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sound);
        //   chatSound = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://" + this.packageName + "/" + R.raw.sound)
        chatSound =    Uri.parse("android.resource://" + packageName + "/" + R.raw.sound)
        val chatId= remoteMessage.data["chat_id"]
        val ttaUserID = remoteMessage.data["user_id"]
        player = MediaPlayer.create(ApplicationUtil.getContext(), Settings.System.DEFAULT_RINGTONE_URI)
        //  player .isLooping = true
        //   player.start()

        val channelId =  "astrologers"
        val notificationId = 1
        createNotificationChannel(channelId)

        //  val userId= SharedPreferenceUtils.readString(ApplicationConstant.USERID,"",SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))

        val intent =Intent(this,MainActivity::class.java)
            intent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("msg",remoteMessage.data["msg"].toString())
            intent.putExtra("chatID",remoteMessage.data["chat_id"].toString())
            intent.putExtra("title",remoteMessage.data["title"])
            intent.putExtra("UserID",remoteMessage.data["user_id"])
            intent. putExtra("Duration",remoteMessage.data["duration"])
            intent.putExtra("userName",remoteMessage.data["username"])
               // NotificationManagerCompat.from(applicationContext).cancel(1)
               // notificationBuilder?.setNotificationSilent()
            // BaseApplication.instance.showToast("chat_id before>>>>>>>> ${retmoteMessage.data["chat_id"].toString()}")

             }
        val pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val denyIntent =Intent(this, MyBroadcastReceiver::class.java)
        denyIntent.apply {
            // flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action ="Deny"
            putExtra("UserID",ttaUserID)
            putExtra("chatId",chatId)
        }
        val denyPendingIntent =PendingIntent.getBroadcast(this,0,denyIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val acceptIntent =Intent(this, MyBroadcastReceiver::class.java)
        acceptIntent.apply {
            //   flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "Accept"
            putExtra("UserID",ttaUserID)
            putExtra("chatId",chatId)
            putExtra("Duration",remoteMessage.data["duration"].toString())
            putExtra("userName",remoteMessage.data["username"].toString())

        }
        val acceptPendingIntent =PendingIntent.getBroadcast(this,0,acceptIntent,PendingIntent.FLAG_UPDATE_CURRENT)

         notificationBuilder = NotificationCompat.Builder(this ,  channelId)
            .setSmallIcon(R.mipmap.ic_logo_tta1)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText("$msg" )
            //   .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH or NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.button_rounded_corners_red,remoteMessage.data["deny"],denyPendingIntent)
            .addAction(R.drawable.button_rounded_corners_green,remoteMessage.data["accept"],acceptPendingIntent)
            .setAutoCancel(true)
            .setVibrate(vibrationPattern)
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +this.getPackageName()+"/"+R.raw.sound))
            // .setSound(chatSound)
            .setColor(Color.rgb(214, 10, 37))

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, notificationBuilder.build())
        }

        val powerManager =
            getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: PowerManager.WakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag")
        wakeLock.acquire()

    }
    private fun createNotificationChannel(channelId:String) {
        // Create the NotificationChannel, but only on API 26+ (Android 8.0) because
        // the NotificationChannel class is new and not in the support library
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("channelId>>",channelId)
        if (channelId == "astrologers") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "My Channel"
                val channelDescription = "Channel Description"
                val importance = NotificationManager.IMPORTANCE_HIGH

                val sound =
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.sound)
                /*val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()*/

                val channel = NotificationChannel(channelId, name, importance)
                channel.apply {
                    description = channelDescription
                }
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                channel.setSound(sound, audioAttributes)
                channel.enableLights(true)
                channel.enableVibration(true)
                // channel.vibrationPattern = longArrayOf(100, 1000, 100, 1000, 100,1000,100)
                channel.vibrationPattern = vibrationPattern
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                // Finally register the channel with system

                notificationManager.createNotificationChannel(channel)
            }
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "other"
                val channelDescription = "other Description"
                val importance = NotificationManager.IMPORTANCE_HIGH

                val channel = NotificationChannel(channelId, name, importance)
                channel.apply {
                    description = channelDescription
                }

                // channel.setSound(sound, audioAttributes)
                channel.enableLights(true)
                channel.enableVibration(true)
                // channel.vibrationPattern = longArrayOf(100, 1000, 100, 1000, 100,1000,100)
                channel.vibrationPattern = vibrationPattern
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                // Finally register the channel with system
                /*val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager*/
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


    fun clearNotification(notificationId : Int ){
        val handler = Handler(Looper.getMainLooper())

        //2
        handler.post(Runnable {
            Toast.makeText(baseContext, "hello",
                Toast.LENGTH_LONG).show()
            val timer = object: CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Toast.makeText(baseContext, "hello"+millisUntilFinished,
                        Toast.LENGTH_LONG).show()

                  /*  NotificationManager mManager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
                    mManager.cancel(id);
                    missedNotification("test");*/

                }

                override fun onFinish() {
                    Toast.makeText(baseContext, "finish",
                        Toast.LENGTH_LONG).show()

                    msg ="sxbcbsjbx"
                }
            }
            timer.start()
        })

    }
    

}
