package tta.astrologerapp.talktoastro.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import tta.astrologerapp.talktoastro.R
import kotlinx.android.synthetic.main.profile_fragment.*
import tta.astrologerapp.talktoastro.BaseFragment

class ProfileFragment : BaseFragment() {

    var userProfileViewModel: tta.astrologerapp.talktoastro.ui.profile.ProfileViewModel? = null
    var originalUserProfile: tta.astrologerapp.talktoastro.model.UserProfile? = null

    override val layoutResId: Int
        get() = R.layout.profile_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getToolbarId(): Int {
        return  0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userProfileViewModel = ViewModelProviders.of(this,
            tta.astrologerapp.talktoastro.ui.profile.ProfileFragment.MyViewModelFactory(
                tta.astrologerapp.talktoastro.BaseApplication.instance
            )
        ).get(tta.astrologerapp.talktoastro.ui.profile.ProfileViewModel::class.java)
        var jsonObj = org.json.JSONObject()
        val userId = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
            tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
            tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
        jsonObj.put("userID", userId)
        userProfileViewModel?.getUserProfile(jsonObj)
        userProfileViewModel?.arrayMutableUserProfile?.observe(this, Observer {
            originalUserProfile = it
            edit_first_name.setText(it!!.firstName)
            edit_last_name.setText(it!!.lastName)
            ccp_edit.fullNumber = it.phonecode
            txv_edit_mobilenumber.setText(it!!.mobile)
            txv_edit_email.setText(it!!.email)
        })
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val refreshButton: ImageView = activity!!.findViewById(R.id.tv_header_refresh)
        refreshButton.visibility = View.INVISIBLE
        edit_first_name.isEnabled = false
        edit_last_name.isEnabled = false
        ccp_edit.isEnabled = false
        txv_edit_mobilenumber.isEnabled = false
        txv_edit_email.isEnabled = false

        chk_edit.setOnClickListener {
            if (chk_edit.isChecked){
                edit_first_name.isEnabled = true
                edit_last_name.isEnabled = true
                ccp_edit.isEnabled = true
                txv_edit_mobilenumber.isEnabled = true
                txv_edit_email.isEnabled = true
            } else {
                edit_first_name.isEnabled = false
                edit_last_name.isEnabled = false
                ccp_edit.isEnabled = false
                txv_edit_mobilenumber.isEnabled = false
                txv_edit_email.isEnabled = false
            }
        }

        btn_signup.setOnClickListener {
            if (originalUserProfile?.firstName != edit_first_name.text.toString() || originalUserProfile?.lastName != edit_last_name.text.toString() ||
                originalUserProfile?.phonecode != ccp_edit.fullNumber || originalUserProfile?.mobile != txv_edit_mobilenumber.text.toString() ||
                originalUserProfile?.email != txv_edit_email.text.toString()) {
                var mapParam = HashMap<String, String>()
                mapParam["first_name"] = edit_first_name.text.toString()
                mapParam["last_name"] = edit_last_name.text.toString()
                mapParam["email"] = txv_edit_email.text.toString()
                mapParam["phonecode"] = ccp_edit.fullNumber
                mapParam["mobile"] = txv_edit_mobilenumber.text.toString()
                mapParam["userID"] = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                    tta.astrologerapp.talktoastro.util.ApplicationConstant.USERID,
                    "",
                    tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext())
                ).toString()
                userProfileViewModel?.updateProfile(mapParam)
                userProfileViewModel?.arrayEditMutable?.observe(this, Observer {
                    if (it!!) {
                        originalUserProfile =
                            tta.astrologerapp.talktoastro.model.UserProfile(
                                originalUserProfile!!.balance,
                                txv_edit_email.text.toString(),
                                edit_first_name.text.toString(),
                                originalUserProfile!!.id,
                                edit_last_name.text.toString(),
                                txv_edit_mobilenumber.text.toString(),
                                ccp_edit.fullNumber,
                                originalUserProfile!!.verified
                            )

                        // Updating the balance and currency on toolbar
                        val oldCurrency = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                            tta.astrologerapp.talktoastro.util.ApplicationConstant.PHONECODE, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                        tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.put(
                            tta.astrologerapp.talktoastro.util.ApplicationConstant.PHONECODE, ccp_edit.fullNumber, tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                        if (oldCurrency != ccp_edit.fullNumber) {
                            val balance = tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.readString(
                                tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, "", tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                    tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                            if (ccp_edit.fullNumber == "91") {
                                val newBal = balance!!.toInt() * tta.astrologerapp.talktoastro.util.ApplicationConstant.constConverterVal
                                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.put(
                                    tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, newBal.toString(), tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                            } else{
                                val newBal: Int = balance!!.toInt() / tta.astrologerapp.talktoastro.util.ApplicationConstant.constConverterVal
                                tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.put(
                                    tta.astrologerapp.talktoastro.util.ApplicationConstant.BALANCE, newBal.toString(), tta.astrologerapp.talktoastro.util.SharedPreferenceUtils.getSharedPref(
                                        tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext()))
                            }
                        }
                        android.widget.Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "Profile updated successfully", android.widget.Toast.LENGTH_SHORT).show()
                    } else{
                        android.widget.Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "Error while updating. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                android.widget.Toast.makeText(tta.astrologerapp.talktoastro.util.ApplicationUtil.getContext(), "No change detected.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    class MyViewModelFactory(private val mApplication: tta.astrologerapp.talktoastro.BaseApplication): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return tta.astrologerapp.talktoastro.ui.profile.ProfileViewModel(
                mApplication
            ) as T
        }
    }


    companion object {
        internal val tagName
                get() = tta.astrologerapp.talktoastro.ui.profile.ProfileFragment::class.java.name

        fun newInstance(bundle: Bundle?): tta.astrologerapp.talktoastro.ui.profile.ProfileFragment {
            val fragment = tta.astrologerapp.talktoastro.ui.profile.ProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
