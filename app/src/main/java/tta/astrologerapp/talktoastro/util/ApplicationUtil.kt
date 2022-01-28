package tta.astrologerapp.talktoastro.util

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager


/**

 * Created by Vivek Singh on 2019-06-09.

 */
class ApplicationUtil {

    companion object {

        /**
         * Returns the status of the network
         */
        fun isNetworkAvailable(pContext: Context): Boolean {
            val conMngr = pContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = conMngr.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnected
        }

        fun getContext(): Context {
            return tta.astrologerapp.talktoastro.BaseApplication.instance
        }

        fun checkLogin() : Boolean {
            val username = SharedPreferenceUtils.readString(
                ApplicationConstant.USERNAME, "",
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
            val password = SharedPreferenceUtils.readString(
                ApplicationConstant.PASSWORD, "",
                SharedPreferenceUtils.getSharedPref(ApplicationUtil.getContext()))
            //val logout = myActivity.findViewById<View>(R.id.nav_logout)
            return !(username.isNullOrEmpty())
        }

        fun showDialog(
            context: Context?,
            message: String?
        ) {
            val alertDialogBuilder =
                AlertDialog.Builder(
                    context
                )

            alertDialogBuilder.setNeutralButton("OK"){_,_ ->

            }
            // set dialog message
            alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)


            // show it


            // show it
            alertDialogBuilder.show()
        }
    }

}