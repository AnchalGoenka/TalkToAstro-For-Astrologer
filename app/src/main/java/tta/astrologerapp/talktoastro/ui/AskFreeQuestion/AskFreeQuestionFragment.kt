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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_ask_free_question.*
import org.json.JSONObject
import tta.astrologerapp.talktoastro.BaseFragment
import tta.astrologerapp.talktoastro.R
import tta.astrologerapp.talktoastro.adapter.AskFreeQuesAdapter
import tta.astrologerapp.talktoastro.model.hubQuestionList
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.util.MarginItemDecorator
import tta.astrologerapp.talktoastro.viewmodel.AskFreeQuestionViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AskFreeQuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AskFreeQuestionFragment : BaseFragment() {

    private var askFreeQuestionViewModel: AskFreeQuestionViewModel? = null
    private var askFreeQuesAdapter: tta.astrologerapp.talktoastro.adapter.AskFreeQuesAdapter? = null
    var  isclicked:Boolean=false

    override val layoutResId: Int
        get() = R.layout.fragment_ask_free_question

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

        return inflater.inflate(R.layout.fragment_ask_free_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarSetup()
        callQuestionList()

        tv_show_less.setOnClickListener {
            if (isclicked == false) {
                tv.visibility = View.VISIBLE
                tv_show_less.text="Show less"
                isclicked = true
            } else {
                tv.visibility = View.GONE
                tv_show_less.text="Show More"
                isclicked = false
            }
        }

        btn_quetion_submit.setOnClickListener {

            questionSubmit()

        }

    }

    override fun onResume() {
        super.onResume()
        //  callQuestionList()
    }

    fun questionSubmit(){
        showProgressBar("Loading")
        val jsonObject= JSONObject()
        jsonObject.put("question",ed_Question_submit.text)
        jsonObject.put("user_type","user")
        jsonObject.put("status","active")
        /*if (!ApplicationUtil.checkLogin()) {
            val intent = Intent(
                ApplicationUtil.getContext(),
                LoginActivity::class.java
            )
            startActivity(intent)
        } else*/ if(ed_Question_submit.text.isNotEmpty()){
            askFreeQuestionViewModel?.gethubQuestionSubmit(jsonObject)
            askFreeQuestionViewModel?.questionSubmitListMutableLiveData?.observe(myActivity, Observer {
                Toast.makeText(ApplicationUtil.getContext(), it, Toast.LENGTH_LONG)
                    .show()
                ed_Question_submit.text.clear()
                card_your_question.visibility = View.GONE
                isclicked = false
                callQuestionList()

            })
        } else{

            Toast.makeText(ApplicationUtil.getContext(),"Please Write Your Question ?", Toast.LENGTH_LONG).show()
        }
        hideProgressBar()
    }

    fun callQuestionList(){
        showProgressBar("Loading")
        askFreeQuestionViewModel = ViewModelProvider(this, MyViewModelFactory(myApplication)).get(AskFreeQuestionViewModel::class.java)
        askFreeQuestionViewModel?.gethubQuestion()
        askFreeQuestionViewModel?.arrayListMutablehubQuestionData?.observe(myActivity, Observer {
            // Collections.reverse(it)
            askFreeQuesAdapter = AskFreeQuesAdapter(it as ArrayList<hubQuestionList>){ hubQuestionListItem, i ->

                val bundle = Bundle()
                bundle.putString("id", hubQuestionListItem.id)
                bundle.putString("question", hubQuestionListItem.question)
                bundle.putInt("position",i)
                // bundle.putParcelable("position",p)
               /* val answerFragment = AnswerFragment.newInstance(bundle)
                var transaction: FragmentTransaction =
                    myActivity.supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.frame_layout,
                    answerFragment,
                    AnswerFragment.tagName
                )
                transaction.addToBackStack(AnswerFragment.tagName)
                transaction.commit()*/
                this.findNavController().navigate(R.id.nav_Answer, bundle)
            }
            val question_list: RecyclerView = myActivity.findViewById(R.id.rv_question_list)
            question_list.layoutManager = LinearLayoutManager(ApplicationUtil.getContext())
            question_list.scheduleLayoutAnimation()
            question_list.adapter = askFreeQuesAdapter
            if (question_list.layoutManager == null){
                question_list.addItemDecoration(MarginItemDecorator(1))
            }
            question_list.hasFixedSize()
            hideProgressBar()
        })

    }

    private fun toolBarSetup() {

        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "AskFreeQuestion"
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
        toolbar.setNavigationOnClickListener {
            val drawer = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.openDrawer(GravityCompat.START)
        }
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon: ImageView = activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility = View.INVISIBLE
    }

    companion object {
        internal val tagName
            get() = AskFreeQuestionFragment::class.java.name

        fun newInstance(bundle: Bundle?): AskFreeQuestionFragment {
            val fragment = AskFreeQuestionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AskFreeQuestionViewModel(mApplication) as T
        }
    }

}