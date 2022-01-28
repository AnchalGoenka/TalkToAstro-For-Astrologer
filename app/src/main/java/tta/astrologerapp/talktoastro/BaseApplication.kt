package tta.astrologerapp.talktoastro

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.android.volley.VolleyLog
import java.util.*
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.android.volley.BuildConfig
import com.twilio.chat.Channel
import com.twilio.chat.ErrorInfo
import tta.astrologerapp.talktoastro.chat.BasicChatClient
import tta.astrologerapp.talktoastro.volley.VollyController
import tta.astrologerapp.talktoastro.volley.VollyRequestManager
import tta.destinigo.talktoastro.agora.chat.ChatManager


/**

 * Created by Vivek Singh on 2019-06-10.

 */

 abstract class BaseApplication: MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    private var mActivityTransitionTimer: Timer? = null
    private var mActivityTransitionTimerTask: TimerTask? = null
    var mAppInBackground:Boolean = false
    var numRunningActivities:Int = 0
    private val MAX_ACTIVITY_TRANSITION_TIME_MS:Long = 2000
    var p0: Channel?=null
    lateinit var vollyController: VollyController
    lateinit var basicClient: BasicChatClient
        private set
    var chatManager: ChatManager? = null
    // check weather app is in background
    var isAppInBackground:Boolean
        get() = mAppInBackground
        set(isAppInBackground) {
            mAppInBackground = isAppInBackground
        }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initializeComponents()
        /*You may wish to enable Thread policy while in debug mode. always place this call after setDebuggable()*/
        //enableStrictPolicy(BuildConfig.DEBUG)
        instance = this
        basicClient = BasicChatClient(applicationContext)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        startService(Intent(this, BackgroundService::class.java))
        registerActivityLifecycleCallbacks(this)

        chatManager = ChatManager(this)
        chatManager!!.init()
    }

    @JvmOverloads fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Handler(Looper.getMainLooper()).post {
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
            if (!TextUtils.isEmpty(text))
                toast.show()
        }
    }

    fun showError(error: ErrorInfo) {
        //showError("Something went wrong", error)
    }

    fun showError(message: String, error: ErrorInfo) {
        showToast(formatted(message, error), Toast.LENGTH_LONG)
        logErrorInfo(message, error)
    }

    fun logErrorInfo(message: String, error: ErrorInfo) {
        error { formatted(message, error) }
    }

    private fun formatted(message: String, error: ErrorInfo): String {
        return String.format("%s. %s", message, error.toString())
    }

    /**
     * should be called from onResume of each activity of application
     */
    fun onActivityResumed() {
        if (this.mActivityTransitionTimerTask != null)
        {
            this.mActivityTransitionTimerTask!!.cancel()
        }

        if (this.mActivityTransitionTimer != null)
        {
            this.mActivityTransitionTimer!!.cancel()
        }
        isAppInBackground = false
    }

    /**
     * should be called from onPause of each activity of app
     */
    fun onActivityPaused() {
        this.mActivityTransitionTimer = Timer()
        this.mActivityTransitionTimerTask = object: TimerTask() {
            override fun run() {
                isAppInBackground = true
            }
        }

        this.mActivityTransitionTimer!!.schedule(mActivityTransitionTimerTask!!, MAX_ACTIVITY_TRANSITION_TIME_MS)
    }

    /**
     * initializeVolly volly for the application
     */
    protected fun initializeVolly(appName:String, defaultDiskUsageByte:Int, threadPoolSizes:Int) {
        var defaultDiskUsageBytes = defaultDiskUsageByte
        var threadPoolSize = threadPoolSizes

        if (defaultDiskUsageBytes <= 0)
        //set default to 5 MB
            defaultDiskUsageBytes = 5242880
        if (threadPoolSize <= 1)
            threadPoolSize = 4

        vollyController = VollyController.initialize(this.applicationContext, VollyRequestManager.Config(
            "data/data/" +
                    appName +
                    "/imageCache", defaultDiskUsageBytes, threadPoolSize))

        VolleyLog.DEBUG = BuildConfig.DEBUG //if Volly is used
    }


    /*
           Enable  Thread and VM policy to detect and avoid avoid performing
            long running operations on the UI thread.
        */
    protected fun enableStrictPolicy(enable:Boolean) {
        //----------------------------Strict Policy-----------------------------//

        if (enable)
        {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .detectCustomSlowCalls()
                    .penaltyLog()
                    //                    .penaltyDropBox()
                    .penaltyDialog()
                    .build())

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    //	                 .detectLeakedRegistrationObjects()
                    .penaltyDropBox()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
        //----------------------------Strict Policy-----------------------------//

    }


    protected abstract fun initializeComponents()

    companion object{
        lateinit var instance : BaseApplication public set
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity?) {
        numRunningActivities++;
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityStopped(p0: Activity?) {
        numRunningActivities--;
        if (numRunningActivities == 0) {
            Log.i("TAG", "No running activities left, app has likely entered the background.");
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    fun save(p0: Channel?) {
        this.p0=p0
    }
}