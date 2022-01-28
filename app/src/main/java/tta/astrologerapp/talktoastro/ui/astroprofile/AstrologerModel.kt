package tta.astrologerapp.talktoastro.ui.astroprofile

import com.google.gson.annotations.SerializedName


/**

 * Created by Vivek Singh on 2019-06-10.

 */
class ProfileList (
    @SerializedName("data")
    val astrologerModel: ArrayList<tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerModel> = ArrayList<tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerModel>()
)

class AstrologerModel(
    @SerializedName("id")
    val id: String? ,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String? ,
    @SerializedName("url")
    val url: String? ,
    @SerializedName("meta_title")
    val metaTitle: String?,
    @SerializedName("meta_description")
    val metaDescription: String?,
    @SerializedName("totalRatings")
    val totalRatings: String? ,
    @SerializedName("phone_status")
    val phoneStatus: String? ,
    @SerializedName("image")
    val image: String? ,
    @SerializedName("expertise")
    val expertise: String? ,
    @SerializedName("experience")
    val experience: String? ,
    @SerializedName("price")
    val price: String? = "",
    @SerializedName("foreigndisplayprice")
    val foreigndisplayprice: String? ,
    @SerializedName("languages")
    val languages: String? ,
    @SerializedName("ratingAvg")
    val ratingAvg: String? ,
    @SerializedName("about")
    val about: String?,
    @SerializedName("isReport")
    val isReport: String? ,
    @SerializedName("orderBy")
    val orderBy : String?,
    @SerializedName("call_min")
    val callMin: String?,
    @SerializedName("report_num")
    val reportNum: String?,
    @SerializedName("audio")
    val audio: String?

)

data class CallModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Int?
)