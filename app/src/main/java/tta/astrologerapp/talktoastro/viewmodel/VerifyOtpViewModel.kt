package tta.astrologerapp.talktoastro.viewmodel

import androidx.lifecycle.MutableLiveData
import android.widget.Toast
import org.json.JSONObject
import tta.astrologerapp.talktoastro.model.LoginModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType


/**

 * Created by Vivek Singh on 2019-06-25.

 */
class VerifyOtpViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication): BaseViewModel(app) {

    var arrayListMutableLiveData = MutableLiveData<LoginModel>()
    var loginDidFailed = MutableLiveData<String>()
    var resendOTPMutableLiveData = MutableLiveData<LoginModel>()
    init {
    }

    fun verifyOtp(jsonObj: JSONObject){
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.VERIFYOTP.ordinal,
            ApplicationConstant.VERIFYOTP, jsonObj, null, null, false)
    }

    fun resendOTPRequest(jsonObj: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.LOGIN.ordinal,
            ApplicationConstant.LOGIN, jsonObj, null, null, false)
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.VERIFYOTP.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), LoginModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<LoginModel>() {
                    override fun onParseComplete(data: LoginModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("OTP verify response: $data")
                        when(data.success) {
                            1 -> {
                                arrayListMutableLiveData.postValue(data)
                            }
                            0 -> {
                                loginDidFailed.postValue(data.message)
                            }
                        }
                    }

                    override fun onParseFailure(data: LoginModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                        Toast.makeText(ApplicationUtil.getContext(),data.message, Toast.LENGTH_SHORT).show()
                    }
                })
        } else if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.LOGIN.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), LoginModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<LoginModel>() {
                    override fun onParseComplete(data: LoginModel) {
                        when(data.success) {
                            1 -> {
                                resendOTPMutableLiveData.postValue(data)
                            }
                            0 -> {
                                loginDidFailed.postValue(data.message)
                            }
                        }
                    }

                    override fun onParseFailure(data: LoginModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                        Toast.makeText(ApplicationUtil.getContext(),data.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

//    override fun onApiResponse(identifier: Int, response: String, serverDate: Long, lastModified: Long) {
//        super.onApiResponse(identifier, response, serverDate, lastModified)
//        var response = response
//        response = response.substring(response.indexOf("{", 0, true),response.length)
//        if(identifier == tta.destinigo.talktoastro.volley.RequestIdentifier.VERIFYOTP.ordinal) {
//            tta.destinigo.talktoastro.volley.gson.GsonHelper.getInstance().parse(response, LoginModel::class.java,
//                object : tta.destinigo.talktoastro.volley.gson.OnGsonParseCompleteListener<LoginModel>() {
//                    override fun onParseComplete(data: LoginModel) {
//                        tta.destinigo.talktoastro.util.LogUtils.d("OTP verify response: $data")
//                        when(data.success) {
//                            1 -> {
//                                arrayListMutableLiveData.postValue(data)
//                            }
//                            0 -> {
////                                SharedPreferenceUtils.put(ApplicationConstant.USERNAME, "", SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
////                                SharedPreferenceUtils.put(ApplicationConstant.PASSWORD, "", SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
//                            }
//                        }
//                    }
//
//                    override fun onParseFailure(data: LoginModel) {
//                        tta.destinigo.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
//                        Toast.makeText(ApplicationUtil.getContext(),data.message, Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//    }
}