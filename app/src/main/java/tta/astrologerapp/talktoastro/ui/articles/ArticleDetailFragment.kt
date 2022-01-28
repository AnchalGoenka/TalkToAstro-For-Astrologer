package tta.astrologerapp.talktoastro.ui.articles

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import tta.astrologerapp.talktoastro.R
import kotlinx.android.synthetic.main.fragment_article_detail.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import tta.astrologerapp.talktoastro.model.ArticleList


class ArticleDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = arguments?.getParcelable<ArticleList>("articleList") as tta.astrologerapp.talktoastro.model.ArticleList
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        Glide.with(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()).load(Uri.parse(article.image)).placeholder(R.drawable.ic_banner_default).diskCacheStrategy(
            DiskCacheStrategy.RESOURCE)

            .skipMemoryCache(false).into(imageView_article_detail)
        txv_article_heading_detail.text = article.title
        txv_article_detail.loadData(article.description, "text/html; charset=UTF-8", null)
        txv_article_detail.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return true
            }
        })
//        var stringDesc = Html.fromHtml(article.description).toString().replace("\n", "").trim()
//        stringDesc = stringDesc.replace("\n","")
//        txv_article_detail.text = stringDesc
    }

    companion object {
        internal val tagName: String
            get() = tta.astrologerapp.talktoastro.ui.articles.ArticleDetailFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment SearchPlacesFragment.
         */
        fun newInstance(bundle: Bundle?): tta.astrologerapp.talktoastro.ui.articles.ArticleDetailFragment {
            val fragment =
                tta.astrologerapp.talktoastro.ui.articles.ArticleDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
