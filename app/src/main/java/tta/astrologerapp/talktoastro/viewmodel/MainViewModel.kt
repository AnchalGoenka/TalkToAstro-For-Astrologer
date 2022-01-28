package tta.astrologerapp.talktoastro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mooveall.driver.base.appUpdate.AppVersionResponse
import org.json.JSONObject
import tour.traveling.travel.ui.product.MainRespository
import tta.astrologerapp.talktoastro.model.TokenModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.volley.RequestIdentifier
import tta.astrologerapp.talktoastro.volley.ext.RequestType

class MainViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app)  {
    var appVersionReq = MutableLiveData<AppVersionResponse>()
    var  joinmessage =MutableLiveData<String>()
    init {
        getAppVersion()
    }

    fun getAppVersion() {
        doApiRequest(
            RequestType.GET,
            tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETAPPVERSION.ordinal,
            ApplicationConstant.GET_APP_VERSION,
            null,
            null,
            null,
            false
        )
    }

    fun joinApi(chatId:String,status:String?){

        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/$status"
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATJOINSTATUS.ordinal, url,
            null, null, null, false
        )
    }


    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        Log.i("TAG", "" + response)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETAPPVERSION.ordinal) {
            val mParsedResponse = Gson().fromJson(response.toString(), AppVersionResponse::class.java)
            appVersionReq.postValue(mParsedResponse)
        }
        if(identifier==RequestIdentifier.CHATJOINSTATUS.ordinal){

            joinmessage.postValue(response["message"].toString())

        }
    }


}