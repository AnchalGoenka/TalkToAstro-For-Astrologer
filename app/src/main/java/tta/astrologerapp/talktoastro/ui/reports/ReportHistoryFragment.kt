package tta.astrologerapp.talktoastro.ui.reports

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.viewmodel.ReportHistoryViewModel


class ReportHistoryFragment : tta.astrologerapp.talktoastro.BaseFragment() {

    var reportHistoryViewModel: ReportHistoryViewModel? = null
    var reportHistoryAdapter: tta.astrologerapp.talktoastro.adapter.ReportHistoryAdapter? = null
    private lateinit var navController: NavController
    override fun getToolbarId(): Int {
        return 0
    }

    override val layoutResId: Int
        get() = R.layout.fragment_report_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = myActivity.findNavController(R.id.nav_host_fragment)
        reportHistoryViewModel = ViewModelProviders.of(this,
            tta.astrologerapp.talktoastro.ui.reports.ReportHistoryFragment.MyViewModelFactory(
                tta.astrologerapp.talktoastro.BaseApplication.instance
            )
        ).get(ReportHistoryViewModel::class.java)
        reportHistoryViewModel?.arrayMutableReportHistory?.observe(this, Observer {
            reportHistoryAdapter =
                tta.astrologerapp.talktoastro.adapter.ReportHistoryAdapter(it!!) { reportHistoryModel: tta.astrologerapp.talktoastro.model.ReportHistoryModel, i: Int ->
                    var bundle = Bundle()
                    bundle.putParcelable("reportHistoryModel", reportHistoryModel)
                    navController.navigate(R.id.nav_report_detail, bundle)
//                    var transaction: FragmentTransaction =
//                        myActivity.supportFragmentManager.beginTransaction()
//                    transaction.replace(
//                        R.id.nav_host_fragment,
//                        ReportDetailFragment.newInstance(bundle),
//                        ReportDetailFragment.tagName
//                    )
//                    transaction.addToBackStack(ReportDetailFragment.tagName)
//                    transaction.commit()
                }
            val report_history_list: androidx.recyclerview.widget.RecyclerView = myActivity.findViewById(R.id.report_history_list)
            report_history_list.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
            report_history_list.adapter = reportHistoryAdapter
            if (report_history_list.layoutManager == null){
                report_history_list.addItemDecoration(
                    tta.astrologerapp.talktoastro.util.MarginItemDecorator(
                        1
                    )
                )
            }
            report_history_list.hasFixedSize()
        })
        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon: ImageView =activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility=View.INVISIBLE
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReportHistoryViewModel(mApplication) as T
        }
    }


    companion object {
        internal val tagName
            get() = tta.astrologerapp.talktoastro.ui.reports.ReportHistoryFragment::class.java.name

        fun newInstance(bundle: Bundle?): tta.astrologerapp.talktoastro.ui.reports.ReportHistoryFragment {
            val fragment =
                tta.astrologerapp.talktoastro.ui.reports.ReportHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
