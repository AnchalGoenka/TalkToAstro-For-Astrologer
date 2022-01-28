package tta.astrologerapp.talktoastro.util

import android.app.Activity


/**

 * Created by Vivek Singh on 2019-09-07.

 */
class ExceptionHandler(private val activity: Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        activity.finish()
        System.exit(2)
    }
}