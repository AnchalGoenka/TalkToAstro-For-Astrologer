package tta.astrologerapp.talktoastro.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import com.google.gson.Gson
import org.json.JSONObject


/**

 * Created by Vivek Singh on 2019-09-18.

 */
class ReportDetailViewModel constructor(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
    BaseViewModel(mApplication), JSONConvertable {
    var arrayMutableReportDetail = MutableLiveData<ArrayList<tta.astrologerapp.talktoastro.model.ReportDetailModel>>()
    var arrReportDetail = ArrayList<tta.astrologerapp.talktoastro.model.ReportDetailModel>()
    var reportBirthDetail = MutableLiveData<tta.astrologerapp.talktoastro.model.ReportBirthDetailModel>()
    var reportCommentResp = MutableLiveData<String>()

    fun getReportDetails(json: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTDETAIL.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.REPORTCOMMENTS, json, null, null, false
        )
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTREQUIREMENT.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.REPORT_REQIUREMENT, json, null, null, false
        )
    }

    fun submitReportComments(json: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTCOMMENTSUBMIT.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.REPORT_COMMENTS_SUBMIT, json, null, null, false
        )
    }

    override fun onApiResponse(
        identifier: Int,
        response: JSONObject,
        serverDate: Long,
        lastModified: Long
    ) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTDETAIL.ordinal) {
            arrReportDetail.clear()
            response.keys().forEach {
                val jsonObj = response.get(it) as JSONObject
                val reportDetail =
                    Gson().fromJson(jsonObj.toString(), tta.astrologerapp.talktoastro.model.ReportDetailModel::class.java)
                arrReportDetail.add(reportDetail)
            }
            arrayMutableReportDetail.postValue(arrReportDetail)
        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTREQUIREMENT.ordinal) {
            val jsonObj = response.get("0") as JSONObject
            val birthDetail =
                Gson().fromJson(jsonObj.toString(), tta.astrologerapp.talktoastro.model.ReportBirthDetailModel::class.java)
            reportBirthDetail.postValue(birthDetail)

            arrayMutableReportDetail.postValue(arrReportDetail)
        }
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.REPORTCOMMENTSUBMIT.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), tta.astrologerapp.talktoastro.model.TokenModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.TokenModel>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.TokenModel) {
                        if (data.success == 1) {
                            reportCommentResp.postValue(data.message)
                        } else {
                            Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), data.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.TokenModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                    }
                })
        }

    }

}