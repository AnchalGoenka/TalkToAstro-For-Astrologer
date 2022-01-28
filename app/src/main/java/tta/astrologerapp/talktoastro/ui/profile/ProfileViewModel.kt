package tta.astrologerapp.talktoastro.ui.profile

import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import org.json.JSONObject

class ProfileViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app)  {
    val arrayMutableUserProfile = MutableLiveData<tta.astrologerapp.talktoastro.model.UserProfile>()
    val arrayEditMutable = MutableLiveData<Boolean>()
    init {
    }

    fun getUserProfile(jsonObj: JSONObject?) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.USERPROFILE.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERPROFILE, jsonObj, null, null, false)
    }

    fun updateProfile(param: HashMap<String, String>?){
        val json = JSONObject(param as Map<*, *>)
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.EDITUSERPROFILE.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.EDITUSERPROFILE, json, null, null, false)
    }

    override fun onApiResponse(identifier: Int, response: JSONObject, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.USERPROFILE.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), tta.astrologerapp.talktoastro.model.UserProfile::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.UserProfile>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.UserProfile) {
                        arrayMutableUserProfile.postValue(data)
                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.UserProfile) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                    }
                })
        }
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.EDITUSERPROFILE.ordinal) {
            if (response["success"] == 1){
                arrayEditMutable.postValue(true)
            } else {
                arrayEditMutable.postValue(false)
            }
        }
    }
}