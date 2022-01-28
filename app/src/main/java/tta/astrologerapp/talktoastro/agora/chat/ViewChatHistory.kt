package tta.astrologerapp.talktoastro.agora.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.activity.NotificationActivity
import tta.astrologerapp.talktoastro.adapter.NotificationMainAdapter
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.MarginItemDecorator
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.viewmodel.NotificationViewModel

class ViewChatHistory : AppCompatActivity() {
    private var ViewChatHistoryViewModel: ViewChatHistoryViewModel? = null
    private var ViewChatHistoryAdapter: ViewChatHistoryAdapter? = null
    lateinit var mBaseApplication: BaseApplication
    lateinit var userName :String

    val myApplication: BaseApplication
        get() {
            return mBaseApplication
        }

    val myActivity: ViewChatHistory
        get() {
            return this
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_chat_history)
        getSupportActionBar()?.hide()
        val application = this.application
        if (application is BaseApplication) {
            mBaseApplication = application
        }

        val intent = intent
        val  chatId = intent.getStringExtra("ChatID")
        callRecyclerview(chatId)


    }
    fun setToolbar(){
        val toolbar_main_activity= myActivity.findViewById<Toolbar?>(R.id.toolbar_ChatHistory)
        //  toolbar_main_activity.tv_header_refresh.visibility= View.INVISIBLE
        //    toolbar_main_activity.iv_notofication.visibility=View.INVISIBLE
        toolbar_main_activity?.title = userName
        toolbar_main_activity?.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_main_activity?.setNavigationOnClickListener {
            finish()
        }
    }



    fun callRecyclerview(chatId:String){

        ViewChatHistoryViewModel = ViewModelProvider(this, MyViewModelFactory(myApplication)).get(
            tta.astrologerapp.talktoastro.agora.chat.ViewChatHistoryViewModel::class.java)

        ViewChatHistoryViewModel?.ViewChat(chatId)
        ViewChatHistoryViewModel?.ViewChatResponse?.observe(this, Observer {

           userName = it.chat.user.first_name +" " + it.chat.user.last_name
            setToolbar()
            val viewchat_list: RecyclerView = findViewById(R.id.rv_viewChat)
            viewchat_list.layoutManager = LinearLayoutManager(myActivity)
            ViewChatHistoryAdapter = ViewChatHistoryAdapter(it.chat.message_list)
            viewchat_list.adapter = ViewChatHistoryAdapter
            viewchat_list.hasFixedSize()
        })

       /* notificationViewModel?.notificationDidFailed?.observe(this, Observer {

            ApplicationUtil.showDialog(this, it)
        })*/
    }

    override fun onResume() {
        super.onResume()

    }

    class MyViewModelFactory(private val mApplication: BaseApplication) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewChatHistoryViewModel(mApplication) as T
        }
    }
}