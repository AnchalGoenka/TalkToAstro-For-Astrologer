package tta.astrologerapp.talktoastro.agora.chat

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.model.MessageList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewChatHistoryAdapter(val list : ArrayList<MessageList>): RecyclerView.Adapter<ViewChatHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)  {
        val  layoutAStro= itemView.findViewById<RelativeLayout>(R.id.rl_astro)
        val  mAstroName=itemView.findViewById(R.id.tv_astro_name) as TextView
        val  mAstroMsg=itemView.findViewById(R.id.tv_astro_msg) as TextView
        val  mAstroTime=itemView.findViewById(R.id.tv_astro_TimeStamp) as TextView
        val  mImageAstro = itemView.findViewById<ImageView>(R.id.iv_img_astro)

        val  layoutUser= itemView.findViewById<RelativeLayout>(R.id.rl_user)
        val  mUserName=itemView.findViewById(R.id.tv_user_name) as TextView
        val  mUserMsg=itemView.findViewById(R.id.tv_user_msg) as TextView
        val  mUserTime=itemView.findViewById(R.id.tv_userTimeStamp) as TextView
        val  mImageUser = itemView.findViewById<ImageView>(R.id.iv_img_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.agora_msg_list,parent,false)
        return ViewHolder(v)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = list[position]
       /* val sdf = SimpleDateFormat("hh:mm:ss")
        val dateStart: Date = sdf.parse(item.sent_at)
       // val currentDate = sdf.format(Date())*/
     /*   val dateTime = LocalDateTime.parse("2016-12-08T10:20:30")
     val time =   dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))

        val formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:sspm")
         var  str :String
        if(item.sent_at!= null){
            str = item.sent_at
        }
        else{
            str = "2021-02-17 03:40:07"
        }

        val timeq = LocalTime.parse(str, formatter)
        println(time)*/
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val output = SimpleDateFormat("HH:mm aa")

        var d: Date? = null

        try {
            if(item.sent_at!=null){
                d = input.parse(item.sent_at)

            }else{
                d = input.parse("2018-02-05 00:00:00.503")

            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val formatted: String = output.format(d)
        Log.d("DATE", "" + formatted)

       // System.out.println(" C DATE is "+dateStart)

        if(item.message==null){
            val msg :String? = Html.fromHtml(item.message.toString()).toString()
            if(item.type=="astrologer"){
                holder.mUserMsg.text = msg
                holder.mUserTime.text = formatted
                holder.mUserMsg.visibility = View.VISIBLE
                holder.mAstroTime.visibility = View.GONE
                holder.mUserName.visibility = View.GONE


            }
            else if(item.type=="user"){

                holder.mAstroMsg.text =msg
                holder.mAstroTime.text = formatted
                holder.mAstroMsg.visibility = View.VISIBLE
                holder.mUserTime.visibility = View.GONE
                holder.mAstroName.visibility = View.GONE
            }
        }
        else{
            val msg = Html.fromHtml(item.message.toString())
            if(item.type=="astrologer"){
                holder.mUserMsg.text = msg
                holder.mUserTime.text = formatted
                holder.mUserMsg.visibility = View.VISIBLE
                holder.mAstroTime.visibility = View.GONE
                holder.mUserName.visibility = View.GONE


            }
            else if(item.type=="user"){

                holder.mAstroMsg.text =msg
                holder.mAstroTime.text = formatted
                holder.mAstroMsg.visibility = View.VISIBLE
                holder.mUserTime.visibility = View.GONE
                holder.mAstroName.visibility = View.GONE
            }
        }

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}