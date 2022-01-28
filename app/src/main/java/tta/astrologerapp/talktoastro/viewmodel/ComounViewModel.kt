package tour.traveling.travel.ui.product

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tta.astrologerapp.talktoastro.model.CallHistoryModel
import tta.astrologerapp.talktoastro.model.ChangStatus
import tta.astrologerapp.talktoastro.model.TokenModel

class ComounViewModel : ViewModel() {

    lateinit var mRepo: MainRespository
    init {
        mRepo = MainRespository()
    }


    fun callSaveToken(udid: String,device_token: String,user_id: String): LiveData<TokenModel> {
        return mRepo.callSaveToken(udid,device_token,user_id)
    }


    fun changeChatStatus(req: ChangStatus): LiveData<CallHistoryModel> {
        return mRepo.changeChatStatus(req)
    }

}