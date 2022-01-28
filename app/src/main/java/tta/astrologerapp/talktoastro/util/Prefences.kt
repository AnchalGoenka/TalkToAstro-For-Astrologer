package com.accountapp.accounts.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object Prefences {

    val SHARED_PREF_NAME = "preferences_name"

    lateinit var isShow: SharedPreferences

    var IS_SHOW = "IS_SHOW"

    @JvmStatic
    fun resetUserData(ctx: Context){
        setShowDialog(ctx,false)
    }


    // pref is Login
    fun setShowDialog(context: Context, iss: Boolean) {
        isShow = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = isShow.edit()
        editor.putBoolean(IS_SHOW, iss)
        editor.commit()
    }

    @JvmStatic
    fun getShowDialog(context: Context): Boolean {
        isShow = PreferenceManager.getDefaultSharedPreferences(context)
        return isShow.getBoolean(IS_SHOW, false)
    }




}