package tta.astrologerapp.talktoastro.ui.reports

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.viewmodel.ReportDetailViewModel
import kotlinx.android.synthetic.main.fragment_report_detail.*
import org.json.JSONObject
import tta.astrologerapp.talktoastro.model.ReportHistoryModel

class ReportDetailFragment : tta.astrologerapp.talktoastro.BaseFragment() {
    private var reportDetailViewModel: ReportDetailViewModel? = null
    private var reportDetailAdapter: tta.astrologerapp.talktoastro.adapter.ReportDetailAdapter? = null
    override val layoutResId: Int
        get() = R.layout.fragment_report_detail

    override fun getToolbarId(): Int {
        return 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reportDetailViewModel = ViewModelProviders.of(this,
            tta.astrologerapp.talktoastro.ui.reports.ReportDetailFragment.MyViewModelFactory(
                tta.astrologerapp.talktoastro.BaseApplication.instance
            )
        ).get(
            ReportDetailViewModel::class.java)
        val reportData = arguments?.getParcelable<ReportHistoryModel>("reportHistoryModel") as tta.astrologerapp.talktoastro.model.ReportHistoryModel
        var json = JSONObject()
        json.put("reportID", reportData.id)
        reportDetailViewModel?.getReportDetails(json)
        reportDetailViewModel?.arrayMutableReportDetail?.observe(this, Observer {
            val recycler_report_detail: androidx.recyclerview.widget.RecyclerView = myActivity.findViewById(R.id.recycler_report_detail)
            reportDetailAdapter =
                tta.astrologerapp.talktoastro.adapter.ReportDetailAdapter(it!!)
            recycler_report_detail.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
            recycler_report_detail.adapter = reportDetailAdapter
            if (recycler_report_detail.layoutManager == null){
                recycler_report_detail.addItemDecoration(
                    tta.astrologerapp.talktoastro.util.MarginItemDecorator(
                        1
                    )
                )
            }
            recycler_report_detail.hasFixedSize()
        })
        reportDetailViewModel?.reportBirthDetail?.observe(this, Observer {
            textView_name.text = it.name
            textView_location.text = it.place
            textView_date.text = it.date
            textView_time.text = it.time
            textView_requirement.text = Html.fromHtml(it.requirement.replace("\n"," "))
        })
        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reportHistoryModel = arguments?.getParcelable<ReportHistoryModel>("reportHistoryModel") as tta.astrologerapp.talktoastro.model.ReportHistoryModel
        val serviceID = reportHistoryModel.service_id
        if (reportHistoryModel.status == "completed"){
            textInputLayout_report_comments.isEnabled = false
            button_submit_comment_report.isEnabled = false
        } else {
            textInputLayout_report_comments.isEnabled = true
            button_submit_comment_report.isEnabled = true
        }

        button_submit_comment_report.setOnClickListener {
            val astroId = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "",
                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())).toString()
            if (textInputLayout_report_comments.text.isNotEmpty()) {
                var json = JSONObject()
                json.put("reportID", reportHistoryModel.id)
                json.put("userID", reportHistoryModel.user_id)
                json.put("reportComment", textInputLayout_report_comments.text)
                reportDetailViewModel?.submitReportComments(json)
                reportDetailViewModel?.reportCommentResp?.observe(this, Observer {
                    textInputLayout_report_comments.text.clear()
                    reportDetailViewModel?.getReportDetails(json)
                })
            }
        }

    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ReportDetailViewModel(mApplication) as T
        }
    }


    companion object {
        internal val tagName
            get() = tta.astrologerapp.talktoastro.ui.reports.ReportDetailFragment::class.java.name

        fun newInstance(bundle: Bundle?): tta.astrologerapp.talktoastro.ui.reports.ReportDetailFragment {
            val fragment =
                tta.astrologerapp.talktoastro.ui.reports.ReportDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
