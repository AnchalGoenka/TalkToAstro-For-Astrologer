package tta.astrologerapp.talktoastro.ui.astroprofile

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.position

import tta.astrologerapp.talktoastro.R
import kotlinx.android.synthetic.main.astro_profile_fragment.*
import org.json.JSONObject
import java.io.IOException

class AstroProfileFragment : tta.astrologerapp.talktoastro.BaseFragment() {

    private var astroProfileViewModel: tta.astrologerapp.talktoastro.ui.astroprofile.AstroProfileViewModel? = null
    private var reviewAdapter: tta.astrologerapp.talktoastro.adapter.ReviewListAdapter? = null
//    private var homeViewModelInProfile: HomeViewModel? = null
    private var ratingVal: String = ""
    private lateinit var mp: MediaPlayer
    private var pause:Boolean = false
    // val audiourl = " https://www.ttadev.in/ttaweb/public/audio/astrologers/"
    val audiourl ="https://www.talktoastro.com/public/audio/astrologers/"// your URL here
    // val audiourl = "http://www.all-birds.com/Sound/western%20bluebird.wav" // your URL here
    var audio:String=""

    override val layoutResId: Int
        get() = R.layout.astro_profile_fragment

    override fun getToolbarId(): Int {
        return 0
    }

    companion object {
        fun newInstance() =
            tta.astrologerapp.talktoastro.ui.astroprofile.AstroProfileFragment()
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showProgressBar("Please wait..")
        astroProfileViewModel  =
        ViewModelProviders.of(this,
            tta.astrologerapp.talktoastro.ui.astroprofile.AstroProfileFragment.MyViewModelFactory(
                tta.astrologerapp.talktoastro.BaseApplication.instance
            )
        ).get(tta.astrologerapp.talktoastro.ui.astroprofile.AstroProfileViewModel::class.java)
        // TODO: Use the ViewModel
        var json = JSONObject()
        val astroID = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
        json.put("astroID", astroID)
        astroProfileViewModel?.getAstroProfile(json)
        astroProfileViewModel?.getAstrologersList()
        astroProfileViewModel?.arrayListMutableLiveData?.observe(this, Observer {

            txv_person_name.text = it.firstName + " " + it.lastName
            txv_detail.loadData(it.about, "text/html; charset=UTF-8", null)
            txv_detail.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return true
                }
            })
            txv_language.text = it.languages
            txv_experience.text = it.experience
            txv_expertise_profile.text = it.expertise
            Glide.with(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()).load(Uri.parse(it.image)).placeholder(R.mipmap.default_profile).diskCacheStrategy(
                DiskCacheStrategy.RESOURCE)

                .skipMemoryCache(false).into(imageProfile)
            tv_StarRatingProfile.text=it.ratingAvg + " "
            tv_review.text = it.totalRatings
            tv_call_min.text = it.callMin
            tv_reports.text = it.reportNum

           /* when(it.ratingAvg){
                "1" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_star_regular, R.drawable.ic_star_regular, R.drawable.ic_star_regular, R.drawable.ic_star_regular)
                "1.5" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_star_half_alt_solid, R.drawable.ic_star_regular, R.drawable.ic_star_regular, R.drawable.ic_star_regular)
                "2" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_star_regular, R.drawable.ic_star_regular, R.drawable.ic_star_regular)
                "2.5" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_star_half_alt_solid, R.drawable.ic_star_regular, R.drawable.ic_star_regular)
                "3" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_star_regular, R.drawable.ic_star_regular)
                "3.5" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_star_half_alt_solid, R.drawable.ic_star_regular)
                "4" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_star_regular)
                "4.5" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_star_half_alt_solid)
                "5" -> setImagesAsPerRating(R.drawable.ic_expertise,R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_expertise, R.drawable.ic_expertise)
            }*/


            mp = MediaPlayer()
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {

                mp.setDataSource(audiourl + it.audio)
               // mp.prepare()
                mp.prepareAsync()

            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (e: SecurityException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (e: IllegalStateException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            audio= it.audio.toString()
            relativeLayout_play.setOnClickListener {
                if(audio.isEmpty()||audio=="0"||audio=="null"){
                AwesomeDialog.build(myActivity)
                    .body("Audio is not Available")
                    .onPositive("Okay",
                        buttonBackgroundColor = R.drawable.layout_rounded,
                        textColor = ContextCompat.getColor(
                            myActivity,
                            android.R.color.white)) {
                        Log.d("TAG", "positive ")
                    }
                    .position(AwesomeDialog.POSITIONS.CENTER)
            }
            else{
                if (pause) {
                    iv_play.visibility = View.INVISIBLE
                    iv_pause.visibility = View.VISIBLE
                    mp.start()
                    pause = false
                    // Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()
                } else {


                    iv_play.visibility = View.INVISIBLE
                    iv_pause.visibility = View.VISIBLE
                    mp.start()
                    // Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()

                }
                mp.setOnCompletionListener {
                    iv_play.visibility = View.VISIBLE
                    iv_pause.visibility = View.INVISIBLE
                    //pauseBtn.isEnabled = false
                    // stopBtn.isEnabled = false
                    //Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
                }
            } }

            iv_pause.setOnClickListener {
                if(mp.isPlaying){
                    mp.pause()
                    pause = true
                    iv_play.visibility = View.VISIBLE
                    iv_pause.visibility=View.INVISIBLE
                    // Toast.makeText(this,"media pause",Toast.LENGTH_SHORT).show()
                }
            }
            hideProgressBar()
        })
        astroProfileViewModel?.astroProfileMutableLiveData?.observe(this, Observer {
            txv_availability_profile.text = it!!.availability.first().day+"\n"+it!!.availability.first().time
            reviewAdapter =
                tta.astrologerapp.talktoastro.adapter.ReviewListAdapter(it!!.reviews)
            val list_reviews_profile: androidx.recyclerview.widget.RecyclerView = myActivity.findViewById(R.id.list_reviews_profile)
            list_reviews_profile.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
            list_reviews_profile.adapter = reviewAdapter
            if (list_reviews_profile.layoutManager == null){
                list_reviews_profile.addItemDecoration(
                    tta.astrologerapp.talktoastro.util.MarginItemDecorator(
                        1
                    )
                )
            }
            list_reviews_profile.hasFixedSize()
        })
    }

    /*fun setImagesAsPerRating(img1: Int, img2: Int, img3: Int, img4: Int, img5: Int){
        imgStar1.setImageResource(img1)
        imgStar2.setImageResource(img2)
        imgStar3.setImageResource(img3)
        imgStar4.setImageResource(img4)
        imgStar5.setImageResource(img5)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        val notificationIcon: ImageView =activity!!.findViewById(R.id.iv_notofication)
        notificationIcon.visibility=View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mp?.release()
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return tta.astrologerapp.talktoastro.ui.astroprofile.AstroProfileViewModel(
                mApplication
            ) as T
        }
    }
}
