package tta.astrologerapp.talktoastro.ui.Login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.awesomedialog.*
import com.google.android.gms.tasks.OnCompleteListener
import tta.astrologerapp.talktoastro.MainActivity
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.viewmodel.LoginViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.json.JSONObject
import tour.traveling.travel.ui.product.ComounViewModel
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.BaseFragment
import tta.astrologerapp.talktoastro.model.TokenModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.LogUtils
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils

class LoginFragment : BaseFragment() {
    override val layoutResId: Int
        get() = R.layout.activity_login
    private var loginWithPassword: Boolean = false

    val mViewModel by lazy { ViewModelProviders.of(this).get(ComounViewModel::class.java) }

    override fun getToolbarId(): Int {
        return 0
    }

    private var loginViewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //resetLoginCredentialsOnLogout()
        SharedPreferenceUtils.clearPrefs(applicationContext)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
                System.exit(0);
            }
        })
        return inflater.inflate(layoutResId, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        val navView: NavigationView = activity.findViewById(R.id.nav_view)
        val toolbar: Toolbar = activity.findViewById(R.id.toolbar)
        toolbar.title = "Login"
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon: ImageView = activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility = View.INVISIBLE

            val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawer_layout)

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar.navigationIcon = null



        navView.getHeaderView(0).nav_txv_name.text = ""
        navView.getHeaderView(0).nav_txv_email.text = ""
        navView.getHeaderView(0).nav_txv_phone.text = ""
        loginViewModel = ViewModelProviders.of(this,
            LoginFragment.MyViewModelFactory(
                BaseApplication.instance
            )
        ).get(LoginViewModel::class.java)
        loginViewModel?.responseDidFailed?.observe(this, Observer {
            hideProgressBar()
            Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), it, Toast.LENGTH_LONG).show()
        })
        loginViewModel?.arrayListMutableLiveData?.observe(this, Observer {


            if(it.user_type=="user"){
                hideProgressBar()
                AwesomeDialog.build(activity)
                    .title("This is Astrologer App ")
                    .body(
                        "For User App Click on below Button ",
                        color = ContextCompat.getColor(activity, android.R.color.holo_orange_dark)
                    )
                    .position(
                        AwesomeDialog.POSITIONS.CENTER
                    )
                    .icon(R.mipmap.ic_launcher_round)
                    .onPositive(
                        " TalkToAstro User App ",
                        buttonBackgroundColor = R.drawable.layout_rounded_cancel,
                        textColor = ContextCompat.getColor(
                            activity,
                            android.R.color.white
                        )
                    )
                    {
                        val appPackageName = activity.packageName // package name of the app
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=tta.destinigo.talktoastro")
                                )
                            )
                        } catch (anfe: android.content.ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=tta.destinigo.talktoastro")
                                )
                            )
                        }

                        hideProgressBar()
                    }
            }else{
            if (loginWithPassword) {
                hideProgressBar()
                val userName = txv_username.text.toString()
                val password = txv_password.text.toString()
                val name = it!!.firstName + " " + it!!.lastName
                SharedPreferenceUtils.put(
                    ApplicationConstant.USERNAME, userName,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.PASSWORD, password,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.USERID, it!!.userID,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.PHONECODE, it!!.phonecode,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.MOBILE, it!!.mobile,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.BALANCE, it!!.balance,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.NAME, name,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

                SharedPreferenceUtils.put(
                    ApplicationConstant.EMAIL, it.email,
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()
                    )
                )

               callSaveTokenApi()

                val intent = Intent(ApplicationUtil.getContext(), MainActivity::class.java)
                startActivity(intent)
            }
            else {
                SharedPreferenceUtils.put(ApplicationConstant.OTP, it.otp, SharedPreferenceUtils.getSharedPref(
                    ApplicationUtil.getContext()))
                var hashMap = HashMap<String, String>()
                hashMap["mobile"] = txv_username.text.toString()
                hashMap["viasms"] = "1"
                hashMap["phonecode"] = "91"

                val intent = Intent(ApplicationUtil.getContext(), MainActivity::class.java)
                intent.putExtra("login credentials", hashMap)
                intent.putExtra("fragmentNumber", 3)
                startActivity(intent)
            }}
        })
        btn_login.setOnClickListener {
            loginWithPassword = true
            showProgressBar("Please wait..")
            var jsonObj = JSONObject()
            jsonObj.put("mobile",txv_username.text.toString())
            jsonObj.put("password", txv_password.text.toString())

            loginViewModel?.loginUser(jsonObj)

        }

        button_login_with_otp.setOnClickListener {
            loginWithPassword = false
            callSaveTokenApi()
            if (ApplicationUtil.isNetworkAvailable(ApplicationUtil.getContext())) {
                val phoneNumber = txv_username.text.toString()
                if (!phoneNumber.isEmpty()) {
                    SharedPreferenceUtils.put(
                        ApplicationConstant.MOBILE, phoneNumber,
                        SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
                    )
                    var jsonObj = JSONObject()
                    jsonObj.put("mobile", phoneNumber)
                    jsonObj.put("viasms", "1")
                    jsonObj.put("phonecode", "91")
                    showProgressBar("Loading", true)
                    loginViewModel?.loginUser(jsonObj)
                } else {
                    hideProgressBar()
                    if (txv_username.text.isEmpty()) {
                        txv_username.setError("Field cannot be blank")
                    }
                    Toast.makeText(ApplicationUtil.getContext(),"Please enter phone number", Toast.LENGTH_SHORT).show()
                }

            }
        }

//        txv_forgot_password.makeLinks(
//            Pair("Forgot Password?", android.view.View.OnClickListener {
//                val intent = Intent(ApplicationUtil.getContext(), MainActivity::class.java)
//                intent.putExtra("fragmentNumber",2)
//                startActivity(intent)
//            })
//        )
//
//        txv_dont_have_account.makeLinks(
//            Pair("Create Account", android.view.View.OnClickListener {
//                val intent = Intent(ApplicationUtil.getContext(), MainActivity::class.java)
//                intent.putExtra("fragmentNumber",1)
//                startActivity(intent)
//            })
//        )
    }

    private fun callSaveTokenApi() {
     var token: String?=""

      FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task->
          if (!task.isSuccessful) {
              Log.w("token failed", "Fetching FCM registration token failed", task.exception)
              return@OnCompleteListener
          }

          // Get new FCM registration token
           token = task.result.toString()
          SharedPreferenceUtils.put(
              ApplicationConstant.fcmtoken, token.toString(),
              SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
          )
          Log.d("fcmtoken","Token: $token")

          loginViewModel?.passDeviceTokenToServer(token!! )
      })



        /*val userID = SharedPreferenceUtils.readString(
            ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))

        var token = SharedPreferenceUtils.readString(
            ApplicationConstant.DEVICETOKEN, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))

        BaseApplication.instance.showToast(token.toString())*/


    }

    fun resetLoginCredentialsOnLogout() {
        SharedPreferenceUtils.put(
            ApplicationConstant.USERNAME, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.PASSWORD, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.PHONECODE, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.MOBILE, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.BALANCE, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.NAME, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))

        SharedPreferenceUtils.put(
            ApplicationConstant.EMAIL, "",
            SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))
    }

    private fun TextView.makeLinks(vararg links: Pair<String, android.view.View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: android.view.View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    class MyViewModelFactory(private val mApplication: BaseApplication) :
        ViewModelProvider.Factory {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(mApplication) as T
        }
    }

    companion object {
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

        internal val tagName: String
            get() = LoginFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment SearchPlacesFragment.
         */
        fun newInstance(bundle: Bundle?): LoginFragment {
            val fragment = LoginFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
