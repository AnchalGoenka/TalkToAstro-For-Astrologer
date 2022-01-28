package tta.astrologerapp.talktoastro.chat.views

import android.content.Context
import android.view.LayoutInflater
import com.twilio.chat.Member
import android.view.ViewGroup
import android.widget.TextView
import eu.inloop.simplerecycleradapter.SettableViewHolder
import kotterknife.bindView
import tta.astrologerapp.talktoastro.R

class MemberViewHolder : SettableViewHolder<Member> {
    private val memberIdentity: TextView by bindView(R.id.identity)
//    private val memberSid: TextView by bindView(R.id.member_sid)

    constructor(context: Context, parent: ViewGroup)
        : super(context, R.layout.member_item_layout, parent)
    {
    }

    override fun setData(member: Member) {
        memberIdentity?.text = member.identity
//        memberSid.text = member.sid
    }
}
