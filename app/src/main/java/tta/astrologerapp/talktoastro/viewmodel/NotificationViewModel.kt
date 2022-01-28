package tta.astrologerapp.talktoastro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.model.NotificationList
import tta.astrologerapp.talktoastro.model.NotificationModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType


class NotificationViewModel constructor(app: BaseApplication): BaseViewModel(app) {

    val arrayNotificationMutableLiveData = MutableLiveData<ArrayList<NotificationList>>()
    //val arrayNotificationLiveData : LiveData<ArrayList<NotificationList>> = arrayNotificationMutableLiveData
    var arrayNotificationModel =ArrayList<NotificationList>()
    var notificationDidFailed = MutableLiveData<String>()


    init {
        notification()
    }

    fun notification(){

        var jsonObj = JSONObject()
        jsonObj.put("apptype", "astrologer")

        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.APPNOTIFICATION.ordinal,
            ApplicationConstant.APP_NOTIFICATION, jsonObj, null, null, false)
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)

        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.APPNOTIFICATION.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), NotificationModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<NotificationModel>() {
                    override fun onParseComplete(data: NotificationModel) {

                        arrayNotificationModel = data.notificationlist
                        arrayNotificationMutableLiveData.postValue(data.notificationlist)
                    }

                    override fun onParseFailure(data: NotificationModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error ")
                    }
                })
        }

    }

//    override fun onApiResponse(identifier: Int, response: String, serverDate: Long, lastModified: Long) {
//        super.onApiResponse(identifier, response, serverDate, lastModified)
//
//        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.APPNOTIFICATION.ordinal) {
//        tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response, NotificationModel::class.java,
//            object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<NotificationModel>() {
//                override fun onParseComplete(data: NotificationModel) {
//
//                    arrayNotificationModel = data.notificationlist
//                    arrayNotificationMutableLiveData.postValue(data.notificationlist)
//                }
//
//                override fun onParseFailure(data: NotificationModel) {
//                    tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error ")
//                }
//            })
//    }
//}


    }
