package tta.astrologerapp.talktoastro.ui.home

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomedialog.*
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.balloon
import kotlinx.android.synthetic.main.chat_notification_popup.*
import kotlinx.android.synthetic.main.custom_dialogs.*
import kotlinx.android.synthetic.main.custom_dialogs.tv_title
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.WidgetService
import tta.astrologerapp.talktoastro.adapter.CallHistoryAdapter
import tta.astrologerapp.talktoastro.agora.chat.AgoraMsgActivity
import tta.astrologerapp.talktoastro.agora.chat.ViewChatHistory
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.AstroCallParaFactory
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils


class HomeFragment : tta.astrologerapp.talktoastro.BaseFragment() {
    override val layoutResId: Int
        get() = R.layout.fragment_home

    override fun getToolbarId(): Int {
        return 0
    }

    private lateinit var homeViewModel: HomeViewModel
    private var callHistoryAdapter: CallHistoryAdapter? = null
    private val Astrocallballoon by balloon(AstroCallParaFactory::class)
    private var isChecked: Boolean = false
    private var type: String = ""
    private var status: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this,
                MyViewModelFactory(
                    tta.astrologerapp.talktoastro.BaseApplication.instance
                )
            ).get(HomeViewModel::class.java)
        val root = inflater.inflate(layoutResId, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        myActivity.startLoginToChat()
        refreshPage()
    }

    fun refreshPage() {
        homeViewModel?.arrayMutableCallHistory?.value?.clear()
        if (callHistoryAdapter != null) {
            callHistoryAdapter!!.notifyDataSetChanged()
            showProgressBar("Loading")
            txv_wallet_balance.text = SharedPreferenceUtils.readString(
                ApplicationConstant.BALANCE, "",
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
            val astroId = SharedPreferenceUtils.readString(
                ApplicationConstant.USERID, "", SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))

            getAstroStatus(astroId!!)
            updateUserWallet(astroId!!)
            getCallHistory(astroId)
        }
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar("Loading...")
        val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        val notificationIcon:ImageView=activity!!.findViewById(R.id.iv_notofication)
        val astroID = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
        refreshButton.visibility = View.VISIBLE
        notificationIcon.visibility=View.VISIBLE
        toolbar.title = "Astrologer - ${tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.NAME, "",
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))}"
        val phoneCode = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.PHONECODE, "",
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
        val astroId = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
        if (phoneCode == "91") {
            txv_wallet_balance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupee_black, 0, 0, 0)
        }
        else {
            txv_wallet_balance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
        }
        txv_wallet_balance.text = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, "",
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))

        txv_callhistory.setTextColor(resources.getColor(R.color.black))
        txv_chathistory.setTextColor(resources.getColor(R.color.gray_light))

        getAstroStatus(astroId!!)
        updateUserWallet(astroId!!)
        getCallHistory(astroId!!)

        getStatusObservers()
        getAstroCallParameter(astroId!!)

        txv_callhistory.setOnClickListener {
            getCallHistory(astroId)
            txv_callhistory.setTextColor(resources.getColor(R.color.black))
            txv_chathistory.setTextColor(resources.getColor(R.color.gray_light))
        }
        txv_chathistory.setOnClickListener {
            getChatHistory(astroId!!)
            txv_callhistory.setTextColor(resources.getColor(R.color.gray_light))
            txv_chathistory.setTextColor(resources.getColor(R.color.black))
        }

        /*iv_info.setOnClickListener {

            this.Astrocallballoon?.showAlignBottom(it)

        }*/
        ll_call_pickup.setOnClickListener {
            showDialog("Call Pickup rate : ","Total number of calls picked out of total incoming calls")
        }

        ll_repeat_order.setOnClickListener {
            showDialog("Repeat order :  ","On average number of repeat calls by unique users. ")
        }

        off_button.setOnClickListener{
            //offlineState()
            changeUserStatus(false, astroId, ApplicationConstant.CALL_STRING)
        }

        on_button.setOnClickListener{
            //onlineState()
            changeUserStatus(true, astroId, ApplicationConstant.CALL_STRING)
        }

        off_button_chat.setOnClickListener {
            changeUserStatus(false, astroId, ApplicationConstant.CHAT_STRING)
        }
        on_button_chat.setOnClickListener {
            changeUserStatus(true, astroId, ApplicationConstant.CHAT_STRING)
        }

        refreshButton.setOnClickListener {
            showProgressBar("Loading...")
            refreshPage()
        }

        notificationIcon.setOnClickListener {
            val intent =
                Intent(
                    ApplicationUtil.getContext(),
                    tta.astrologerapp.talktoastro.activity.NotificationActivity::class.java
                )
            startActivity(intent)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(myActivity)) {
             //  myActivity.startService(Intent(myActivity, WidgetService::class.java))
            } else {
                showOverlayDialogPermission()
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(myActivity)) {
            //  myActivity.startService(Intent(myActivity, WidgetService::class.java))
        } else {
            showOverlayDialogPermission()
        }

    }
     fun showOverlayDialogPermission(){
         AwesomeDialog.build(myActivity)
             .title("Permission Required",titleColor = ContextCompat.getColor(activity, android.R.color.black))
             .body("To Get Chat Request From User Please Click on OK button",color = ContextCompat.getColor(activity, android.R.color.holo_orange_dark))
             .icon(R.mipmap.ic_launcher_round)
             .onPositive("Okay",buttonBackgroundColor = R.drawable.button_rounded_corners_orange,
                 textColor = ContextCompat.getColor(activity, android.R.color.black)) {
                 startManageDrawOverlaysPermission()
             }
             .position(
                 AwesomeDialog.POSITIONS.CENTER
             )
     }

    private fun startManageDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${applicationContext.packageName}")
            ).let {
                startActivityForResult(it, 1)
            }
        }
    }

    fun getStatusObservers() {
        homeViewModel._success.observe(this, Observer {
            hideProgressBar()
            if (it == 1) {
                Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "Status changed successfully", Toast.LENGTH_LONG)
                    .show()
                if (isChecked) {
                    if (type == ApplicationConstant.CHAT_STRING) {
                        txv_status_val_chat.text = "online"
                        txv_status_val_chat.setTextColor(resources.getColor(R.color.color_green_dark))
                        onlineState(ApplicationConstant.CHAT_STRING)
                    } else {
                        txv_status_val.text = "online"
                        txv_status_val.setTextColor(resources.getColor(R.color.color_green_dark))
                        onlineState(ApplicationConstant.CALL_STRING)
                    }
                } else {
                    if (type == ApplicationConstant.CHAT_STRING) {
                        txv_status_val_chat.text = "offline"
                        txv_status_val_chat.setTextColor(resources.getColor(R.color.black))
                        offlineState(ApplicationConstant.CHAT_STRING)
                    } else {
                        txv_status_val.text = "offline"
                        txv_status_val.setTextColor(resources.getColor(R.color.black))
                        offlineState(ApplicationConstant.CALL_STRING)
                    }
                }
                SharedPreferenceUtils.put(
                    ApplicationConstant.STATUS, status, tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "change status status", Toast.LENGTH_LONG)
                    .show()
                //refreshFragment()
            } else {
                Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "Failed to change status", Toast.LENGTH_LONG)
                    .show()
                txv_status_val.text = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                    tta.astrologerapp.talktoastro.util.ApplicationConstant.STATUS, "online", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                        tta.astrologerapp.talktoastro.util.ApplicationConstant.STATUS, "online", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())) == "online") {
                    txv_status_val.setTextColor(resources.getColor(R.color.color_green_dark))
                } else {
                    txv_status_val.setTextColor(resources.getColor(R.color.black))
                }
                //refreshFragment()
            }
        })
        homeViewModel.responseDidFailed.observe(this, Observer {
            hideProgressBar()
            Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), it, Toast.LENGTH_LONG)
                .show()
            txv_status_val.text = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                tta.astrologerapp.talktoastro.util.ApplicationConstant.STATUS, "online", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
            if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                    tta.astrologerapp.talktoastro.util.ApplicationConstant.STATUS, "online", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())) == "online") {
                txv_status_val.setTextColor(resources.getColor(R.color.color_green_dark))
            } else {
                txv_status_val.setTextColor(resources.getColor(R.color.black))
            }
            refreshFragment()
        })
    }

    fun offlineState(type: String) {
        if (type == ApplicationConstant.CALL_STRING) {
            on_button.setBackgroundColor(resources.getColor(R.color.white))
            on_button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_white, 0, 0, 0)
            on_button.setTextColor(resources.getColor(R.color.black))
            off_button.setBackgroundColor(resources.getColor(R.color.gray_light))
            off_button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_corners_gray, 0, 0, 0)
            off_button.setTextColor(resources.getColor(R.color.white))
            off_button.setText("Offline")
            off_button.visibility = View.VISIBLE

            textView.setTextColor(resources.getColor(R.color.gray))
            textView.text = "Currently you are Offline"
        } else {
            on_button_chat.setBackgroundColor(resources.getColor(R.color.white))
            on_button_chat.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_white, 0, 0, 0)
            on_button_chat.setTextColor(resources.getColor(R.color.black))
            off_button_chat.setBackgroundColor(resources.getColor(R.color.gray_light))
            off_button_chat.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_corners_gray, 0, 0, 0)
            off_button_chat.setTextColor(resources.getColor(R.color.white))
            off_button_chat.setText("Offline")
            off_button_chat.visibility = View.VISIBLE
        }

    }

    fun onlineState(type: String) {
        if (type == ApplicationConstant.CALL_STRING) {
            on_button.setBackgroundColor(resources.getColor(R.color.color_green_color))
            on_button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_corners_green, 0, 0, 0)
            on_button.setTextColor(resources.getColor(R.color.white))
            off_button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_white, 0, 0, 0)
            off_button.setTextColor(resources.getColor(R.color.black))
            on_button.setText("Online")
            off_button.visibility = View.VISIBLE

            textView.setTextColor(resources.getColor(R.color.color_button_green))
            textView.text = "Currently you are Online"
        } else {
            on_button_chat.setBackgroundColor(resources.getColor(R.color.color_green_color))
            on_button_chat.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_corners_green, 0, 0, 0)
            on_button_chat.setTextColor(resources.getColor(R.color.white))
            off_button_chat.setCompoundDrawablesWithIntrinsicBounds( R.drawable.button_rounded_white, 0, 0, 0)
            off_button_chat.setTextColor(resources.getColor(R.color.black))
            on_button_chat.setText("Online")
            off_button_chat.visibility = View.VISIBLE
        }

    }

    fun busyState(type: String) {
        if (type == ApplicationConstant.CALL_STRING) {
            on_button.setBackgroundColor(resources.getColor(R.color.accent))
            on_button.setTextColor(resources.getColor(R.color.white))
            on_button.setText("Busy")
            off_button.visibility = View.GONE
            textView.setTextColor(resources.getColor(R.color.accent))
            textView.text = "Currently you are Busy"
        } else {
            on_button_chat.setBackgroundColor(resources.getColor(R.color.accent))
            on_button_chat.setTextColor(resources.getColor(R.color.white))
            on_button_chat.setText("Busy")
            off_button_chat.visibility = View.GONE
        }


    }

    fun getAstroStatus(astroId: String) {
        var json = JSONObject()
        json.put("astroID",astroId)
        homeViewModel?.getAstroStatus(json)
        homeViewModel?.astroStatus?.observe(this, Observer {
            SharedPreferenceUtils.put(
                ApplicationConstant.STATUS, it, SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
            myActivity.runOnUiThread {
                if (it == ApplicationConstant.ONLINE) {
                    txv_status_val.text = ApplicationConstant.ONLINE
                    txv_status_val.setTextColor(resources.getColor(R.color.color_green_dark))
                    onlineState(ApplicationConstant.CALL_STRING)
                    constraint_switch.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight_1))
                    txv_status.setTextColor(resources.getColor(R.color.black))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.ONLINE, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                } else if (it == ApplicationConstant.BUSY){
                    txv_status_val.text = ApplicationConstant.BUSY
                    txv_status_val.setTextColor(resources.getColor(R.color.white))
//                    switch_status.visibility = View.GONE
                    busyState(ApplicationConstant.CALL_STRING)
                    constraint_switch.setBackgroundColor(resources.getColor(R.color.accent))
                    txv_status.setTextColor(resources.getColor(R.color.white))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.BUSY, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                }
                else {
                    txv_status_val.text = ApplicationConstant.OFFLINE
                    txv_status_val.setTextColor(resources.getColor(R.color.black))
//                    switch_status.isChecked = false
//                    switch_status.visibility = View.VISIBLE
                    offlineState(ApplicationConstant.CALL_STRING)
                    constraint_switch.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight_1))
                    txv_status.setTextColor(resources.getColor(R.color.black))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.OFFLINE, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                }
            }
        })
        getAstroChatStatus()
    }

    fun getAstroChatStatus() {
        homeViewModel?.astroChatStatus?.observe(this, Observer {
            SharedPreferenceUtils.put(
                ApplicationConstant.STATUS, it, SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
            myActivity.runOnUiThread {
                if (it == ApplicationConstant.ONLINE) {
                    txv_status_val_chat.text = ApplicationConstant.ONLINE
                    txv_status_val_chat.setTextColor(resources.getColor(R.color.color_green_dark))
                    onlineState(ApplicationConstant.CHAT_STRING)
                    constraint_switch_chat.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight_1))
                    txv_status_chat.setTextColor(resources.getColor(R.color.black))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.ONLINE, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                } else if (it == ApplicationConstant.BUSY){
                    txv_status_val_chat.text = ApplicationConstant.BUSY
                    txv_status_val_chat.setTextColor(resources.getColor(R.color.white))
                    busyState(ApplicationConstant.CHAT_STRING)
                    constraint_switch_chat.setBackgroundColor(resources.getColor(R.color.accent))
                    txv_status_chat.setTextColor(resources.getColor(R.color.white))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.BUSY, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                }
                else {
                    txv_status_val_chat.text = ApplicationConstant.OFFLINE
                    txv_status_val_chat.setTextColor(resources.getColor(R.color.black))
                    offlineState(ApplicationConstant.CHAT_STRING)
                    constraint_switch_chat.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight_1))
                    txv_status_chat.setTextColor(resources.getColor(R.color.black))
                    SharedPreferenceUtils.put(
                        ApplicationConstant.STATUS, ApplicationConstant.OFFLINE, SharedPreferenceUtils.getSharedPref(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                }
            }
        })
    }

    fun changeUserStatus(isCheckedVal: Boolean, astroId: String, typeVal: String) {
        showProgressBar("Please wait...", true)
        var json = JSONObject()
        if (isCheckedVal) {
            status = "online"

        } else {
            status = "offline"
        }
        isChecked = isCheckedVal
        type = typeVal

        if (type == ApplicationConstant.CHAT_STRING) {
            json.put("chatStatus", status)
            json.put("astroID",astroId)
            homeViewModel.changeChatStatus(json)
        } else {
            json.put("phoneStatus", status)
            json.put("astroID",astroId)
            homeViewModel.changeStatus(json)
        }
    }

    fun getCallHistory(astroId: String) {
        showProgressBar("Loading")
        val json = JSONObject()
        json.put("userID",astroId)
        homeViewModel.getCallHistory(json)
        homeViewModel.arrayMutableCallHistory.observe(this, Observer {
            Handler().postDelayed({
               // hideProgressBar()
            },2000)
            myActivity.runOnUiThread {
                callHistoryAdapter =
                    tta.astrologerapp.talktoastro.adapter.CallHistoryAdapter(it!!, null, false){chatHistoryModel, i ->

                    }
                call_history_list.layoutManager = LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
                call_history_list.adapter = callHistoryAdapter
                call_history_list.hasFixedSize()
                hideProgressBar()
            }
        })
    }

    fun getAstroCallParameter(astroId: String){
        val json = JSONObject()
        json.put("astroID",astroId)
        Log.e("TAG","json=== : "+json);
        homeViewModel.getAstroCallParameter(json)
        homeViewModel.astroCallParameter.observe(this, Observer {
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.put(
                tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, it,
                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
          //  val tv_callPickUp: TextView = Astrocallballoon!!.getContentView().findViewById(R.id.tv_call_pickup_rate)
          //  val tv_repeat: TextView = Astrocallballoon!!.getContentView().findViewById(R.id.tv_reapt_oder)
            tv_call_pickup_rate.text=it.call_pickup_rate + " "
            tv_reapt_oder.text=it.repeat_order + " "
           // tv_callPickUp.text=it.call_pickup_rate
           // tv_repeat.text=it.repeat_order
           // txv_wallet_balance.text = it.call_pickup_rate
        })
    }

    fun  showDialog(title:String,desc:String){
        var dialog=Dialog(myActivity)
        dialog.setContentView(R.layout.custom_dialogs)
        dialog.tv_title.text=title
        dialog.tv_body.text=desc
        dialog.setCancelable(false)
        dialog.buttonOk.setOnClickListener { dialog.dismiss() }
        val metrics = DisplayMetrics() //get metrics of screen

        getActivity()!!.windowManager.defaultDisplay.getMetrics(metrics)
        val height = (metrics.heightPixels * 0.5).toInt() //set height to 90% of total

        val width = (metrics.widthPixels * 0.9).toInt() //set width to 90% of total


        dialog.window?.setLayout(width, height)
       // dialog.window.setLayout(((getWidth(this.context) / 100) * 90), LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show()
    }

    fun getChatHistory(astroId: String) {
        showProgressBar("Loading")
        val json = JSONObject()
        json.put("astroID",astroId)
        homeViewModel.getChatHistory(json)
        homeViewModel.arrayMutableChatHistory.observe(this, Observer {
            Handler().postDelayed({hideProgressBar()},2000)
            myActivity.runOnUiThread {
                callHistoryAdapter =
                    tta.astrologerapp.talktoastro.adapter.CallHistoryAdapter(null, it!!, true){chatHistoryModel, i ->
                       // BaseApplication.instance.showToast(chatHistoryModel.status.toString())
                        if(chatHistoryModel.status.toString()=="Success" || chatHistoryModel.status.toString()=="ended" ) {

                            val intent = Intent(context, ViewChatHistory::class.java)
                            intent.putExtra("ChatID", chatHistoryModel.chatID)
                            startActivity(intent)
                        }
                        else {
                            BaseApplication.instance.showToast("chat, history not available")
                        }
                    }
                call_history_list.layoutManager = LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
                call_history_list.adapter = callHistoryAdapter
                call_history_list.hasFixedSize()
            }
        })
    }

    fun updateUserWallet (astroId: String) {
        val json = JSONObject()
        json.put("userID",astroId)
        homeViewModel.getWalletTransactions(json)
        homeViewModel.arrayMutableWalletViewModel.observe(this, Observer {
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.put(
                tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, it,
                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
            txv_wallet_balance.text = it
        })
    }

    fun refreshFragment() {
        if (getFragmentManager() != null) {

            getFragmentManager()?.beginTransaction()
                ?.detach(this)
                ?.attach(this)
                ?.commit();
        }
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return tta.astrologerapp.talktoastro.ui.home.HomeViewModel(mApplication) as T
        }
    }
}