package tta.astrologerapp.talktoastro

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tta.astrologerapp.talktoastro.util.ApplicationUtil
import tta.astrologerapp.talktoastro.interfaces.IScreen
import tta.astrologerapp.talktoastro.util.LogUtils


/**

 * Created by Vivek Singh on 2019-09-13.

 */
abstract class BaseFragment : Fragment(), IScreen {

    protected lateinit var activity: MainActivity
    protected lateinit var parentLayout: ConstraintLayout//

    protected abstract val layoutResId: Int
    protected var applicationContext = ApplicationUtil.getContext()

    fun getColor(colorInt: Int): Int {
        return ContextCompat.getColor(applicationContext, colorInt)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (getActivity() !is MainActivity)
            throw RuntimeException("Your Activity should extend from com.agreeya.android.baseframework.ui.activity.BaseActivity")
    }

    fun showProgressBar(message: String) {
        try {
            activity.showProgressBar(message, true)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun showProgressBar(message: String, cancellable: Boolean) {
        activity.showProgressBar(message, cancellable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = myActivity
        setHasOptionsMenu(true)
    }

    val myActivity: MainActivity
        @Throws(RuntimeException::class)
        get() {
            if (getActivity() !is MainActivity)
                throw RuntimeException("Your Activity should extend from com.agreeya.android.baseframework.ui.activity.BaseActivity")
            return getActivity() as MainActivity
        }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {
        activity.hideProgressBar()
    }

    override val myApplication: BaseApplication
        get() {
            return activity.myApplication
        }

    fun setToolBarTitle(title: String?) {
        if (activity.supportActionBar != null)
            activity.supportActionBar!!.title = title
    }

    protected abstract fun getToolbarId(): Int

    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutResId <= 0) {
            LogUtils.e("No layout resource provided. your fragment should override getLayoutResId method and return layout view")
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        val viewINS = if (view != null) {
            view
        }
        else {
            inflater.inflate(layoutResId, container, false)
        }

        if(view != null) {
            toolbar = viewINS!!.findViewById<View>(getToolbarId()) as Toolbar
        }else
        {
            toolbar = if (getToolbarId() != -1) {
                myActivity.getToolbar()
            } else {
                viewINS!!.findViewById<View>(getToolbarId()) as Toolbar
            }
        }

        toolbar.setTitleTextColor(Color.WHITE)
        myActivity.setSupportActionBar(toolbar)
        setToolBarTitle(tag)
        return viewINS
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}