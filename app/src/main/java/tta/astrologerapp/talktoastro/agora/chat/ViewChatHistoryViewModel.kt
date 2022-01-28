package tta.astrologerapp.talktoastro.agora.chat

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mooveall.driver.base.appUpdate.AppVersionResponse
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.model.MessageList
import tta.astrologerapp.talktoastro.model.viewChatModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.RequestIdentifier
import tta.astrologerapp.talktoastro.volley.ext.RequestType

class ViewChatHistoryViewModel constructor(app: BaseApplication) : BaseViewModel(app)  {

    var ViewChatResponse = MutableLiveData<viewChatModel>()
    fun ViewChat(chatId:String){
        val url = ApplicationConstant.ChatBaseUrl +"$chatId"+"/details"
        doApiRequest(
            RequestType.GET, RequestIdentifier.VIEWCHAT.ordinal,
            url, null, null, null, false
        )
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.VIEWCHAT.ordinal) {
            val mParsedResponse = Gson().fromJson(response.toString(), viewChatModel::class.java)
            ViewChatResponse.postValue(mParsedResponse)
        }
    }
}