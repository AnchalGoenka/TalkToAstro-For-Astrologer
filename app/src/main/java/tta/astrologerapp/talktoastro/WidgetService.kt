package tta.astrologerapp.talktoastro

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast


public class WidgetService:Service(){
    var LAyouut_FlAg = 0
    var view: View? = null
    private var wm: WindowManager? = null
    private var mAccept: TextView? = null
    private var mdeny: TextView? = null
    private var mtitle: TextView? = null
     val vibrator:Vibrator?=null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
             LAyouut_FlAg =WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            LAyouut_FlAg =WindowManager.LayoutParams.TYPE_PHONE
        }
        // inflate widget layout
        view =LayoutInflater.from(this).inflate(R.layout.layout_widget,null,false)
        val v = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        // Vibrate for 500 milliseconds
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(10,
                    VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            v.vibrate(10)
        }
        val player = MediaPlayer.create(this,Settings.System.DEFAULT_RINGTONE_URI)
        player.start()
        val layoutParms=WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,LAyouut_FlAg,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT)
        wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        //initial position
        layoutParms.gravity = Gravity.LEFT or Gravity.TOP
        layoutParms.x = 0
        layoutParms.y = 0
        //layout params
        mAccept = view?.findViewById(R.id.tv_accept)
        mdeny = view?.findViewById(R.id.tv_deny)
        mtitle = view?.findViewById(R.id.tv_title)
        mAccept?.setOnClickListener {
            Toast.makeText(this," Call Accept",Toast.LENGTH_LONG).show()
        }
        mdeny?.setOnClickListener {
            wm?.removeView(view)
            v.cancel()
            player.stop()
            Toast.makeText(this," Call Deny",Toast.LENGTH_LONG).show()
        }
        wm?.addView(view,layoutParms)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}