package tta.astrologerapp.talktoastro.ui.astroprofile

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**

 * Created by Vivek Singh on 2019-06-10.

 */
data class AstrologerListModel (
    @SerializedName("id")
    val id : String?,
    @SerializedName("first_name")
    val firstName : String?,
    @SerializedName("last_name")
    val lastName : String?,
    @SerializedName("url")
    val url : String?,
    @SerializedName("meta_title")
    val metaTitle : String?,
    @SerializedName("meta_description")
    val metaDescription : String?,
    @SerializedName("totalRatings")
    val totalRatings : String?,
    @SerializedName("phone_status")
    var phoneStatus : String?,
    @SerializedName("image")
    val image : String?,
    @SerializedName("expertise")
    val expertise : String?,
    @SerializedName("experience")
    val experience : String?,
    @SerializedName("price")
    val price : String?,
    @SerializedName("foreigndisplayprice")
    val foreigndisplayprice: String? = "",
    @SerializedName("languages")
    val languages : String?,
    @SerializedName("ratingAvg")
    val ratingAvg : String?,
    @SerializedName("about")
    val about : String?,
    @SerializedName("isReport")
    val isReport : String?,
    @SerializedName("orderBy")
    val orderBy : String?,
    @SerializedName("call_min")
    val callMin: String?,
    @SerializedName("report_num")
    val reportNum: String?,
    @SerializedName("audio")
    val audio: String?

): Parcelable{
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(id)
        parcel?.writeString(firstName)
        parcel?.writeString(lastName)
        parcel?.writeString(url)
        parcel?.writeString(metaTitle)
        parcel?.writeString(metaDescription)
        parcel?.writeString(totalRatings)
        parcel?.writeString(phoneStatus)
        parcel?.writeString(image)
        parcel?.writeString(expertise)
        parcel?.writeString(experience)
        parcel?.writeString(price)
        parcel?.writeString(foreigndisplayprice)
        parcel?.writeString(languages)
        parcel?.writeString(ratingAvg)
        parcel?.writeString(about)
        parcel?.writeString(isReport)
        parcel?.writeString(orderBy)
        parcel?.writeString(callMin)
        parcel?.writeString(reportNum)
        parcel?.writeString(audio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel> {
        override fun createFromParcel(parcel: Parcel): tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel {
            return tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<tta.astrologerapp.talktoastro.ui.astroprofile.AstrologerListModel?> {
            return arrayOfNulls(size)
        }
    }
}