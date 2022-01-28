package tta.astrologerapp.talktoastro.viewmodel

import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import tta.astrologerapp.talktoastro.model.hubQuestion
import tta.astrologerapp.talktoastro.model.hubQuestionList
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.RequestIdentifier
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import tta.astrologerapp.talktoastro.volley.gson.GsonHelper
import tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener
import java.util.*
import kotlin.collections.ArrayList

class AskFreeQuestionViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app) {
    var arrayhubQuestionData = ArrayList<hubQuestionList>()
    var arrayListMutablehubQuestionData = MutableLiveData<ArrayList<hubQuestionList>>()
    var questionSubmitListMutableLiveData = MutableLiveData<String>()




    fun gethubQuestion(){
        doApiRequest(
            RequestType.GET,
            RequestIdentifier.ASKFREEQUESTION.ordinal,ApplicationConstant.AskFreeQuestion,
            null,null,null,false)
    }

    fun gethubQuestionSubmit(jsonObject: JSONObject){
        doApiRequest(RequestType.POST,
            RequestIdentifier.HUBQUESTIONSUBMIT.ordinal, ApplicationConstant.HUBQUESTIONSUBMIT,
            jsonObject,null,null,false)
    }

    override fun onApiResponse(
        identifier: Int,
        response: String,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier== RequestIdentifier.ASKFREEQUESTION.ordinal){
         GsonHelper.getInstance().parse(response,hubQuestion::class.java,object :
             OnGsonParseCompleteListener<hubQuestion>(){
             override fun onParseComplete(data: hubQuestion) {
                 arrayhubQuestionData = data.hubQuestionListItem
                 Collections.reverse(arrayhubQuestionData)
                 arrayListMutablehubQuestionData.postValue(arrayhubQuestionData)
             }

             override fun onParseFailure(data: hubQuestion) {
                 tta.astrologerapp.talktoastro.util.LogUtils.d(
                     "place list view model",
                     "parse failed due to error "
                 )
             }

         })
        }


    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier==RequestIdentifier.HUBQUESTIONSUBMIT.ordinal){
            if (response.get("success") == 1){
                questionSubmitListMutableLiveData.postValue(response.get("message").toString())
            }
        }
    }
}