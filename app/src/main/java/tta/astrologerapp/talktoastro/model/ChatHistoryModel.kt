package tta.astrologerapp.talktoastro.model

import com.google.gson.annotations.SerializedName


/**

 * Created by Vivek Singh on 7/5/20.
 */

data class ChatHistoryModel(
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("astro_id")
    val astroId: String?,
    @SerializedName("chat_id")
    val chatID: String?,
    @SerializedName("session_id")
    val sessionID: String?,
    @SerializedName("chat_duration")
    val chatDuration: String?,
    @SerializedName("chatprice")
    val amount: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("chatTime")
    val chatTime: String?
)