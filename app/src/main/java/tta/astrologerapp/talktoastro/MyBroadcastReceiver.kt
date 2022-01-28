package tta.astrologerapp.talktoastro

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import tta.astrologerapp.talktoastro.agora.chat.AgoraMsgActivity
import tta.astrologerapp.talktoastro.services.FCMListenerService
import tta.astrologerapp.talktoastro.ui.home.HomeFragment
import tta.astrologerapp.talktoastro.util.ApplicationConstant

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        val chatId =intent?.getStringExtra("chatId")
          //  val ussreId = getStringExtra("UserId")
            val notificationId = intent?.getIntExtra("notificationId",1)

        if (action.equals("Deny")){

           // FCMListenerService.player.release()
            ApplicationConstant.ChatNotification = false
            val mainActivityIntent=Intent(context,MainActivity::class.java)
            mainActivityIntent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   }
            mainActivityIntent.putExtra("iscomingfrom","MyBroadcastReceiver")
            mainActivityIntent.putExtra("chatId",chatId)
            context!!.startActivity(mainActivityIntent)
          //  MainActivity.instance.joinStatus(chatId,"cancel")

            Toast.makeText(context,"Chat Deny ",Toast.LENGTH_SHORT).show()
        }

        if (action.equals("Accept")){
            ApplicationConstant.ChatNotification = false
            val ussreID = intent?.getStringExtra("UserID")
            val duration = intent?.getStringExtra("Duration")
            val username = intent?.getStringExtra("userName")
          //  FCMListenerService.player.release()
           // MainActivity.instance.joinStatus(chatId,"accept")
            val intent = Intent(context,AgoraMsgActivity::class.java)
            intent.apply { flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   }
            intent.putExtra("userId",ussreID)
            intent.putExtra("chatId",chatId)
            intent.putExtra("Duration",duration)
            intent.putExtra("UserName",username)
            context!!.startActivity(intent)
           // Toast.makeText(context,"chatId: $chatId >>>>>>>UserId $ussreID ",Toast.LENGTH_SHORT).show()
        }

        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context!!.sendBroadcast(it)

        context.apply {
            // Remove the notification tray programmatically on button click
            NotificationManagerCompat.from(context).cancel(notificationId!!)
        }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(3)
        /*val action = intent?.action
        if (action.equals("Deny")){

            val ussreID = intent?.getExtras()!!["UserID"].toString()
            Toast.makeText(context,"Deleted ID: $ussreID",Toast.LENGTH_SHORT).show()
        }

        if (action.equals("Accept")){
            val ussreID = intent?.getStringExtra("UserID")
            val name = intent?.getExtras()!!["Name"].toString()
            Toast.makeText(context,"Update : $name",Toast.LENGTH_SHORT).show()
        }*/
}}