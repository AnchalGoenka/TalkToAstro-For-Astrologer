package tta.astrologerapp.talktoastro.interfaces

import tta.astrologerapp.talktoastro.BaseApplication


/**

 * Created by Vivek Singh on 2019-06-10.

 */

interface IScreen {
    val myApplication: BaseApplication
    fun showProgressBar()
    fun hideProgressBar()
}