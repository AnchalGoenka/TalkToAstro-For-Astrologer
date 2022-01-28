package tta.astrologerapp.talktoastro

import tta.astrologerapp.talktoastro.R


/**

 * Created by Vivek Singh on 2019-06-11.

 */
class TTABaseAppplication : BaseApplication() {

    override fun initializeComponents() {

        initializeVolly(getString(R.string.app_name), 0, 0)
    }


}