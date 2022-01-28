package tta.astrologerapp.talktoastro.viewmodel

import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import com.google.gson.Gson
import org.json.JSONObject


/**

 * Created by Vivek Singh on 2019-08-29.

 */
class ReportHistoryViewModel constructor(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication): BaseViewModel(mApplication), JSONConvertable {
    var arrayMutableReportHistory = MutableLiveData<ArrayList<tta.astrologerapp.talktoastro.model.ReportHistoryModel>>()
    var arrOfReportHistoryModel = ArrayList<tta.astrologerapp.talktoastro.model.ReportHistoryModel>()

    init {
        getReportHistory()
    }

    fun getReportHistory() {
        var json = JSONObject()
        json.put(
            "astroID", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "",
                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
            )
        )
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTHISTORY.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERREPORTHISTORY, json, null, null, false
        )
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTHISTORY.ordinal) {
            response.keys().forEach {
                val jsonObj = response.get(it) as JSONObject
                val reportHistory = Gson().fromJson(jsonObj.toString(), tta.astrologerapp.talktoastro.model.ReportHistoryModel::class.java)
                arrOfReportHistoryModel.add(reportHistory)
            }
            arrayMutableReportHistory.postValue(arrOfReportHistoryModel)

        }

    }
}

interface JSONConvertable {
    fun toJSON(): String = Gson().toJson(this)
}

inline fun <reified T: JSONConvertable> String.toObject(): T = Gson().fromJson(this, T::class.java)