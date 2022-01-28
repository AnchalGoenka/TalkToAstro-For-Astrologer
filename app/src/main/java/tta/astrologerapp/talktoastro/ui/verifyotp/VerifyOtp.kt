package tta.astrologerapp.talktoastro.ui.verifyotp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import org.json.JSONObject
import tta.astrologerapp.talktoastro.MainActivity
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.model.LoginModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.viewmodel.VerifyOtpViewModel

class VerifyOtp : tta.astrologerapp.talktoastro.BaseFragment() {

    private var verifyOtpModel: VerifyOtpViewModel? = null
    var timer: CountDownTimer? = null

    override val layoutResId: Int
        get() = R.layout.fragment_verify_otp

    override fun getToolbarId(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val num1: EditText = myActivity.findViewById(R.id.num1)
        val num2: EditText = myActivity.findViewById(R.id.num2)
        val num3: EditText = myActivity.findViewById(R.id.num3)
        val num4: EditText = myActivity.findViewById(R.id.num4)
        val btn_submit: Button = myActivity.findViewById(R.id.btn_submit)
        val type = arguments?.getString("Register")
        runTimer()
        verifyOtpModel = ViewModelProviders.of(this, MyViewModelFactory(myApplication)).get(VerifyOtpViewModel::class.java)

        verifyOtpModel?.resendOTPMutableLiveData?.observe(this, Observer {
            if(it.otp==null){
                success(it)
            }else{
            SharedPreferenceUtils.put(ApplicationConstant.OTP, it.otp, SharedPreferenceUtils.getSharedPref(
                ApplicationUtil.getContext()))}
        })
        verifyOtpModel?.loginDidFailed?.observe(viewLifecycleOwner, Observer {
            hideProgressBar()
        })
        verifyOtpModel?.arrayListMutableLiveData?.observe(viewLifecycleOwner, Observer {
            success(it)
        })

        num1.requestFocus()
        num1.showSoftInputOnFocus = true
        num1.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (num1.length() == 1)
                    num2.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        num2.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (num2.length() == 1)
                    num3.requestFocus()
                if (num2.length() == 0)
                    num1.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        num3.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (num3.length() == 1)
                    num4.requestFocus()
                if (num3.length() == 0)
                    num2.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        num4.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (num4.length() == 0)
                    num3.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        btn_submit.setOnClickListener {
            showProgressBar("Loading")
            verifyOtp(num1, num2, num3, num4)
        }

        button_Resend.setOnClickListener{
            val type = arguments?.getString("Register")
            if (type == "login") {
                val hashVal = arguments?.getSerializable("login credential") as HashMap<String, String>
                verifyOtpModel?.resendOTPRequest(JSONObject(hashVal as Map<*, *>))
                runTimer()
            } else {
                val hashVal = arguments?.getSerializable("Register credential") as HashMap<String, String>
                verifyOtpModel?.resendOTPRequest(JSONObject(hashVal as Map<*, *>))
                runTimer()
            }
        }
    }

    fun success(it: LoginModel) {
        val type = arguments?.getString("Register")
        showProgressBar("Loading")
        val txv_otp_verified: TextView = myActivity.findViewById(R.id.txv_otp_verified)
        txv_otp_verified.visibility = View.VISIBLE
        if (type == "login") {
            val name = it!!.firstName + " " + it!!.lastName
            SharedPreferenceUtils.put(
                ApplicationConstant.USERNAME, it!!.email,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            SharedPreferenceUtils.put(
                ApplicationConstant.USERID, it!!.userID,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            SharedPreferenceUtils.put(
                ApplicationConstant.PHONECODE, it!!.phonecode,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            SharedPreferenceUtils.put(
                ApplicationConstant.MOBILE, it!!.mobile,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            SharedPreferenceUtils.put(
                ApplicationConstant.BALANCE, it!!.balance,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            SharedPreferenceUtils.put(
                ApplicationConstant.NAME, name,
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
            )

            it.notifyCount?.notify?.let { it1 ->
                SharedPreferenceUtils.put(
                    ApplicationConstant.NOTIFYCOUNT, it1,
                    SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
                )
            }
        }
        Handler().postDelayed(Runnable {
            hideProgressBar()
            val intent = Intent(
                ApplicationUtil.getContext(),
                MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK   or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun verifyOtp(num1: EditText, num2: EditText, num3: EditText, num4: EditText){
        val userId = SharedPreferenceUtils.readString(ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
        val mobile = SharedPreferenceUtils.readString(ApplicationConstant.MOBILE, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
        val otp = SharedPreferenceUtils.readString(ApplicationConstant.OTP, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
        val userEnteredOTP = "${num1.text}${num2.text}${num3.text}${num4.text}"
        var jsonObj = JSONObject()
        if (userEnteredOTP == otp){
            val type = arguments?.getString("Register")
            //var url = ""
            if (type == "login") {
                //url = "${ApplicationConstant.VERIFYOTP}?verify=\"1\"&mobile=\"${mobile}\""
                jsonObj.put("otp", otp)
                jsonObj.put("mobile", mobile)
                verifyOtpModel?.resendOTPRequest(jsonObj)
            } else {
                //url = "${ApplicationConstant.VERIFYOTP}?verify=\"1\"&userID=\"${userId}\""
                jsonObj.put("verify", "1")
                jsonObj.put("userID", userId)
                verifyOtpModel?.verifyOtp(jsonObj)
            }

        }else{
            showAlertDialog()
        }
    }

    fun runTimer() {
        button_Resend.isEnabled = false
        button_Resend.setBackgroundResource(R.drawable.button_rounded_corners_gray)
        button_Resend.setTextColor(Color.DKGRAY)
        timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if ((millisUntilFinished/1000) < 10) {
                    txv_timer_otp.setText("0:0"+ (millisUntilFinished/1000))
                } else {
                    txv_timer_otp.setText("0:"+ (millisUntilFinished/1000))
                }
            }

            override fun onFinish() {
                button_Resend.isEnabled = true
                button_Resend.setBackgroundResource(R.drawable.button_signup)
                button_Resend.setTextColor(Color.WHITE)
                txv_timer_otp.setText("00:00")
            }
        }
        timer?.start()
    }

    private fun showAlertDialog() {
            hideProgressBar()
            // Initialize a new instance of
            val builder = AlertDialog.Builder(myActivity)
            // Set the alert dialog title
            builder.setTitle("Incorrect pin")

            // Display a message on alert dialog
            builder.setMessage("Please enter correct pin")
            // Display a neutral button on alert dialog
            builder.setNeutralButton("OK"){_,_ ->

            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()

    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VerifyOtpViewModel(mApplication) as T
        }
    }


    companion object {
        internal val tagName: String
        get() = VerifyOtp::class.java.name

        fun newInstance(bundle: Bundle?): VerifyOtp{
            val fragment = VerifyOtp()
            fragment.arguments = bundle
            return fragment
        }
    }
}
