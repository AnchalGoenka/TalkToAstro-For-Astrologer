package tta.astrologerapp.talktoastro.chat.views

import android.content.Context
import com.twilio.chat.CallbackListener
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import eu.inloop.simplerecycleradapter.SettableViewHolder
import kotterknife.bindView
import org.jetbrains.anko.*
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.chat.ChannelModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChannelViewHolder : SettableViewHolder<ChannelModel>, AnkoLogger {
    val friendlyName: TextView by bindView(R.id.chat_history_user_name_val)
    val chatDuration: TextView by bindView(R.id.chat_history_conversatio_dur_val)
    val status: TextView by bindView(R.id.chat_history_status_val)
    //val channelSid: TextView by bindView(R.id.channel_sid)
//    val updatedDate: TextView by bindView(R.id.channel_updated_date)
//    val createdDate: TextView by bindView(R.id.channel_created_date)
//    val usersCount: TextView by bindView(R.id.channel_users_count)
//    val totalMessagesCount: TextView by bindView(R.id.channel_total_messages_count)
//    val unconsumedMessagesCount: TextView by bindView(R.id.channel_unconsumed_messages_count)
//    val lastMessageDate: TextView by bindView(R.id.channel_last_message_date)
//    val pushesLevel: TextView by bindView(R.id.channel_pushes_level)

    constructor(context: Context, parent: ViewGroup)
            : super(context, R.layout.channel_item_layout, parent)
    {}

    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("MMM dd, yyyy")
        return format.format(date)
    }

    override fun setData(channel: ChannelModel) {
        warn { "setData for ${channel.friendlyName} sid|${channel.sid}|" }
        val simpleDateFormat =
            SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        try {
            val date1 = channel.dateCreatedAsDate
            val date2 = channel.lastMessageDate
            if (date1 == null || date2 == null) {
                chatDuration.text = "00:00"
            } else {
                chatDuration.text = printDifference(date1!!, date2!!)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        friendlyName.text = channel.friendlyName
        status.text = channel.status.toString()

        if (channel.dateUpdatedAsDate != null){
            val dateFormated = toSimpleString(channel.dateUpdatedAsDate!!)
            //updatedDate.text = "Last Updated On: $dateFormated"
        } else
            "<no updated date>"



        if (channel.dateCreatedAsDate != null) {
            val dateFormated = toSimpleString(channel.dateUpdatedAsDate!!)
            //createdDate.text = "Created on: $dateFormated"
        }
        else
            "<no created date>"

//        pushesLevel.text = if (channel.notificationLevel == NotificationLevel.MUTED)
//            "Pushes: Muted"
//        else
//            "Pushes: Default"

        channel.getUnconsumedMessagesCount(object : CallbackListener<Long>() {
            override fun onSuccess(value: Long?) {
                Log.d("ChannelViewHolder", "getUnconsumedMessagesCount callback")
                //unconsumedMessagesCount.text = "Unread " + value!!.toString()
            }
        })

        channel.getMessagesCount(object : CallbackListener<Long>() {
            override fun onSuccess(value: Long?) {
                Log.d("ChannelViewHolder", "getMessagesCount callback")
                //totalMessagesCount.text = "Messages " + value!!.toString()
            }
        })

        channel.getMembersCount(object : CallbackListener<Long>() {
            override fun onSuccess(value: Long?) {
                Log.d("ChannelViewHolder", "getMembersCount callback")
                //usersCount.text = "Members " + value!!.toString()
            }
        })

        val lastmsg = channel.lastMessageDate;
        if (lastmsg != null) {
            //lastMessageDate.text = lastmsg.toString()
        }

        itemView.setBackgroundColor(
            Color.WHITE
//            if (channel.status == ChannelStatus.JOINED) {
//                Color.WHITE
////                if (channel.type == ChannelType.PRIVATE)
////                    Color.BLUE
////                else
////                    Color.WHITE
//            } else {
//                if (channel.status == ChannelStatus.INVITED)
//                    Color.YELLOW
//                else
//                    Color.GRAY
//            }
        )
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
}
