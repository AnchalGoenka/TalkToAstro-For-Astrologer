package tta.astrologerapp.talktoastro.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ReportHistoryModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("user_id")
    val user_id: String?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("service_id")
    val service_id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("place")
    val place: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("requirement")
    val requirement: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
): Parcelable {
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
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(id)
        parcel?.writeString(user_id)
        parcel?.writeString(userName)
        parcel?.writeString(service_id)
        parcel?.writeString(name)
        parcel?.writeString(gender)
        parcel?.writeString(place)
        parcel?.writeString(date)
        parcel?.writeString(time)
        parcel?.writeString(requirement)
        parcel?.writeString(message)
        parcel?.writeString(status)
        parcel?.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportHistoryModel> {
        override fun createFromParcel(parcel: Parcel): ReportHistoryModel {
            return ReportHistoryModel(parcel)
        }

        override fun newArray(size: Int): Array<ReportHistoryModel?> {
            return arrayOfNulls(size)
        }
    }
}