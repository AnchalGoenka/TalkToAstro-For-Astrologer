package tta.astrologerapp.talktoastro.ui.privacypolicy

import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import org.json.JSONObject

class PrivacyPolicyViewModel  constructor(app: tta.astrologerapp.talktoastro.BaseApplication): BaseViewModel(app) {
    var getPrivacyPolicyObserver = MutableLiveData<tta.astrologerapp.talktoastro.model.PrivacyPolicyModel>()

    init {
        getPrivacyPolicy()
    }

    private fun getPrivacyPolicy() {
        doApiRequest(
            RequestType.GET, tta.astrologerapp.talktoastro.volley.RequestIdentifier.PRIVACYPOLICY.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.PRIVACY_POLICY, null, null, null, false)
    }

    override fun onApiResponse(identifier: Int, response: JSONObject, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.PRIVACYPOLICY.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), tta.astrologerapp.talktoastro.model.PrivacyPolicyModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.PrivacyPolicyModel>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.PrivacyPolicyModel) {
                        getPrivacyPolicyObserver.postValue(data)
                    }
                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.PrivacyPolicyModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                    }
                })
        }
    }

}