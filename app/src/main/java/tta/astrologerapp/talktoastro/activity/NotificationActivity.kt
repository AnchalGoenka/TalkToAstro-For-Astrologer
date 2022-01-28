package tta.astrologerapp.talktoastro.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.toolbar.*

import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.MainActivity
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.adapter.NotificationMainAdapter
import tta.astrologerapp.talktoastro.agora.chat.ViewChatHistory
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.MarginItemDecorator
import tta.astrologerapp.talktoastro.viewmodel.NotificationViewModel

class NotificationActivity : AppCompatActivity() {


    private var notificationViewModel: NotificationViewModel? = null
    private var notificationMainAdapter: NotificationMainAdapter? = null
    lateinit var mBaseApplication: BaseApplication
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        getSupportActionBar()?.hide();
        val application = this.application
        if (application is BaseApplication) {
            mBaseApplication = application
        }

        setToolbar()

      //  toolbar = findViewById(R.id.toolbar)
      //  setSupportActionBar(toolbar)
        callRecyclerview()
    }

    val myApplication: BaseApplication
        get() {
            return mBaseApplication
        }

    val myActivity: NotificationActivity
        get() {
            return this
        }

    fun setToolbar(){
        val toolbar_main_activity= myActivity.findViewById<Toolbar>(R.id.toolbar_notification)
      //  toolbar_main_activity.tv_header_refresh.visibility= View.INVISIBLE
    //    toolbar_main_activity.iv_notofication.visibility=View.INVISIBLE
        toolbar_main_activity.title = "Notification"
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_main_activity.setNavigationOnClickListener {
            finish()
        }
    }

    fun callRecyclerview(){

        notificationViewModel = ViewModelProvider(this, MyViewModelFactory(myApplication)).get(NotificationViewModel::class.java)

        notificationViewModel?.arrayNotificationMutableLiveData?.observe(this, Observer {

            notificationMainAdapter = NotificationMainAdapter(it!!)
            val notifi_list: RecyclerView = findViewById(R.id.rv_notification)
            notifi_list.layoutManager = LinearLayoutManager(ApplicationUtil.getContext())
            notifi_list.adapter = notificationMainAdapter
            if (notifi_list.layoutManager == null){
                notifi_list.addItemDecoration(MarginItemDecorator(1))
            }
            //notifi_list.hasFixedSize()
        })

        notificationViewModel?.notificationDidFailed?.observe(this, Observer {

            ApplicationUtil.showDialog(this, it)
        })
    }

    class MyViewModelFactory(private val mApplication: BaseApplication) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotificationViewModel(mApplication) as T
        }
    }
}