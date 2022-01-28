package tta.astrologerapp.talktoastro.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.databinding.CallHistoryListBinding
import tta.astrologerapp.talktoastro.model.CallHistoryModel
import tta.astrologerapp.talktoastro.model.ChatHistoryModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.LogUtils
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**

 * Created by Vivek Singh on 2019-08-29.

 */
class CallHistoryAdapter(
    private val arrayList: ArrayList<CallHistoryModel>?,
    private val arrayChatHistory: ArrayList<ChatHistoryModel>?,
    private val isChat: Boolean,private val itemClick: (ChatHistoryModel, Int) -> Unit
) : RecyclerView.Adapter<CallHistoryAdapter.customView>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): CallHistoryAdapter.customView {
        val layoutInflator = LayoutInflater.from(parent.context)
        val callListBinding: CallHistoryListBinding =
            DataBindingUtil.inflate(layoutInflator, R.layout.call_history_child_list, parent, false)
        return customView(callListBinding)
    }

    override fun getItemCount(): Int {
        if (isChat) {
            return arrayChatHistory!!.size
        } else {
            return arrayList!!.size
        }
    }

    override fun onBindViewHolder(holder: CallHistoryAdapter.customView, position: Int) {
        try {
            if (isChat) {
                val h1 = holder as CallHistoryAdapter.customView
                val chatListModel = arrayChatHistory!![position]
                h1.bind(null, chatListModel)
            } else {
                val h1 = holder as CallHistoryAdapter.customView
                val callListModel = arrayList!![position]
                h1.bind(callListModel, null)
            }
        } catch (e: Exception) {
            LogUtils.d("onBindViewHolder(): ${e.printStackTrace()}")
        }
    }

    inner class customView(
        val callListBinding: CallHistoryListBinding
    ) : RecyclerView.ViewHolder(callListBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(callListModel: CallHistoryModel?, chatHistoryModel: ChatHistoryModel?) {
            callListBinding.callHistoryList = callListModel
            callListBinding.executePendingBindings()

            val phoneCode = SharedPreferenceUtils.readString(
                ApplicationConstant.PHONECODE, "",
                SharedPreferenceUtils.getSharedPref(
                    ApplicationUtil.getContext()
                )
            )
            if (isChat) {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                formatter.timeZone = TimeZone.getTimeZone("GMT")
                val result = formatter.parse(chatHistoryModel?.chatTime)

                val outputFmt =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val time = outputFmt.format(result)
               // if (chatHistoryModel!!.status == "success" || chatHistoryModel!!.status == "ended") {
                if (chatHistoryModel!!.chatDuration?.toInt()!! >=1 ) {
                    callListBinding.constraintInternal.setBackgroundResource(R.color.colorPrimaryLight_1)
                    callListBinding.callHistoryConversatioDur.visibility = View.VISIBLE
                    callListBinding.callHistoryPrice.visibility = View.VISIBLE
                    callListBinding.callHistoryConversatioDurVal.visibility = View.VISIBLE
                    callListBinding.callHistoryPriceVal.visibility = View.VISIBLE
                    callListBinding.view4.visibility = View.VISIBLE
                    callListBinding.viewPaymentTime.visibility = View.VISIBLE
                    callListBinding.callHistoryAstroNameVal.text = chatHistoryModel.userName
                    callListBinding.callHistoryConversatioDurVal.text = chatHistoryModel.chatDuration
                    callListBinding.callHistoryConversatioDur.text ="Chat Duration"
                    callListBinding.callHistoryCallTimee.text ="Chat Time"
                    callListBinding.callHistoryCallTimeVal.text = time
                    callListBinding.callHistoryPriceVal.text = chatHistoryModel.amount
                    callListBinding.callHistoryStatusVal.text = "Success"
                    if (phoneCode == "91") {
                        callListBinding.callHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_rupee,
                            0,
                            0,
                            0
                        )
                    } else {
                        callListBinding.callHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_dollar,
                            0,
                            0,
                            0
                        )
                    }
                    callListBinding.callHistoryConversatioDurVal.text =
                        "${chatHistoryModel.chatDuration} mins"
                    itemView.setOnClickListener { itemClick(chatHistoryModel, adapterPosition) }
                } else {
                    callListBinding.constraintInternal.setBackgroundResource(R.color.gray_light)
                    callListBinding.callHistoryConversatioDur.visibility = View.GONE
                    callListBinding.callHistoryPrice.visibility = View.GONE
                    callListBinding.view4.visibility = View.GONE
                    callListBinding.viewPaymentTime.visibility = View.GONE
                    callListBinding.callHistoryConversatioDurVal.visibility = View.GONE
                    callListBinding.callHistoryPriceVal.visibility = View.GONE
                    callListBinding.callHistoryAstroNameVal.text = chatHistoryModel.userName
                    callListBinding.callHistoryCallTimeVal.text = time
                    callListBinding.callHistoryStatusVal.text = "Failed"
                }
            } else {
                if (callListModel!!.status == "Success") {
                    callListBinding.constraintInternal.setBackgroundResource(R.color.colorPrimaryLight_1)
                    callListBinding.callHistoryConversatioDur.visibility = View.VISIBLE
                    callListBinding.callHistoryPrice.visibility = View.VISIBLE
                    callListBinding.callHistoryConversatioDurVal.visibility = View.VISIBLE
                    callListBinding.callHistoryPriceVal.visibility = View.VISIBLE
                    callListBinding.view4.visibility = View.VISIBLE
                    callListBinding.viewPaymentTime.visibility = View.VISIBLE
                    if (phoneCode == "91") {
                        callListBinding.callHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_rupee,
                            0,
                            0,
                            0
                        )
                    } else {
                        callListBinding.callHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_dollar,
                            0,
                            0,
                            0
                        )
                    }
                    callListBinding.callHistoryConversatioDurVal.text =
                        "${callListModel.conversationDuration} mins"
                } else {
                    callListBinding.constraintInternal.setBackgroundResource(R.color.gray_light)
                    callListBinding.callHistoryConversatioDur.visibility = View.GONE
                    callListBinding.callHistoryPrice.visibility = View.GONE
                    callListBinding.view4.visibility = View.GONE
                    callListBinding.viewPaymentTime.visibility = View.GONE
                    callListBinding.callHistoryConversatioDurVal.visibility = View.GONE
                    callListBinding.callHistoryPriceVal.visibility = View.GONE
                }
            }

        }
    }
}