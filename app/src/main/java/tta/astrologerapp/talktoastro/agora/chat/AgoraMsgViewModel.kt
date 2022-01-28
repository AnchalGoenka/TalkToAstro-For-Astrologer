package tta.astrologerapp.talktoastro.agora.chat

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationConstant.Companion.RTMTOKEN
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.volley.RequestIdentifier
import tta.astrologerapp.talktoastro.volley.ext.RequestType

class AgoraMsgViewModel constructor(app: BaseApplication) : BaseViewModel(app) {

    val rtmToken= MutableLiveData<String>()
    var mesageCheck = MutableLiveData<String>()
    var saveChat = MutableLiveData<Boolean>()
    var joinmessage =MutableLiveData<String>()



    fun rtmToken(jsonObject: JSONObject){
       /* val userID = SharedPreferenceUtils.readString(ApplicationConstant.USERID, "", SharedPreferenceUtils.getSharedPref(
            ApplicationUtil.getContext()))
        var json = JSONObject();
        jsonObject.put("userID", userID)*/
        doApiRequest(
            RequestType.POST, RequestIdentifier.RTMTOKEN.ordinal,
            ApplicationConstant.RTMTOKEN, jsonObject, null, null, false
        )
    }

    fun joinApi(chatId:String,status:String?){
        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/$status"
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.CHATJOINSTATUS.ordinal, url,
            null, null, null, false
        )
    }
    fun checkAstroChatJoinStatus(chatId: String){
        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/status"
        doApiRequest(
            RequestType.GET, RequestIdentifier.CHATFCMNOTIFICATION.ordinal,
            url, null, null, null, false
        )
    }

    fun saveChat(jsonObject: JSONObject,chatId: String){
        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/save"
        doApiRequest(
            RequestType.POST, RequestIdentifier.SAVECHAT.ordinal,
            url, jsonObject, null, null, false
        )
    }

    fun endchat( chatId: String){
        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/end"
        doApiRequest(
            RequestType.POST, RequestIdentifier.ENDCHAT.ordinal,
            url, null, null, null, false
        )
    }


    override fun onApiResponse(identifier: Int, response: JSONObject, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier==RequestIdentifier.RTMTOKEN.ordinal){
            if (response["success"] == 1) {
                rtmToken.postValue(response["token"] as String?)
            } else {

            }

        }
        if(identifier==RequestIdentifier.CHATJOINSTATUS.ordinal){

            joinmessage.postValue(response["message"].toString())

        }

        if(identifier == RequestIdentifier.CHATFCMNOTIFICATION.ordinal){

            if(response["success"]== 1){
                mesageCheck.postValue(response["message"].toString())

            } else{

            }

        }
        if(identifier == RequestIdentifier.SAVECHAT.ordinal){
            saveChat.postValue(response["status"] as Boolean)
        }

        if(identifier==RequestIdentifier.ENDCHAT.ordinal){

        }

    }
}