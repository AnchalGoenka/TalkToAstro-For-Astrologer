package tta.astrologerapp.talktoastro.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


/**

 * Created by Vivek Singh on 2019-06-24.

 */
object SharedPreferenceUtils {
    private var mDefaultSharedPref: SharedPreferences? = null

    fun init(con: Context) {
        tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref =
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                con
            )
    }

    /** Get the instance of the sharedPreference to save for fetch
     * @param con
     * @return
     */
    fun getSharedPref(con: Context): SharedPreferences {
        return (if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref == null) PreferenceManager.getDefaultSharedPreferences(con) else tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref)!!
    }

    /**
     * write object into default shared preference object
     * @param value - value to write into shared pref key
     * @param key - key mapping for value
     */
    fun put(key: String, value: Any) {
        if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref == null)
            throw IllegalStateException("Default Shared Preference object not initialized...exiting!!!")
        tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.writeObject(
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref!!,
            key,
            value
        )
    }

    /**
     * write object into names shared preference object
     * @param value - value to write into shared pref key
     * @param key - key mapping for value
     * @param sharedPref - reference of the shared preference object
     */
    fun put(key: String, value: Any, sharedPref: SharedPreferences?) {
        if (sharedPref == null)
            throw IllegalStateException("Shared Preference object not initialized...exiting!!!")
        tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.writeObject(
            sharedPref,
            key,
            value
        )
    }


    /**
     * Read value for key in default shared preference
     * @param key - key mapped for value
     * @return - value mapped for key
     */
    fun readString(key: String, vararg defaultVal: String): String? {
        if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref == null)
            throw IllegalStateException("Default Shared Preference object not initialized...exiting!!!")
        return tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref!!.getString(key, if (defaultVal != null && defaultVal.size > 1) defaultVal[0] else "")
    }

    fun readString(key: String, defaultVal: String, sharedPref: SharedPreferences?): String? {
        if (sharedPref == null)
            throw IllegalStateException("Shared Preference object not initialized...exiting!!!")

        return sharedPref.getString(key, defaultVal)
    }

    fun readBoolean(key: String, defaultVal: Boolean?, sharedPref: SharedPreferences?): Boolean {
        if (sharedPref == null)
            throw IllegalStateException("Shared Preference object not initialized...exiting!!!")

        return sharedPref.getBoolean(key, defaultVal!!)
    }

    /**
     * truncate all data in default shared preference
     */
    fun clear() {
        if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref != null)
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref!!.edit().clear().apply()
    }
    fun clearPrefs(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

    }


    /**
     * delete a key from  default shared preference
     * @param key - key to delete
     */
    fun remove(key: String) {
        if (tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref != null)
            tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.mDefaultSharedPref!!.edit().remove(key).apply()
    }

    fun remove(key: String, sharedPref: SharedPreferences?) {
        sharedPref?.edit()?.remove(key)?.apply()
    }

    /**
     * Check type of object and write into sharedpref object
     * @param sharedPreferences refernce of shared prefrence object
     * @param key key for the preference value
     * @param value value of the preference to dave
     * @return - true if object is successfully written into sharedpref, false otherwise
     */
    private fun writeObject(sharedPreferences: SharedPreferences, key: String, value: Any): Boolean {
        val editor = sharedPreferences.edit()
        if (value is String) {
            editor.putString(key, value)
        } else if (value is Int) {
            editor.putInt(key, value)
        } else if (value is Boolean) {
            editor.putBoolean(key, value)
        } else if (value is Float) {
            editor.putFloat(key, value)
        } else if (value is Long) {
            editor.putFloat(key, value.toFloat())
        }
        editor.apply()
        return true
    }


}