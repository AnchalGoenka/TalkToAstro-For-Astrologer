package tta.astrologerapp.talktoastro.adapter

import androidx.databinding.DataBindingUtil
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.databinding.ReportDetailListBinding
import tta.astrologerapp.talktoastro.model.ReportDetailModel
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.LogUtils
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils

/**

 * Created by Vivek Singh on 2019-09-19.

 */
class ReportDetailAdapter(private val arrayList: ArrayList<ReportDetailModel>): androidx.recyclerview.widget.RecyclerView.Adapter<ReportDetailAdapter.customView>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ReportDetailAdapter.customView {
        val layoutInflator = LayoutInflater.from(parent.context)
        val reportListBinding: ReportDetailListBinding =
            DataBindingUtil.inflate(layoutInflator, R.layout.report_detail_child_list, parent, false)
        return customView(reportListBinding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: ReportDetailAdapter.customView, position: Int) {
        try {
            val h1 = holder as ReportDetailAdapter.customView
            val reportListModel = arrayList!![position]
            h1.bind(reportListModel)
        } catch (e: Exception) {
            LogUtils.d("onBindViewHolder(): ${e.printStackTrace()}")
        }
    }

    inner class customView(
        val reportListBinding: ReportDetailListBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(reportListBinding.root) {

        fun bind(reportListModel: ReportDetailModel) {
            reportListBinding.reportDetailList = reportListModel
            reportListBinding.executePendingBindings()
            LogUtils.d("reportDetail: ${reportListModel}")
            reportListBinding.txvReportDetailMsg.text = Html.fromHtml(reportListModel.comment)
            if (reportListModel.userId == SharedPreferenceUtils.readString(
                    ApplicationConstant.USERID, "",
                    SharedPreferenceUtils.getSharedPref(
                        ApplicationUtil.getContext()))) {
                reportListBinding.txvReportDetailName.text = "You:"
            } else {
                reportListBinding.txvReportDetailName.text = "User:"
            }

            reportListBinding.txvReportDetailUpdatedAt.text = reportListModel.updatedAt
        }
    }
}