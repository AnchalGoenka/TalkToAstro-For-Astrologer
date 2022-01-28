package tta.astrologerapp.talktoastro.model


import com.google.gson.annotations.SerializedName

data class CallHistoryModel(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("call_id")
    val callId: String,
    @SerializedName("callTime")
    val callTime: String,
    @SerializedName("conversation_duration")
    val conversationDuration: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("status")
    val status: String
)