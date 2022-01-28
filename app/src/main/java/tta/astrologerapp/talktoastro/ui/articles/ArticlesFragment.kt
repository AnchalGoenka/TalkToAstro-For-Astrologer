package tta.astrologerapp.talktoastro.ui.articles

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_articles.*
import tta.astrologerapp.talktoastro.R

class ArticlesFragment : tta.astrologerapp.talktoastro.BaseFragment(), SearchView.OnQueryTextListener {
    override val layoutResId: Int
        get() = R.layout.fragment_articles

    override fun getToolbarId(): Int {
        return 0
    }

    private lateinit var articleViewModel: tta.astrologerapp.talktoastro.ui.articles.ArticlesViewModel
    private lateinit var articleAdapter: tta.astrologerapp.talktoastro.adapter.ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showProgressBar("Loading...")
        articleViewModel =
            ViewModelProviders.of(this,
                tta.astrologerapp.talktoastro.ui.articles.ArticlesFragment.MyViewModelFactory(
                    tta.astrologerapp.talktoastro.BaseApplication.instance
                )
            ).get(tta.astrologerapp.talktoastro.ui.articles.ArticlesViewModel::class.java)
        articleViewModel?.arrayArticleMutableLiveData?.observe(this, Observer {
            hideProgressBar()
            articleAdapter =
                tta.astrologerapp.talktoastro.adapter.ArticleAdapter(it) { articleList, position ->
                    val bundle = Bundle()
                    bundle.putParcelable("articleList", articleList)
                    this.findNavController().navigate(R.id.nav_article_detail, bundle)
                }
            article_list.layoutManager = LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
            article_list.adapter = articleAdapter
            article_list.hasFixedSize()
        })
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivity()!!.getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon:ImageView=activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility=View.INVISIBLE
        val searchManager = context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        searchView_articles.setSearchableInfo(searchManager!!.getSearchableInfo(activity!!.componentName))
        searchView_articles.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        try {
            val originalArray = articleViewModel!!.arrayArticleModel
            var filteredArray = ArrayList<tta.astrologerapp.talktoastro.model.ArticleList>()
            originalArray.forEach {
                if (it.title?.contains (query.toString(),true)!!){
                    filteredArray!!.add(it)
                }
                if (filteredArray!!.isNotEmpty()){
                    articleAdapter!!.filteredList(filteredArray)
                }
            }
        }catch (e: Exception){
            tta.astrologerapp.talktoastro.util.LogUtils.d("onQueryTextChange")
        }

        return true
    }


    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return tta.astrologerapp.talktoastro.ui.articles.ArticlesViewModel(
                mApplication
            ) as T
        }
    }

}