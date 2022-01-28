package network

import retrofit2.Call
import retrofit2.http.*
import tta.astrologerapp.talktoastro.model.CallHistoryModel
import tta.astrologerapp.talktoastro.model.ChangStatus
import tta.astrologerapp.talktoastro.model.TokenModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant


/**
 * Created by Avnish on 2/4/18.
 */
interface AppService {


    //    //OTP Generate API
//    @FormUrlEncoded
//    @POST(NetworkConstants.OTP_GENERATE)
//    fun callOTP_Api(
//        @Field("user_mobile") user_mobile: String
//    ): Call<OTPGenerateResponce>


    // Home Banner List
    @POST(ApplicationConstant.SAVE_TOKEN)
    fun callSaveToken(
        @Query("udid") udid: String,
        @Query("device_token") device_token: String,
        @Query("user_id") user_id: String
    ): Call<TokenModel>


    @POST(ApplicationConstant.CHAT_STATUS)
    fun changeChatStatus(@Body req: ChangStatus
    ): Call<CallHistoryModel>

}

