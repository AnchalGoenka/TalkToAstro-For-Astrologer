package tta.astrologerapp.talktoastro.ui.home

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import com.google.gson.Gson
import org.json.JSONObject
import tta.astrologerapp.talktoastro.model.AstroCallParameterResponse
import tta.astrologerapp.talktoastro.model.ChatHistoryModel

class HomeViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app) {

    var _success = MutableLiveData<Int>()
    var arrayMutableCallHistory = MutableLiveData<ArrayList<tta.astrologerapp.talktoastro.model.CallHistoryModel>>()
    var arrOfCallHistoryModel = ArrayList<tta.astrologerapp.talktoastro.model.CallHistoryModel>()
    var arrayMutableChatHistory = MutableLiveData<ArrayList<ChatHistoryModel>>()
    var arrOfChatHistoryModel = ArrayList<ChatHistoryModel>()
    var arrayMutableWalletViewModel = MutableLiveData<String>()
    var astroCallParameter=MutableLiveData<AstroCallParameterResponse>()
    var responseDidFailed = MutableLiveData<String>()
    var astroStatus = MutableLiveData<String>()
    var astroChatStatus = MutableLiveData<String>()

    init {

    }

    fun changeStatus(json: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.STATUS.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.CHANGEASTROSTATUS, json, null, null, false
        )
    }

    fun changeChatStatus(json: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATSTATUS.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.CHANGEASTROCHATSTATUS, json, null, null, false
        )
    }

    fun getCallHistory(json: JSONObject?) {
        Handler().postDelayed({
            doApiRequest(
                RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.CALLHISTORY.ordinal,
                tta.astrologerapp.talktoastro.util.ApplicationConstant.USER_CALL_HISTORY, json, null, null, false
            )
        }, 1000)
    }

    fun getChatHistory(json: JSONObject?) {
        Handler().postDelayed({
            doApiRequest(
                RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATHISTORY.ordinal,
                tta.astrologerapp.talktoastro.util.ApplicationConstant.ASTRO_CHAT_HISTORY, json, null, null, false
            )
        }, 500)
    }

    fun getWalletTransactions(json: JSONObject?){
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.UPDATEUSERWALLET.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERWALLETHISTORY, json, null, null, false
        )
    }

    fun getAstroStatus(json: JSONObject?) {
        Handler().postDelayed({
            doApiRequest(
                RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETASTROSTATUS.ordinal,
                tta.astrologerapp.talktoastro.util.ApplicationConstant.GET_ASTRO_STATUS, json, null, null, false
            )
            doApiRequest(
                RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETASTROCHATSTATUS.ordinal,
                tta.astrologerapp.talktoastro.util.ApplicationConstant.GET_ASTRO_CHAT_STATUS, json, null, null, false
            )
        }, 1000)
    }


    fun getAstroCallParameter(json: JSONObject?){
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROCALLPARAMETER.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.Astro_Call_Parameter, json, null, null, false
        )
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.STATUS.ordinal) {
            _success.postValue(response["success"] as Int?)
        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATSTATUS.ordinal) {
            _success.postValue(response["success"] as Int?)
        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETASTROSTATUS.ordinal) {
            if (response["phone_status"] == "online") {
                astroStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.ONLINE)
            }else if (response["phone_status"] == tta.astrologerapp.talktoastro.util.ApplicationConstant.BUSYSTATUS){
                astroStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.BUSY)
            }
            else {
                astroStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.OFFLINE)
            }

        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.GETASTROCHATSTATUS.ordinal) {
            if (response["chat_status"] == "online") {
                astroChatStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.ONLINE)
            }else if (response["chat_status"] == tta.astrologerapp.talktoastro.util.ApplicationConstant.BUSYSTATUS){
                astroChatStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.BUSY)
            }
            else {
                astroChatStatus.postValue(tta.astrologerapp.talktoastro.util.ApplicationConstant.OFFLINE)
            }

        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.CALLHISTORY.ordinal) {
            response.keys().forEach {
                val jsonObj = response.get(it) as JSONObject
                val callHistory = Gson().fromJson(jsonObj.toString(), tta.astrologerapp.talktoastro.model.CallHistoryModel::class.java)
                arrOfCallHistoryModel.add(callHistory)
            }
            arrayMutableCallHistory.postValue(arrOfCallHistoryModel)

        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATHISTORY.ordinal) {
            response.keys().forEach {
                val jsonObj = response.get(it) as JSONObject
                val chatHistory = Gson().fromJson(jsonObj.toString(), ChatHistoryModel::class.java)
                arrOfChatHistoryModel.add(chatHistory)
            }
            arrayMutableChatHistory.postValue(arrOfChatHistoryModel)

        }
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.UPDATEUSERWALLET.ordinal) {
            val userBalance = (response["0"] as JSONObject).get("userWalletBalance").toString()
            arrayMutableWalletViewModel.postValue(userBalance)
        }

        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROCALLPARAMETER.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), AstroCallParameterResponse::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<AstroCallParameterResponse>() {
                    override fun onParseComplete(data: AstroCallParameterResponse) {
                        astroCallParameter.postValue(data)
                    }
                    override fun onParseFailure(data: AstroCallParameterResponse) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                    }
                })
        }
    }

    override fun onApiError(identifier: Int, error: String?, errorCode: String?) {
        super.onApiError(identifier, error, errorCode)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.STATUS.ordinal) {
            responseDidFailed.postValue(error)
        }
    }
}