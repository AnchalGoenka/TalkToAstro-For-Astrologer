package tta.astrologerapp.talktoastro.ui.astroprofile

import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.*
import tta.astrologerapp.talktoastro.volley.ext.RequestType
import org.json.JSONObject

class AstroProfileViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication) : BaseViewModel(app) {

    var arrayListMutableLiveData = MutableLiveData<tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel>()
    var astroProfileMutableLiveData = MutableLiveData<tta.astrologerapp.talktoastro.model.AstroProfileModel>()


    init {

    }

    fun getAstrologersList(){
        doApiRequest(
            RequestType.GET, tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROLOGER_LIST.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.ASTROLOGER_LIST, null, null, null, false)
    }

    fun getAstroProfile(json: JSONObject) {
        doApiRequest(
            RequestType.POST, tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROPROFILE.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.ASTRO_PROFILE, json, null, null, false
        )
    }


    override fun onApiResponse(identifier: Int, response: String, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if(identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROLOGER_LIST.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response, tta.astrologerapp.talktoastro.ui.astroprofile.ProfileList::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.ui.astroprofile.ProfileList>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.ui.astroprofile.ProfileList) {
                        loop@ for(it in data.astrologerModel) {
                            val astrologerList: tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel =
                                tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel(
                                    it.id,
                                    it.firstName,
                                    it.lastName,
                                    it.url,
                                    it.metaTitle,
                                    it.metaDescription,
                                    it.totalRatings,
                                    it.phoneStatus,
                                    it.image,
                                    it.expertise,
                                    "${it.experience} years",
                                    it.price,
                                    "${it.foreigndisplayprice} / Min",
                                    it.languages,
                                    it.ratingAvg,
                                    it.about,
                                    it.isReport,
                                    it.orderBy,
                                    it.callMin,
                                    it.reportNum,
                                    it.audio



                                )
                           // arrayListMutableLiveData.postValue(astrologerList)
                            val astroID = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                                tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                            if (astrologerList.id == astroID) {
                                arrayListMutableLiveData.postValue(astrologerList)
                                break@loop
                            }
                        }

                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.ui.astroprofile.ProfileList) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error " )
                    }
                })
        }

    }
    override fun onApiResponse(identifier: Int, response: JSONObject, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.ASTROPROFILE.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response.toString(), tta.astrologerapp.talktoastro.model.AstroProfileModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.AstroProfileModel>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.AstroProfileModel) {
                        astroProfileMutableLiveData.postValue(data)
                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.AstroProfileModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error ")
                    }
                })
        }
    }

}