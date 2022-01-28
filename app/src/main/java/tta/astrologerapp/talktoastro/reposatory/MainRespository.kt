package tour.traveling.travel.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import network.AppRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tta.astrologerapp.talktoastro.model.CallHistoryModel
import tta.astrologerapp.talktoastro.model.ChangStatus
import tta.astrologerapp.talktoastro.model.TokenModel

class MainRespository {

    fun callSaveToken(udid: String,device_token: String,user_id: String): LiveData<TokenModel> {
        val data = MutableLiveData<TokenModel>()
        AppRetrofit.instance.callSaveToken(udid,device_token,user_id).enqueue(object : Callback<TokenModel> {
            override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {

                if (response.isSuccessful){
                    data.value = if (response != null && response.body() != null) response!!.body() else null
                }
                else {
                    val gson = Gson()
                    val adapter = gson.getAdapter(TokenModel::class.java)
                    if (response.errorBody() != null)
                        data.value = adapter.fromJson(response.errorBody()!!.string())
                }
            }
        })

        return data
    }


    fun changeChatStatus(req: ChangStatus): LiveData<CallHistoryModel> {
        val data = MutableLiveData<CallHistoryModel>()
        AppRetrofit.instance.changeChatStatus(req).enqueue(object : Callback<CallHistoryModel> {
            override fun onFailure(call: Call<CallHistoryModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<CallHistoryModel>, response: Response<CallHistoryModel>) {

                if (response.isSuccessful){
                    data.value = if (response != null && response.body() != null) response!!.body() else null
                }
                else {
                    val gson = Gson()
                    val adapter = gson.getAdapter(CallHistoryModel::class.java)
                    if (response.errorBody() != null)
                        data.value = adapter.fromJson(response.errorBody()!!.string())
                }
            }
        })

        return data
    }


}