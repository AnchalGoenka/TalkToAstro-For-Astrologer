package tta.astrologerapp.talktoastro.model


import com.google.gson.annotations.SerializedName

data class AstroProfileModel(
    @SerializedName("about")
    val about: String,
    @SerializedName("availability")
    val availability: List<Availability>,
    @SerializedName("experience")
    val experience: String,
    @SerializedName("expertise")
    val expertise: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("INRPrice")
    val iNRPrice: Int,
    @SerializedName("isReport")
    val isReport: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("reviews")
    val reviews: ArrayList<Review>,
    @SerializedName("USDDisplayPrice")
    val uSDDisplayPrice: String,
    @SerializedName("USDPrice")
    val uSDPrice: Int
)

data class Availability(
    @SerializedName("day")
    val day: String,
    @SerializedName("time")
    val time: String
)

data class Review(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("rating")
    val rating: String
)