package tta.astrologerapp.talktoastro.chat.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tta.astrologerapp.talktoastro.BaseApplication
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.chat.ChannelModel
import tta.astrologerapp.talktoastro.chat.models.ChatHistoryModel
import tta.astrologerapp.talktoastro.databinding.ChatHistoryListBinding
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


/**

 * Created by Vivek Singh on 7/18/20.

 */
class ChannelViewAdapter(private var chatHistoryModelList: ArrayList<ChatHistoryModel>, private val itemClick: (ChatHistoryModel, Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItems = chatHistoryModelList
    private var channelsList = HashMap<String, ChannelModel>()

    fun updateChatHistoryList(newChatHistoryList: List<ChatHistoryModel>?, channels: HashMap<String, ChannelModel>) {
        if (newChatHistoryList != null) {
            mItems.clear()
            mItems.addAll(newChatHistoryList)
        }
        channelsList = channels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val chatHistoryListBinding: ChatHistoryListBinding =
            DataBindingUtil.inflate(inflator, R.layout.channel_item_layout, parent, false)
        return customView(chatHistoryListBinding, itemClick)
        //return ChannelHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            val h1 = holder as ChannelViewAdapter.customView
            val chatHistoryListModel = mItems!![position]
            h1.bind(chatHistoryListModel, position)
        } catch (e: Exception) {
            tta.astrologerapp.talktoastro.util.LogUtils.d("onBindViewHolder(): ${e.printStackTrace()}")
        }
    }

    fun printDifference(startDate: Date, endDate: Date): String {
        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return "${elapsedHours.toString()}:${elapsedMinutes.toString()}:${elapsedSeconds.toString()}"

    }

    fun clear() {
        mItems.clear()
    }

    fun addItems(items: List<ChatHistoryModel>) {
        addItems(items, false)
    }

    fun addItems(
        items: List<ChatHistoryModel>,
        notifyInserted: Boolean
    ) {
        mItems.addAll(items)
        if (notifyInserted) notifyItemRangeInserted(mItems.size - items.size - 1, items.size)
    }

    //class ChannelHistoryViewHolder(var view: View): RecyclerView.ViewHolder(view)

    inner class customView(
        val chatHistoryListBinding: ChatHistoryListBinding,
        val itemClick: (ChatHistoryModel, Int) -> Unit
    ) : RecyclerView.ViewHolder(chatHistoryListBinding.root) {
        fun bind(chatHistory: ChatHistoryModel, position: Int) {
            val phoneCode = SharedPreferenceUtils.readString(
                ApplicationConstant.PHONECODE, "",
                SharedPreferenceUtils.getSharedPref(
                    ApplicationUtil.getContext()
                )
            )
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("GMT")
            val result = formatter.parse(chatHistory.chatTime)

            val outputFmt =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val time = outputFmt.format(result)

              if(chatHistory.status == "success" || chatHistory.status == "ended"){
                  chatHistoryListBinding.constraintLayout.setBackgroundResource(R.color.colorPrimaryLight_1)
                  chatHistoryListBinding.chatHistoryConversatioDur.visibility = View.VISIBLE
                  chatHistoryListBinding.chatHistoryPrice.visibility = View.VISIBLE
                  chatHistoryListBinding.chatHistoryConversatioDurVal.visibility = View.VISIBLE
                  chatHistoryListBinding.chatHistoryPriceVal.visibility = View.VISIBLE
                  chatHistoryListBinding.view4.visibility = View.VISIBLE
                  chatHistoryListBinding.viewPaymentTime.visibility = View.VISIBLE
                  chatHistoryListBinding.chatHistoryUserName.text = chatHistory.userName
                 // chatHistoryListBinding.chatHistoryConversatioDurVal.text = chatHistory.chatDuration
                  chatHistoryListBinding.chatHistoryChatTimee.text ="Chat Time"
                  chatHistoryListBinding.chatHistoryChatTimeVal.text = time
                  chatHistoryListBinding.chatHistoryPriceVal.text = chatHistory.chatprice
                  chatHistoryListBinding.chatHistoryStatusVal.text = "Success"
                  if (phoneCode == "91") {
                      chatHistoryListBinding.chatHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                          R.drawable.ic_rupee,
                          0,
                          0,
                          0
                      )
                  } else {
                      chatHistoryListBinding.chatHistoryPriceVal.setCompoundDrawablesWithIntrinsicBounds(
                          R.drawable.ic_dollar,
                          0,
                          0,
                          0
                      )
                  }
                  chatHistoryListBinding.chatHistoryConversatioDurVal.text =
                      "${chatHistory.chatDuration} mins"

            }else  {
                  chatHistoryListBinding.constraintLayout.setBackgroundResource(R.color.gray_light)
                  chatHistoryListBinding.chatHistoryPrice.visibility =View.GONE
                  chatHistoryListBinding.viewPaymentTime.visibility=View.GONE
                  chatHistoryListBinding.chatHistoryChatTimee.text ="Chat Time"
                  chatHistoryListBinding.chatHistoryStatusVal.text = "Failed"
                  chatHistoryListBinding.chatHistoryPriceVal.visibility=View.GONE
                  chatHistoryListBinding.chatHistoryConversatioDur.visibility=View.GONE
                  chatHistoryListBinding.view4.visibility = View.GONE
                  chatHistoryListBinding.chatHistoryConversatioDurVal.visibility=View.GONE
            }

            chatHistoryListBinding.chatHistoryChatTimeVal.text = time


            val simpleDateFormat =
                SimpleDateFormat("dd/M/yyyy hh:mm:ss")

            try {
                for (chanObj in channelsList) {
                    if (chatHistory.sessionID == chanObj.value.sid) {
                        val date1 = chanObj.value.dateCreatedAsDate
                        val date2 = chanObj.value.lastMessageDate
                        if (date1 == null || date2 == null) {
                            chatHistoryListBinding.chatHistoryConversatioDurVal.text = "00:00"
                        } else {
                            chatHistoryListBinding.chatHistoryConversatioDurVal.text = printDifference(date1!!, date2!!)
                        }
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            chatHistoryListBinding.chatHistoryUserNameVal.text = chatHistory.userName
            itemView.setOnClickListener { itemClick(chatHistory, adapterPosition) }
        }
    }

}