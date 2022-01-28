package tta.astrologerapp.talktoastro.chat.services

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.chat.models.ChatFormModel
import tta.astrologerapp.talktoastro.chat.models.ChatFormSubmitResponseModel
import tta.astrologerapp.talktoastro.chat.models.ChatHistoryModel
import tta.astrologerapp.talktoastro.util.*
import tta.astrologerapp.talktoastro.volley.RequestIdentifier
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import tta.astrologerapp.talktoastro.volley.gson.GsonHelper
import tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener


/**

 * Created by Vivek Singh on 5/11/20.

 */

class ChatFormViewModel constructor(app: BaseApplication) : BaseViewModel(app) {
    var arrayListMutableLiveData = MutableLiveData<ChatFormModel>()

    var chatFormSubmitSuccess  = MutableLiveData<ChatFormSubmitResponseModel>()
    var chatFormSubmitFailure  = MutableLiveData<String>()

    var arrayMutableChatHistory = MutableLiveData<ArrayList<ChatHistoryModel>>()
    var arrOfChatHistoryModel = ArrayList<ChatHistoryModel>()

    fun getChatHistory(json: JSONObject) {
        doApiRequest(
            RequestType.POST, RequestIdentifier.CHATHISTORY.ordinal,
            ApplicationConstant.ASTRO_CHAT_HISTORY, json, null, null, false
        )
    }

    override fun onApiResponse(identifier: Int, response: JSONObject, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == RequestIdentifier.CHATHISTORY.ordinal) {
            GsonHelper.getInstance().parse(response.toString(), ChatHistoryModel::class
                .java,
                object : OnGsonParseCompleteListener<ChatHistoryModel>() {
                    override fun onParseComplete(data: ChatHistoryModel?) {
                        response.keys().forEach {
                            val jsonObj = response.get(it) as JSONObject
                            val chatHistory = Gson().fromJson(jsonObj.toString(), ChatHistoryModel::class.java)
                            arrOfChatHistoryModel.add(chatHistory)
                        }
                        arrayMutableChatHistory.postValue(arrOfChatHistoryModel)
                    }
                    override fun onParseFailure(data: ChatHistoryModel?) {
                        LogUtils.d("Parsing failed for chat form submit response.")
                    }
                }
            )
        }
    }
}