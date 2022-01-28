package tta.astrologerapp.talktoastro.ui.customersupport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tta.astrologerapp.talktoastro.R
import kotlinx.android.synthetic.main.fragment_customer_support.*



class CustomerSupportFragment : Fragment() {

    private lateinit var customerSupportViewModel: tta.astrologerapp.talktoastro.ui.customersupport.CustomerSupportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customerSupportViewModel =
            ViewModelProviders.of(this).get(tta.astrologerapp.talktoastro.ui.customersupport.CustomerSupportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_customer_support, container, false)
        customerSupportViewModel.text.observe(this, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        txv_phone_number.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL);
                intent.data = Uri.parse("tel:${txv_phone_number.text}");
                startActivity(intent);
            } catch (e: Exception) {
                print("exception:$e")
            }
        }
        txv_whatsapp_number.setOnClickListener {
            try {
                val url = "https://api.whatsapp.com/send?phone=${txv_whatsapp_number.text}"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: Exception) {
                print("exception:$e")
            }
        }
        txv_email.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/html"
                intent.putExtra(Intent.EXTRA_EMAIL, txv_email.text)
                startActivity(Intent.createChooser(intent, "Send Email"))
            } catch (e: Exception) {
                print("exception:$e")
            }
        }
        txv_website.makeLinks(
            Pair("www.talktoastro.com", android.view.View.OnClickListener {
                try {
                    val url = tta.astrologerapp.talktoastro.util.ApplicationConstant.talkToAstroURL
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setDataAndType(Uri.parse(url), "text/html");
                    startActivity(i)
                } catch (e: Exception) {
                    print("exception:$e")
                }

            })
        )
    }

    private fun TextView.makeLinks(vararg links: Pair<String, android.view.View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: android.view.View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}