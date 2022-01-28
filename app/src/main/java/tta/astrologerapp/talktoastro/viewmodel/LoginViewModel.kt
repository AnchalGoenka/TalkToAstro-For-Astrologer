package tta.astrologerapp.talktoastro.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import org.json.JSONObject
import tta.astrologerapp.talktoastro.model.TokenModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.volley.RequestIdentifier

/**

 * Created by Vivek Singh on 2019-06-30.

 */
class LoginViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app) {

    var arrayListMutableLiveData = MutableLiveData<tta.astrologerapp.talktoastro.model.LoginModel>()
    var responseDidFailed = MutableLiveData<String>()

    init {
    }

    fun loginUser(jsonObj: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.LOGIN.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.LOGIN, jsonObj, null, null, false
        )
    }

    fun passDeviceTokenToServer(fcmtoken:String) {
        val userID = SharedPreferenceUtils.readString(
            ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
        )
        var token = SharedPreferenceUtils.readString(
            ApplicationConstant.fcmtoken, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext())
        )


        val jsonObj =JSONObject()
        jsonObj.put("udid",fcmtoken)
        jsonObj.put("device_token",fcmtoken)
        jsonObj.put("user_id",userID)

        doApiRequest(
            RequestType.POST, RequestIdentifier.DEVICTOKEN.ordinal,
            ApplicationConstant.SAVE_TOKEN, jsonObj, null, null, false
        )

    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.LOGIN.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), tta.astrologerapp.talktoastro.model.LoginModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.LoginModel>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.LoginModel) {
                        when (data.success) {
                            1 -> {
                               //[ Toast.makeText(ApplicationUtil.getContext(),"otp ${data.otp}",Toast.LENGTH_LONG).show()
                                arrayListMutableLiveData.postValue(data)
                            }
                            0->{
                                responseDidFailed.postValue(data.message)
                                Toast.makeText(
                                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(),
                                    data.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.LoginModel) {
                        Toast.makeText(
                            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(),
                            data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


    }

    override fun onApiResponse(identifier: Int,
        response: String, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == RequestIdentifier.DEVICTOKEN.ordinal) {
            tta.astrologerapp.talktoastro .volley.gson.GsonHelper.getInstance()
                .parse(response, TokenModel::class.java,
                    object :
                        tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<TokenModel>() {
                        override fun onParseComplete(data: TokenModel) {
                            if (data.success == 1) {
                                Toast.makeText(ApplicationUtil.getContext(), "Token sent ${data.message}", Toast.LENGTH_LONG).show()
                            } else {
                                //Toast.makeText(ApplicationUtil.getContext(), data.message, Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onParseFailure(data: TokenModel) {
                            tta.astrologerapp.talktoastro.util.LogUtils.d(
                                "place list view model",
                                "parse failed due to error "
                            )
                        }
                    })
        }
    }

}