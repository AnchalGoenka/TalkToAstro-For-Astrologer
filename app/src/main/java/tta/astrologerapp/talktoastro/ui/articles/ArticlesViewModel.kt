package tta.astrologerapp.talktoastro.ui.articles

import androidx.lifecycle.MutableLiveData
import tta.astrologerapp.talktoastro.util.BaseViewModel
import tta.astrologerapp.talktoastro.volley.ext.RequestType


/**

 * Created by Vivek Singh on 2019-09-09.

 */
class ArticlesViewModel constructor(app: tta.astrologerapp.talktoastro.BaseApplication): BaseViewModel(app) {

    val arrayArticleMutableLiveData = MutableLiveData<ArrayList<tta.astrologerapp.talktoastro.model.ArticleList>>()
    var arrayArticleModel = ArrayList<tta.astrologerapp.talktoastro.model.ArticleList>()

    init {
        getArticles()
    }

    fun getArticles() {
        doApiRequest(
            RequestType.GET, tta.astrologerapp.talktoastro.volley.RequestIdentifier.ARTICLES.ordinal,
            tta.astrologerapp.talktoastro.util.ApplicationConstant.ACTIVE_ARTICLES, null, null, null, false)
    }

    override fun onApiResponse(identifier: Int, response: String, serverDate: Long, lastModified: Long) {
        super.onApiResponse(identifier, response, serverDate, lastModified)
        if (identifier == tta.astrologerapp.talktoastro.volley.RequestIdentifier.ARTICLES.ordinal) {
            tta.astrologerapp.talktoastro.volley.gson.GsonHelper.getInstance().parse(response, tta.astrologerapp.talktoastro.model.ArticleModel::class.java,
                object : tta.astrologerapp.talktoastro.volley.gson.OnGsonParseCompleteListener<tta.astrologerapp.talktoastro.model.ArticleModel>() {
                    override fun onParseComplete(data: tta.astrologerapp.talktoastro.model.ArticleModel) {
                        arrayArticleModel = data.articleList
                        arrayArticleMutableLiveData.postValue(data.articleList)
                    }

                    override fun onParseFailure(data: tta.astrologerapp.talktoastro.model.ArticleModel) {
                        tta.astrologerapp.talktoastro.util.LogUtils.d("place list view model", "parse failed due to error ")
                    }
                })
        }
    }
}