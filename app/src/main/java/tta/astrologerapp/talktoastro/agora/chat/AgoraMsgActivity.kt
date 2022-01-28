package tta.astrologerapp.talktoastro.agora.chat

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.agora.rtm.*
import kotlinx.android.synthetic.main.activity_agora_msg.*
import org.json.JSONArray
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.MainActivity
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.chat.services.ChatFormViewModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.MarginItemDecorator
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.destinigo.talktoastro.agora.chat.ChatManager
import tta.destinigo.talktoastro.agora.chat.MessageAdapter
import tta.destinigo.talktoastro.agora.chat.MessageBean
import tta.destinigo.talktoastro.agora.chat.MessageUtils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class AgoraMsgActivity : AppCompatActivity() {
    private var mRtmClient: RtmClient? = null
    private var mClientListener: RtmClientListener? = null
    private var mMessageBeanList: ArrayList<MessageBean> = ArrayList<MessageBean>()
    private var adapter: MessageAdapter? = null
    private var  mRvMsgList: RecyclerView?=null
    private var mUserId:String? = ""
    private var mPeerId :String? = ""
    var mChatManager: ChatManager? =null
    var timer: CountDownTimer? = null
    var milliSecondsFinished: Long = 0L
    var milliSeconds: Long = 0L
    private lateinit var chatFormViewModel: ChatFormViewModel
    private var agoraMsgViewModel: AgoraMsgViewModel? = null
    lateinit var mBaseApplication: BaseApplication
    var token = ""
    private var chatId :String? = ""
    var title :String? ="Chat "
    var duration :String? =""
    var time: String ?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agora_msg)
      //  MainActivity.instance.joinStatus(chatId,"accept")
        if (application is BaseApplication) {
            mBaseApplication = application as BaseApplication
        }
        ApplicationConstant.ClassName =AgoraMsgActivity::class.java.simpleName
        mRvMsgList =findViewById(R.id.rv_message_list)
        agoraMsgViewModel = ViewModelProvider(this, MyViewModelFactory(myApplication)).get(AgoraMsgViewModel::class.java)
        val intent = intent

        mUserId  = SharedPreferenceUtils.readString(
                ApplicationConstant.USERID, "", SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))

         mPeerId= intent.getStringExtra("userId")
         chatId = intent.getStringExtra("chatId")
          joinStatus(chatId,"accept")
         duration = intent.getStringExtra("Duration")
       // BaseApplication.instance.showToast(duration.toString())
        if(intent.getStringExtra("UserName")!=null){
            title = intent.getStringExtra("UserName")
        }
        if (duration != null) {

            val s :Int = (duration!!.toString()).toInt()
            val durationMinutes = duration!!.toInt()
            milliSeconds = (s * 60 * 1000).toLong()
            setTimerForChat(milliSeconds)
        }


        var json = JSONObject()
        json.put("userID", mUserId)
        /*var isreturntoChat = intent.getStringExtra("returntochat")
        BaseApplication.instance.showToast("mdcmcm $mPeerId")
        if(isreturntoChat == "yes"){
            val messageListBean = getExistMessageListBean(mPeerId?.toString().toString())
            if (messageListBean != null) {
                mMessageBeanList.addAll(messageListBean.getMessageBeanList())
            }
        }
        else{

        }*/


        agoraMsgViewModel?.rtmToken(json)
        agoraMsgViewModel?.rtmToken?.observe(this, Observer {
            token = it
            agoraLogin()
        })
        agoraMsgViewModel?.mesageCheck?.observe(this, Observer {it ->
            if(it == "ended"){
                endChat("User not Reachable please click On Okay Button To End Chat ")
            }

        })



        mChatManager = BaseApplication.instance.chatManager
        mRtmClient = mChatManager!!.mrtmClient
        mClientListener = MyRtmClientListener()
        mChatManager!!.registerListener(mClientListener as MyRtmClientListener)
        setToolbar()
        getSupportActionBar()?.hide()
        setRecyclerView()
        btn_send.setOnClickListener {
            sendPeerMessage()
            ed_msg.setText("")
        }
        btn_endChat.setOnClickListener {
            endChat("Do you want to end chat?")
        }
     //   cancelAllNotifications()
       /* val localTime: LocalTime = LocalTime.now()
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
         time = localTime.format(dateTimeFormatter)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
          //  var answer: String =  current.format(formatter)
            time = current.format(formatter)
           // Log.d("answer",answer)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
           // val answer: String = formatter.format(date)
            time = formatter.format(date)
           // Log.d("answer",answer)
        }
       /* val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
       // val formattedDate: String = df.format(c.getTime())
        time =df.format(c.getTime())*/
        println("Currrent Date Time : $time")

    }
    fun joinStatus(chatId:String?,status:String?){
        agoraMsgViewModel!!.joinApi(chatId!!,status)
        agoraMsgViewModel!!.joinmessage.observe(this, Observer {

        })
    }
    private fun setTimerForChat(timerVal: Long) {
       runOnUiThread { milliSeconds = timerVal
           timer = object : CountDownTimer(timerVal, 1000) {
               override fun onTick(millisUntilFinished: Long) {
                   milliSecondsFinished = millisUntilFinished
                   tv_chatTimeValue.text = String.format(
                       "%02d:%02d",
                       (millisUntilFinished / 60000),
                       (millisUntilFinished % 60000 / 1000)
                   )
               }

               override fun onFinish() {
                   tv_chatTimeValue.text = "00"
                   milliSecondsFinished = 0L
                   val alertDialogBuilder =
                       android.app.AlertDialog.Builder(
                           this@AgoraMsgActivity
                       )

                   alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                       timer?.cancel()
                       val timeSpentInChat = milliSeconds - milliSecondsFinished
                       val min = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
                       val sec = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - (min * 60)
                       mRtmClient!!.logout(null)
                       finish()

                   }
                   // set dialog message
                   alertDialogBuilder
                       .setMessage("Chat Ended")
                       .setCancelable(false)
                   alertDialogBuilder.show()
               }
           }

       }
        timer?.start()

    }

    val myApplication: BaseApplication
        get() {
            return mBaseApplication
        }

    val myActivity: AgoraMsgActivity
        get() {
            return this
        }
    private fun cancelAllNotifications() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
    fun endChat(message:String){
        runOnUiThread {
            val alertDialogBuilder =
            android.app.AlertDialog.Builder(this)
            alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
            alertDialogBuilder.setPositiveButton("OK") { _, _ ->

                apiscall()
                val intent = Intent(ApplicationUtil.getContext(), MainActivity::class.java)
                /* intent.putExtra("iscoming","ChatScreen")
                 intent.putExtra("peerId", mPeerId)*/
                startActivity(intent)
                finish()
                mRtmClient!!.logout(null)
                //  mRtmClient!!.release()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }
            alertDialogBuilder.show() }


    }

    fun apiscall(){
        val jsonArray = JSONArray()

        for(message in mMessageBeanList) {
            // var i=0
            val json1 = JSONObject()
            if(message.beSelf==true ){
                json1.put("type", "astrologer")}
            else{
                json1.put("type", "user")
            }
            val msg: String = message.message?.text ?: "" ;
            Log.d("Message Array", "message====>"+msg)
            json1.put("text",msg)
            json1.put("sent_at",message.sentAt)
            //i++
            jsonArray.put(json1)
        }
        val json2 = JSONObject()
        json2.put("messages", jsonArray)
        agoraMsgViewModel?.saveChat(json2,chatId.toString())
        agoraMsgViewModel?.endchat(chatId.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        apiscall()
    }

    override fun onResume() {
        super.onResume()
        /*if (timer != null)
            timer!!.start()
*/
       /* val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                //call function
                checkjoinStatus()
                ha.postDelayed(this, 5000)
            }
        }, 5000)*/

    }

    fun checkjoinStatus(){

        agoraMsgViewModel?.checkAstroChatJoinStatus(chatId.toString())

    }


    fun setToolbar(){
        val toolbar_main_activity= findViewById<android.widget.Toolbar>(R.id.toolbar_AgoraMsg)
        toolbar_main_activity.title = title
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_main_activity.setNavigationOnClickListener {
            endChat("Do you want to end chat?")

        }
    }


 /*   private fun sendchatFormMessage(text: String) {
        *//* channel!!.messages.sendMessage(
             Message.options().withBody(text),
             ChatCallbackListener<Message>() {
                 //BaseApplication.instance.showToast("Successfully sent message");
                 adapter.notifyDataSetChanged()
                 messageInput.setText("")
             })*//*
        val message = mRtmClient!!.createMessage()
        message.text = text
        val messageBean = MessageBean(mUserId, message, true)
        mMessageBeanList.add(messageBean)
        adapter?.notifyItemRangeChanged(mMessageBeanList.size, 1)
        mRvMsgList?.scrollToPosition(mMessageBeanList.size - 1)


        sendmessage(message)
    }*/

   /* private fun setTimerForChat(timerVal: Long) {

        milliSeconds = timerVal
        LogUtils.d("Chat timer in seconds: $milliSeconds")

        val intent = Intent(this, BroadcastService::class.java)
        intent.putExtra("timer", milliSeconds)
        startService(intent)

        timer = object : CountDownTimer(timerVal, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                milliSecondsFinished = millisUntilFinished
                tv_chatTimeValue.text = String.format(
                    "%02d:%02d",
                    (millisUntilFinished / 60000),
                    (millisUntilFinished % 60000 / 1000)
                )
            }

            override fun onFinish() {
                tv_chatTimeValue.text = "00"
                milliSecondsFinished = 0L
                val alertDialogBuilder =
                    android.app.AlertDialog.Builder(
                        this@AgoraMessageActivity
                    )

                alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                    timer?.cancel()
                    val timeSpentInChat = milliSeconds - milliSecondsFinished
                    val min = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
                    val sec = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - (min * 60)
                    mRtmClient!!.logout(null)
                    sendChatLogOnChatEnd("$min:$sec")
                }
                // set dialog message
                alertDialogBuilder
                    .setMessage("Please recharge to chat. Your wallet is empty.")
                    .setCancelable(true)
                alertDialogBuilder.show()
            }
        }
//        timer?.start()

    }
    private fun sendChatLogOnChatEnd(duration: String) {
        var jsonObj = JSONObject()

        jsonObj.put(
            "userID",
            SharedPreferenceUtils.readString(
                ApplicationConstant.USERID,
                "",
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )!!
        )
        jsonObj.put("astroID", intent.getStringExtra(Constants.CHAT_ASTRO_ID))
        jsonObj.put("duration", duration)
        jsonObj.put("status", "success")
        jsonObj.put("chatID", intent.getStringExtra(Constants.CHAT_ID))
        jsonObj.put("sessionID", intent.getStringExtra(Constants.EXTRA_CHANNEL_SID))

        chatFormViewModel.sendChatLog(jsonObj)
        jsonObj.put("chatStatus", "online")
        chatFormViewModel.changeChatStatus(jsonObj)

        BaseApplication.instance.p0 = null

        val intent = Intent(ApplicationUtil.getContext(), ChannelActivity::class.java)
        startActivity(intent)

        finish()
    }*/
    //Login in agoraServer
    fun agoraLogin(){


        mRtmClient?.login(token,mUserId,object : ResultCallback<Void?> {
            override fun onSuccess(p0: Void?) {
                runOnUiThread {
                  //  Toast.makeText(this@AgoraMsgActivity, "login success    " , Toast.LENGTH_LONG).show()
                    Toast.makeText(this@AgoraMsgActivity, "Start Chat     " , Toast.LENGTH_LONG).show()
                    /* val i = Intent(myActivity, AgoraMessageActivity::class.java)
                     i.putExtra("userId",userId)
                     startActivity(i)*/
                }

            }

            override fun onFailure(errorInfo: ErrorInfo?) {
                runOnUiThread {
                   // Toast.makeText(this@AgoraMsgActivity, "login onFailure   " + errorInfo?.errorDescription, Toast.LENGTH_LONG).show()
                    Toast.makeText(this@AgoraMsgActivity, "chat Failure    " , Toast.LENGTH_LONG).show()
                }
            }

        })


    }

    private  fun sendPeerMessage(){
        val message = mRtmClient!!.createMessage()
        message.text = ed_msg.getText().toString()

        val messageBean = MessageBean(mUserId, message, true,time)
        mMessageBeanList.add(messageBean)
        adapter?.notifyItemRangeChanged(mMessageBeanList.size, 1)
        mRvMsgList?.scrollToPosition(mMessageBeanList.size - 1)


        sendmessage(message)


    }


    private  fun  sendmessage(message: RtmMessage){
        //  val options = mChatManager!!.sendMessageOptions
        //  options!!.enableOfflineMessaging = true
        mRtmClient!!.sendMessageToPeer(mPeerId, message, mChatManager!!.sendMessageOptions, object :
            ResultCallback<Void?> {

            override fun onSuccess(aVoid: Void?) {
                // do nothing
                //  Toast.makeText(baseContext, "onSuccess, Toast.LENGTH_LONG).show()

            }

            override fun onFailure(errorInfo: ErrorInfo) {
                // refer to RtmStatusCode.PeerMessageState for the message state
                val errorCode = errorInfo.errorCode
                runOnUiThread {

                   // Toast.makeText(baseContext, "onFailure>>   " + errorInfo.errorDescription, Toast.LENGTH_LONG).show()
                    when (errorCode) {
                        RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT, RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE -> BaseApplication.instance.showToast(getString(
                          R.string.send_msg_failed))
                        RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE ->{
                            //    BaseApplication.instance.showToast(getString(R.string.peer_offline))
                            timer!!.cancel()
                            endChat("User not Reachable\n Please click On Ok Button To End Chat ")

                        }
                        RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> BaseApplication.instance.showToast(getString(
                            R.string.message_cached))
                    }
                }
            }

        }
        )

    }


    private fun setRecyclerView(){
        adapter = MessageAdapter(mMessageBeanList) {mMessageBean, i ->
           // BaseApplication.instance.showToast("hh")

        }

        mRvMsgList?.layoutManager = LinearLayoutManager(ApplicationUtil.getContext())
        mRvMsgList?.scheduleLayoutAnimation()
        mRvMsgList?.adapter = adapter
        if (mRvMsgList?.layoutManager == null){
            mRvMsgList?.addItemDecoration(MarginItemDecorator(1))
        }
        mRvMsgList?.hasFixedSize()
    }


    /**
     * API CALLBACK: rtm event listener
     */

    inner  class MyRtmClientListener : RtmClientListener {

        override fun onConnectionStateChanged(state: Int, p1: Int) {
            runOnUiThread {
                when (state) {
                    RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING -> BaseApplication.instance.showToast("reconnecting")
                    RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED -> {
                        BaseApplication.instance.showToast("offline")
                        setResult(MessageUtils.ACTIVITY_RESULT_CONN_ABORTED)
                        finish()
                    }
                }
            }
        }

        override fun onMessageReceived(message: RtmMessage, peerId: String) {

            runOnUiThread {

                if (peerId == mPeerId) {

                    val messageBean = MessageBean(peerId, message, false,time)
                    messageBean.background = getMessageColor(peerId)
                    mMessageBeanList.add(messageBean)
                    adapter?.notifyItemRangeChanged(mMessageBeanList.size, 1)
                    mRvMsgList?.scrollToPosition(mMessageBeanList.size - 1)
                } else {
                    MessageUtils.addMessageBean(peerId, message)
                }
            }
        }
        override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {

        }

        override fun onTokenExpired() {

        }

        override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {

        }

        override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {

        }

        override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {

        }



        override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {

        }

    }

    private fun getMessageColor(account: String): Int {
        for (i in mMessageBeanList.indices) {
            if (account == mMessageBeanList[i].account) {
                return mMessageBeanList[i].background
            }
        }
        return MessageUtils.COLOR_ARRAY[MessageUtils.RANDOM.nextInt(MessageUtils.COLOR_ARRAY.size)]
    }
    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AgoraMsgViewModel(
                mApplication
            ) as T
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        endChat("Do you want to end chat?")

    }
}