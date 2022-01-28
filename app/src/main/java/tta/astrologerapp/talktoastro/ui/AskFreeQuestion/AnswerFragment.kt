package tta.astrologerapp.talktoastro.ui.AskFreeQuestion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_answer.*
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseFragment
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.adapter.AnswerAdapter
import tta.astrologerapp.talktoastro.util.ApplicationConstant
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.MarginItemDecorator
import tta.astrologerapp.talktoastro.util.SharedPreferenceUtils
import tta.astrologerapp.talktoastro.viewmodel.AnswerViewModel


class AnswerFragment : BaseFragment() {
    private var answerViewModel: AnswerViewModel? = null
    private var answerAdapter: tta.astrologerapp.talktoastro.adapter.AnswerAdapter? = null
    lateinit var id :String

    override val layoutResId: Int
        get() = R.layout.fragment_answer

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarSetup()
        val position= arguments?.getInt("position")
        val id =arguments?.getString("id")
        val question=arguments?.getString("question")
        tv_questionAsked.text=question
        getAnswer(id)
        btn_submit_answer.setOnClickListener { getAnswerSubmit(id) }

    }

    private fun toolBarSetup() {
        /*var toolbar = ApplicationUtil.setToolbarTitleAndRechargeButton("Answer", false, R.drawable.ic_arrow_back_24dp)
            toolbar.setNavigationOnClickListener {
                popBackStack(myActivity)
            }*/
        val toolbar = myActivity.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Answer"
        myActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp)
        toolbar.setNavigationOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon: ImageView = activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility = View.INVISIBLE
    }

    fun getAnswerSubmit(id: String?){
        showProgressBar("Loading")
        val userId = SharedPreferenceUtils.readString(
            ApplicationConstant.USERID, "",
            SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
        val jsonObject= JSONObject()
        jsonObject.put("question_id",id)
        jsonObject.put("answer",ed_answer_submit.text)
        jsonObject.put("user_type","astrologer")
        jsonObject.put("user_id",userId)
        /*if (!ApplicationUtil.checkLogin()) {
            val intent = Intent(
                ApplicationUtil.getContext(),
                tta.astrologerapp.talktoastro.activity.LoginActivity::class.java
            )
            startActivity(intent)
        } else*/ if(ed_answer_submit.text.isNotEmpty()){
            answerViewModel?.getAnswerSubmit(jsonObject)
            answerViewModel?.answerSubmitListMutableLiveData?.observe(myActivity, Observer {

                ed_answer_submit.text.clear()
                getAnswer(id)

            })
        } else{

            Toast.makeText(ApplicationUtil.getContext(),"Please Write Your Question ?", Toast.LENGTH_LONG).show()
        }
        hideProgressBar()
    }

    fun getAnswer(id:String?){
        showProgressBar("Loading")
        // val id =arguments?.getString("id")
        val jsonObject= JSONObject()
        jsonObject.put("question_id",id)
        answerViewModel = ViewModelProvider(this,MyViewModelFactory(myApplication)).get(AnswerViewModel::class.java)
        answerViewModel?.getAnswer(jsonObject)
        answerViewModel?.arrayAnswerMutableLiveData?.observe(myActivity, Observer {

            answerAdapter = AnswerAdapter(it)
            val question_list: RecyclerView = myActivity.findViewById(R.id.rv_AnswerList)
            question_list.layoutManager = LinearLayoutManager(ApplicationUtil.getContext())
            question_list.adapter = answerAdapter
            if (question_list.layoutManager == null){
                question_list.addItemDecoration(MarginItemDecorator(1))
            }
            hideProgressBar()
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnswerFragment.
         */

        internal val tagName: String
            get() = AnswerFragment::class.java.name



        fun newInstance(bundle: Bundle?): AnswerFragment {
            val fragment = AnswerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AnswerViewModel(mApplication) as T
        }
    }

}