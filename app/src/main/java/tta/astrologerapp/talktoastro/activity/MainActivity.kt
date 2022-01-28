package tta.astrologerapp.talktoastro

import ChatCallbackListener
import ToastStatusListener
import android.app.ActivityManager
import android.app.Dialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.accountapp.accounts.utils.Prefences
import com.example.awesomedialog.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.kaopiz.kprogresshud.KProgressHUD
import com.twilio.chat.*
import kotlinx.android.synthetic.main.chat_notification_popup.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.custom_dialogs.tv_title
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.*
import tour.traveling.travel.ui.product.ComounViewModel
import tta.astrologerapp.talktoastro.Interface.OkValueCallback
import tta.astrologerapp.talktoastro.agora.chat.AgoraMsgActivity
import tta.astrologerapp.talktoastro.chat.BasicChatClient
import tta.astrologerapp.talktoastro.chat.ChannelModel
import tta.astrologerapp.talktoastro.chat.activities.ChannelActivity
import tta.astrologerapp.talktoastro.chat.activities.MessageActivity
import tta.astrologerapp.talktoastro.model.TokenModel
import tta.astrologerapp.talktoastro.services.FCMListenerService
import tta.astrologerapp.talktoastro.util.*
import tta.astrologerapp.talktoastro.viewmodel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity(), ChatClientListener, BasicChatClient.LoginListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var hud: KProgressHUD? = null
    lateinit var mBaseApplication: BaseApplication
    lateinit var mHelperViewModel: MainViewModel
    private lateinit var toolbar: Toolbar
    private var isTokenUpdatedCalled = false
    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val mp = MediaPlayer.create(ApplicationUtil.getContext(), notification)
    private var mediaPlayer : MediaPlayer? = null
    var alert: AlertBuilder<DialogInterface> ?=null

    val mViewModel by lazy { ViewModelProviders.of(this).get(ComounViewModel::class.java) }

    lateinit var OldDate: Date
    lateinit var channelDescriptor: ChannelDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        hud = KProgressHUD(this)
        instance= this

       /* var comingFrom = intent.getStringExtra("iscoming")
        if(comingFrom=="ChatScreen"){
            view_return_to_chat.visibility = View.VISIBLE
        }else{
            view_return_to_chat.visibility = View.GONE
        }*/

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        view_return_to_chat.setOnClickListener {
            var i=intent.getStringExtra("peerId")
            BaseApplication.instance.showToast("sbxhbhcbz $i")
            val intent = Intent(this, AgoraMsgActivity::class.java)
            intent.putExtra("returntochat","yes")
            intent.putExtra("userId", i)
            startActivity(intent)
        }
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_articles, R.id.nav_privacy_policy,
                R.id.nav_Customer_Support, R.id.nav_profile, R.id.nav_report_history,
                R.id.nav_logout, R.id.nav_txv_email, R.id.nav_txv_phone,
                R.id.nav_txv_name, R.id.nav_login,R.id.nav_free_questions
            ), drawerLayout
        )
       // NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_login) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
        checkLogin(toolbar, navView, drawerLayout)
        if (getIntent().getIntExtra("fragmentNumber", 0) == 3) {
            val hashVal = getIntent().getSerializableExtra("login credentials")
            var bundle = Bundle()
            bundle.putString("Register", "login")
            bundle.putSerializable("login credential", hashVal)
            navController.navigate(R.id.nav_verifyOtp, bundle)
        }
        val application = this.application
        if (application is BaseApplication) {
            mBaseApplication = application
        }
        mHelperViewModel =
            ViewModelProviders.of(this, MyViewModelFactory(myApplication))
                .get(MainViewModel::class.java)
        checkAppUpdate()
        checkForAppUpdate(this)
        val chatId =intent?.getStringExtra("chatId")
        val iscoming = intent?.getStringExtra("iscomingfrom")
        if(iscoming=="MyBroadcastReceiver"){
            joinStatus(chatId,"cancel")
        }
        if(ApplicationConstant.ChatNotification==true){

            chat()

        }

   getCurrentActivity()
        /* Log.d("Token>>","token")
         FirebaseInstanceId.getInstance().instanceId
             .addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w("", "getInstanceId failed", task.exception)
                     return@OnCompleteListener
                 }

                 // Get new Instance ID token
                 val token = task.result?.token
                 BaseApplication.instance.showToast("$token")
                 Log.d("Token>>",token)
                 SharedPreferenceUtils.put(
                     ApplicationConstant.DEVICETOKEN, token.toString(),
                     SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
                 )
                 LogUtils.d("Token: $token")
                 //Toast.makeText(baseContext, "Token:$token", Toast.LENGTH_SHORT).show()
             })*/

      //  FCMListenerService.mp.stop()


        Log.d("MainActivtyisShoDialog", "" + Prefences.getShowDialog(getApplication()))


        Log.d(
            "My_token", SharedPreferenceUtils.readString(
                ApplicationConstant.DEVICETOKEN, "",
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )
        )

        if (intent.hasExtra("isFromChat")) {
            Prefences.setShowDialog(applicationContext, true)

            if (!this@MainActivity.isFinishing()) {
                getToolbar().postDelayed(Runnable {
                    if (BaseApplication.instance.basicClient.chatClient != null)
                        BaseApplication.instance.basicClient.chatClient!!.channels.getUserChannelsList(
                            object :
                                CallbackListener<Paginator<ChannelDescriptor>>() {

                                var defaultDate: Date? = null
                                var latestDate: Date? = null
                                var latestChannelDescriptor: ChannelDescriptor? = null

                                override fun onSuccess(channelPaginator: Paginator<ChannelDescriptor>?) {
                                    for (channel in channelPaginator!!.getItems()) {
                                        var channelCreatedDate: Date = channel.dateCreated
//                                        var createdBy: String = channel.createdBy

                                        if ((defaultDate == null || latestDate!!.before(
                                                channelCreatedDate
                                            )) && channel.createdBy.equals("Vivek Mishra")
                                        ) {
                                            latestDate = channelCreatedDate
                                            latestChannelDescriptor = channel
                                            defaultDate = channelCreatedDate
                                        }

                                    }

                                    if (latestDate != null && latestChannelDescriptor != null) {
                                        latestChannelDescriptor!!.getChannel(object :
                                            CallbackListener<Channel?>() {

                                            override fun onSuccess(p0: Channel?) {
                                                if (alert == null)
                                                {
                                                    if(BaseApplication.instance.p0==null)
                                                    openDialog(p0)
                                                    else
                                                        openDialog(BaseApplication.instance.p0)
                                                }
                                            }
                                        })
                                    }
                                }

                            })
                }, 1000)


            }
        }

    }



  /*  fun chat(){
        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorService.vibrate(50000)
        mediaPlayer=MediaPlayer.create(ApplicationUtil.getContext(), R.raw.sound)
        //  mediaPlayer?.prepareAsync()
        mediaPlayer?.start()
        val chatId =intent?.getStringExtra("chatId")
        val msg =intent?.getStringExtra("msg")
        val title =intent?.getStringExtra("title")
        AwesomeDialog.build(this)
            .title(
                title.toString(),
                titleColor = ContextCompat.getColor(this, android.R.color.white)
            )
            .body(
                msg.toString(),
                color = ContextCompat.getColor(this, android.R.color.white)
            )
            .background(R.drawable.chat_notification_bg)
            .onNegative("Accept Chat Request ", buttonBackgroundColor = R.drawable.button_rounded_corners_green
            ) {
                val ussreID = intent?.getStringExtra("UserID")
                val userName = intent?.getStringExtra("userName")
                val duration = intent?.getStringExtra("Duration")

                joinStatus(chatId,"accept")
                val intent = Intent(this,AgoraMsgActivity::class.java)
                intent.putExtra("userId",ussreID)
                intent.putExtra("chatId",chatId)
                intent.putExtra("UserName",userName)
                intent.putExtra("Duration", duration)
                startActivity(intent)
                mediaPlayer?.release()
                vibratorService.cancel()
                // v.cancel()
                ApplicationConstant.ChatNotification = false
            }
            .onPositive("Deny Chat Request", buttonBackgroundColor = R.drawable.layout_rounded_cancel) {
                Log.d("TAG", "positive ")


                joinStatus(chatId,"cancel")
                Toast.makeText(this,"Chat Deny ",Toast.LENGTH_SHORT).show()
                //   v.cancel()
                mediaPlayer?.release()
                vibratorService.cancel()
                ApplicationConstant.ChatNotification = false

            }

            .position(AwesomeDialog.POSITIONS.CENTER)
            .setCancelable(false)

    }*/

       fun getCurrentActivity(){
           val am: ActivityManager =
               this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
           val taskInfo: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
           val currentActivity = taskInfo[0].topActivity!!.getClassName()
           Log.d("CURRENT Activity ", currentActivity)

          // BaseApplication.instance.showToast(MainActivity::class.java.simpleName )
       }

    fun  chat(){
        runOnUiThread {
             val pause = false
            NotificationManagerCompat.from(this).cancel(1)
            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(30000)
            mediaPlayer=MediaPlayer.create(ApplicationUtil.getContext(), R.raw.sound)
            //  mediaPlayer?.prepareAsync()

           /* if(pause){
                mediaPlayer!!.release()
            }
            else{
                mediaPlayer!!.start();
            }
            if(mediaPlayer!!.isPlaying())
            {
                //stop or pause your media player mediaPlayer.stop(); or mediaPlayer.pause();
                mediaPlayer!!.stop()
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                //audio is stopped here
            }
            else
            {
                mediaPlayer!!.start();


            }*/
            mediaPlayer?.start()
            val chatId =intent?.getStringExtra("chatID")
            val msg =intent?.getStringExtra("msg")
            val title =intent?.getStringExtra("title")
           // BaseApplication.instance.showToast("chat_idMainActivity>>>>>>>> $chatId")
            var dialog= Dialog(myActivity)
            dialog.setContentView(R.layout.chat_notification_popup)
            dialog.tv_title.text=title
            dialog.tv_msg.text=msg
            dialog.setCancelable(false)
            dialog.btn_accpt.setOnClickListener {
                val ussreID = intent?.getStringExtra("UserID")
                val userName = intent?.getStringExtra("userName")
                val duration = intent?.getStringExtra("Duration")

                joinStatus(chatId,"accept")
                val agoraintent = Intent(this,AgoraMsgActivity::class.java)
                agoraintent.putExtra("userId",ussreID)
                agoraintent.putExtra("chatId",chatId)
                agoraintent.putExtra("UserName",userName)
                agoraintent.putExtra("Duration", duration)
                startActivity(agoraintent)
                mediaPlayer?.release()
                vibratorService.cancel()
                dialog.dismiss()
                // v.cancel()
                ApplicationConstant.ChatNotification = false
            }
            dialog.btn_dny.setOnClickListener {
                joinStatus(chatId,"cancel")
                Toast.makeText(this,"Chat Deny ",Toast.LENGTH_SHORT).show()
                mediaPlayer?.release()
                vibratorService.cancel()
                ApplicationConstant.ChatNotification = false
                dialog.dismiss()
            }
            val metrics = DisplayMetrics() //get metrics of screen

            myActivity.windowManager.defaultDisplay.getMetrics(metrics)
            val height = (metrics.heightPixels * 0.5).toInt() //set height to 90% of total

            val width = (metrics.widthPixels * 0.9).toInt() //set width to 90% of total


            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // dialog.window.setLayout(((getWidth(this.context) / 100) * 90), LinearLayout.LayoutParams.MATCH_PARENT);
            dialog.show()

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if( ApplicationConstant.ChatNotification == true){
                    dialog.dismiss()
                    timer.cancel()
                    }
                }
            }, 90000)
        }

    }


    fun returnToChat(){
        view_return_to_chat.visibility = View.VISIBLE

    }

     fun callSaveTokenApi() {

        val userID = SharedPreferenceUtils.readString(
            ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
        var token = SharedPreferenceUtils.readString(
            ApplicationConstant.DEVICETOKEN, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))

       BaseApplication.instance.showToast("cllll")
        mViewModel.callSaveToken(ApplicationConstant.SAVE_TOKEN, ""+token, ""+userID)
            .observe(this, object : Observer<TokenModel> {
                override fun onChanged(resp: TokenModel?) {
//                    showLoadingView(false, binding.loadingView.loadingIndicator, binding.loadingView.container)
                    if (resp != null) {

                    } else {

                    }
                }

            })

    }

    fun joinStatus(chatId:String?,status:String?){
        mHelperViewModel.joinApi(chatId!!,status)
        mHelperViewModel.joinmessage.observe(this, Observer {

        })
    }
    fun checkAppUpdate() {
        // Creates instance of the manager.
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks whether the platform allows the specified type of update,
// and checks the update priority.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    500
                )
            }
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

           if(requestCode == 1) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                   // startService(Intent(myActivity, WidgetService::class.java))

                } else {
                    BaseApplication.instance.showToast ("Permission is not granted!")
                }
            }


        if (requestCode == 500) {
            if (resultCode != RESULT_OK) {
                LogUtils.d("Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }

    }

    fun checkForAppUpdate(mContext: FragmentActivity) {



        try {
            if (mContext != null && mHelperViewModel != null) {

                checkAppVersionAndShowUpdateDialogue(object : OkValueCallback {
                    override fun onValueReceived(value: String?) {
                        if (value.equals("1", false)) {
                            AwesomeDialog.build(mContext)
                                .title("New Version available")
                                .body("There is newer version of this application available, click UPDATE to update now?")
                                .icon(R.drawable.ic_congrts)
                                .onPositive(
                                    "Update Now ",
                                    buttonBackgroundColor = R.drawable.layout_rounded_cancel,
                                    textColor = ContextCompat.getColor(
                                        mContext,
                                        android.R.color.white
                                    )
                                )
                                {
                                    val appPackageName = packageName // package name of the app
                                    try {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=$appPackageName")
                                            )
                                        )
                                    } catch (anfe: android.content.ActivityNotFoundException) {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                            )
                                        )
                                    }
                                }
                                .onNegative(
                                    "No, Thanks",
                                    buttonBackgroundColor = R.drawable.layout_rounded,
                                    textColor = ContextCompat.getColor(
                                        mContext,
                                        android.R.color.white
                                    )
                                )
                                {}
                                .position(AwesomeDialog.POSITIONS.CENTER)
                                .setCancelable(false)
                        }
                    }
                }, mContext)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun checkAppVersionAndShowUpdateDialogue(mCallback: OkValueCallback, mContext: Context?) {

        try {
            if (mContext != null) {
                if (mHelperViewModel != null) {
                    mHelperViewModel.getAppVersion()
                    mHelperViewModel.appVersionReq.observeForever { val mResponse = it
                        if (mResponse != null && mResponse.data != null) {
                            val versionOfAppOnServer = mResponse.data?.get(0).astro_version

                            if (ApplicationConstant.CURRENT_APP_VERSION < versionOfAppOnServer) {
                                mCallback.onValueReceived("1")
                            }

                        }
                    }


                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    override fun onDestroy() {
        super.onDestroy()

    }

    val myApplication: BaseApplication
        get() {
            return mBaseApplication
        }

    val myActivity: MainActivity
        get() {
            return this
        }

    fun getToolbar(): Toolbar {
        return toolbar
    }

    fun checkLogin(toolbar: Toolbar, navView: NavigationView, drawerLayout: DrawerLayout) {
        val username = SharedPreferenceUtils.readString(
            ApplicationConstant.USERNAME, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
        val password = SharedPreferenceUtils.readString(
            ApplicationConstant.PASSWORD, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
        val name = SharedPreferenceUtils.readString(
            ApplicationConstant.NAME, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
        val email = SharedPreferenceUtils.readString(
            ApplicationConstant.EMAIL, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
        val phoneNumber = "+" + "${
            SharedPreferenceUtils.readString(
                ApplicationConstant.PHONECODE, "",
                SharedPreferenceUtils.getSharedPref(
                    ApplicationUtil.getContext()
                )
            )
        }" + " " + "${
            SharedPreferenceUtils.readString(
                ApplicationConstant.MOBILE, "",
                SharedPreferenceUtils.getSharedPref(
                    ApplicationUtil.getContext()
                )
            )
        }"
        val phCode = SharedPreferenceUtils.readString(
            ApplicationConstant.PHONECODE, "", SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )

        if (username.isNullOrEmpty()) {
            drawerLayout.closeDrawers()
            navController.navigate(R.id.nav_login)
        } else {
            startLoginToChat()
            navView.getHeaderView(0).nav_txv_name.text = name
            navView.getHeaderView(0).nav_txv_name.setOnClickListener {
                navigateToProfileFragment(drawerLayout)
                toolbar.title = "Edit profile"
            }
            navView.getHeaderView(0).nav_txv_email.text = username
            navView.getHeaderView(0).nav_txv_email.setOnClickListener {
                navigateToProfileFragment(drawerLayout)
                toolbar.title = "Edit profile"
            }
            navView.getHeaderView(0).nav_txv_phone.text = phoneNumber
            val versionCode = BuildConfig.VERSION_CODE
            val versionName = BuildConfig.VERSION_NAME
            navView.getHeaderView(0).nav_tv_version.text = "Version:  $versionName"

            navView.getHeaderView(0).nav_txv_phone.setOnClickListener {
                navigateToProfileFragment(drawerLayout)
                toolbar.title = "Edit profile"
            }

               // navView.menu.findItem(R.id.nav_chat_history).setVisible(false)
            navView.menu.getItem(8).subMenu.getItem(0)
                .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        drawerLayout.closeDrawers()
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.setType("text/plain")
                        val text =
                            "https://play.google.com/store/apps/details?id=tta.destinigo.talktoastro&hl=en".toString()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
                        startActivity(Intent.createChooser(shareIntent, "Share using"))
                        return true
                    }

                })
            /*navView.menu.getItem(8).subMenu.getItem(0)
                .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        SharedPreferenceUtils.clear()
                        return true
                    }

                })*/
        }
    }

    fun navigateToProfileFragment(drawerLayout: DrawerLayout) {
        drawerLayout.closeDrawers()
        navController.navigate(R.id.nav_txv_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

   /* override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this,
            R.id.nav_host_fragment
        ).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showProgressBar(message: String, cancellable: Boolean) {

        hud!!.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(message)
            .setCancellable(cancellable)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()

    }

    fun hideProgressBar() {
        hud!!.dismiss()

    }

    /**
     * Login to create chat client
     */

    fun startLoginToChat() {
        val userName = SharedPreferenceUtils.readString(
            ApplicationConstant.NAME, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()
            )
        )
        val certPinningChosen = true
        val realm = "us1"
        val ttl = "3000"

        SharedPreferenceUtils.put(
            "pinCerts",
            certPinningChosen,
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
        )
        SharedPreferenceUtils.put(
            "realm",
            realm,
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
        )
        SharedPreferenceUtils.put(
            "ttl",
            ttl,
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
        )
        val url = Uri.parse(BuildConfig.ACCESS_TOKEN_SERVICE_URL)
            .buildUpon()
            .appendQueryParameter("identity", userName)
            .appendQueryParameter("realm", realm)
            .appendQueryParameter("ttl", ttl)
            .build()
            .toString()
        LogUtils.d("url string : $url")
        BaseApplication.instance.basicClient.login(userName!!, certPinningChosen, realm, url, this)

    }

    fun updateTokenForChatClient() {
        isTokenUpdatedCalled = true
        if (BaseApplication.instance.basicClient.chatClient == null) {
            startLoginToChat()
        } else {
            BaseApplication.instance.basicClient.updateToken()
        }
    }

    override fun onLoginStarted() {
        LogUtils.d("Log in started")
    }

    override fun onLoginFinished() {
        LogUtils.d("Login finished")
        //BaseApplication.instance.showToast("Login finished" )
        if (isTokenUpdatedCalled) {
            val intent = Intent(ApplicationUtil.getContext(), ChannelActivity::class.java)
            startActivity(intent)
        }
        isTokenUpdatedCalled = false
        BaseApplication.instance.basicClient.chatClient?.setListener(this)
    }

    override fun onLoginError(errorMessage: String) {
        BaseApplication.instance.showToast("Error logging in : " + errorMessage, Toast.LENGTH_LONG)
        isTokenUpdatedCalled = false
    }

    override fun onLogoutFinished() {
        BaseApplication.instance.showToast("Log out finished")
        isTokenUpdatedCalled = false
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(ApplicationUtil.getContext())
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(
                    myActivity,
                    resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                    .show()
            } else {
                LogUtils.d("This device is not supported.")
            }
            return false
        }
        return true
    }

    companion object {
        lateinit var instance: MainActivity public set
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }


    /**
     * Chat client listeners
     */

    override fun onChannelAdded(p0: Channel?) {
        Log.d("MainActivity_Twillo", "onChannelAdded")
    }

    override fun onChannelDeleted(p0: Channel?) {
        Log.d("MainActivity_Twillo", "onChannelDeleted")
    }

    override fun onChannelInvited(p0: Channel?) {

        Log.d("MainActivity_Twillo", "onChannelInvited")
        if (p0?.status == Channel.ChannelStatus.JOINED) {
            return
        }
        if (mp.isPlaying) {
            return
        }
        mp.start()

        if (BaseApplication.instance.numRunningActivities == 0) {
           //save channel in base application
            BaseApplication.instance.save(p0)
        }



      else{
            Prefences.setShowDialog(applicationContext, true)

            if (!this@MainActivity.isFinishing()) {

                openDialog(p0)


            }
        }


//        runOnUiThread({
//            object : Runnable {
//                override fun run() {
//                    if (!isFinishing) {
//
//
//
//                    }
//                }
//            }
//        })


    }

    private fun createLocalPush() {

        if (FCMListenerService.mp.isPlaying) {
            return
        }
        FCMListenerService.mp.start()

        val client = BaseApplication.instance.basicClient.chatClient
        var title = "You Received chat"

        // Set up action Intent
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("isFromChat", true);

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "tta_channel_id"
        val channelName: CharSequence = "TTA Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(title)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(Color.rgb(214, 10, 37))
           // .addAction()
            .build()

        notificationManager.notify(0, notification)
    }

    private fun openDialog(p0: Channel?) {

        try {
            Handler().postDelayed({
                alert = alert(R.string.channel_invite_message, R.string.channel_invite) {
                    customView {
                        verticalLayout {
                            textView {
                                text = "You are invited to chat by ${p0?.createdBy}"
                                padding = dip(10)
                            }.lparams(width = matchParent)
                            positiveButton(R.string.join) {
                                Prefences.setShowDialog(applicationContext, false)
                                mp.stop()
                                p0?.join(
                                    ToastStatusListener(
                                        "",
                                        "Failed to join chat",
                                        {},
                                        {
                                            if (p0?.status == Channel.ChannelStatus.JOINED) {
                                                Handler().postDelayed({
                                                    mp.stop()
                                                    ChannelModel(p0).getChannel(
                                                        ChatCallbackListener<Channel>() {
                                                            startActivity<MessageActivity>(
                                                                Constants.EXTRA_CHANNEL to it,
                                                                Constants.EXTRA_CHANNEL_SID to it.sid
                                                            )
                                                        })
                                                }, 1000)
                                            }
                                        })
                                )
                            }
                            negativeButton(R.string.decline) {
                                Prefences.setShowDialog(applicationContext, false)

                                mp.stop()
                                p0?.declineInvitation(
                                    ToastStatusListener(
                                        "Successfully declined channel invite",
                                        "Failed to decline channel invite"
                                    )
                                )
                            }
                        }
                    }
                };
                alert!!.show()
            }, 2000)
        } catch (e: Exception) {
        } finally {
        }

    }

    override fun onChannelJoined(p0: Channel?) {

        Log.d("MainActivity_Twillo", "onChannelJoined")
    }

    override fun onChannelSynchronizationChange(p0: Channel?) {
        Log.d("MainActivity_Twillo", "onChannelSynchronizationChange")
    }

    override fun onChannelUpdated(p0: Channel?, p1: Channel.UpdateReason?) {
        Log.d("MainActivity_Twillo", "onChannelUpdated")
    }

    override fun onClientSynchronization(p0: ChatClient.SynchronizationStatus?) {
        Log.d("MainActivity_Twillo", "onClientSynchronization")
    }

    override fun onConnectionStateChange(p0: ChatClient.ConnectionState?) {

        Log.d("MainActivity_Twillo", "onConnectionStateChange")
    }

    override fun onError(p0: ErrorInfo?) {
        Log.d("MainActivity_Twillo", "onError")
    }

    override fun onRemovedFromChannelNotification(p0: String?) {
        Log.d("MainActivity_Twillo", "onRemovedFromChannelNotification")
    }

    override fun onUserSubscribed(p0: User?) {
        Log.d("MainActivity_Twillo", "onUserSubscribed")
    }

    override fun onUserUnsubscribed(p0: User?) {
        Log.d("MainActivity_Twillo", "onUserUnsubscribed")
    }

    override fun onUserUpdated(p0: User?, p1: User.UpdateReason?) {
        Log.d("MainActivity_Twillo", "onUserUpdated")
    }

    override fun onTokenAboutToExpire() {
        Log.d("MainActivity_Twillo", "onTokenAboutToExpire")
    }

    override fun onAddedToChannelNotification(p0: String?) {
        Log.d("MainActivity_Twillo", "onAddedToChannelNotification")
    }

    override fun onInvitedToChannelNotification(p0: String?) {
        Log.d("MainActivity_Twillo", "onInvitedToChannelNotification")
    }

    override fun onNewMessageNotification(p0: String?, p1: String?, p2: Long) {
        Log.d("MainActivity_Twillo", "onNewMessageNotification")
    }

    override fun onNotificationFailed(p0: ErrorInfo?) {
        Log.d("MainActivity_Twillo", "onNotificationFailed")
    }

    override fun onTokenExpired() {
        Log.d("MainActivity_Twillo", "onTokenExpired")
    }

    override fun onNotificationSubscribed() {
        Log.d("MainActivity_Twillo", "onNotificationSubscribed")
    }


    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(mApplication) as T
        }
    }



    override fun onBackPressed() {
        when (navController.getCurrentDestination()!!.getId()) {

            R.id.nav_logout -> {
               // onWarningAlertDialog(this, "Alert", "Do you want to close this application ?")
                finish()
            }
            R.id.nav_home -> {
                finish()
                System.exit(0)


            }
        }

        super.onBackPressed()
    }



    private fun onWarningAlertDialog(mainActivity: MainActivity, s: String, s1: String) {

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(/*""*/s1)
            .setCancelable(false)
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()
    }
   /* override fun onBackPressed() {

        val f: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        *//**
         * Compare the instances based on fragment name to change the action bar
         *//*

        *//**
         * Compare the instances based on fragment name to change the action bar
         *//*
        if (f is LoginFragment) {
            finish()
            System.exit(0)
        }
        super.onBackPressed()
    }*/

}
