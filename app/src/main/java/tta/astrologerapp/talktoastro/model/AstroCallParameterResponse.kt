package tta.astrologerapp.talktoastro.model

import com.google.gson.annotations.SerializedName

data class AstroCallParameterResponse (

    @SerializedName("call_pickup_rate")
    val call_pickup_rate: String,

    @SerializedName("repeat_order")
    val repeat_order: String

)