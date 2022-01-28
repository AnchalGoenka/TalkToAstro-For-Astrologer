package tta.astrologerapp.talktoastro.util

import tta.astrologerapp.talktoastro.agora.chat.AgoraMsgActivity

/**

 * Created by Vivek Singh on 2019-06-10.

 */
class ApplicationConstant {
    companion object {

        /**
         * List of URL's used in application
         */
        //const val BASE_URL = "https://www.talktoastro.com/webservices"
      //  const val BASE_URL = "https://www.ttadev.in/tta/webservices"
      //  const val BASE_URL = "https://www.ttadev.in/ttaweb/webserviceslive"
       //   const val  BASE_URL="https://www.ttadev.in/ttaweb/webserviceslive" //Development url
        const val BASE_URL = "https://www.talktoastro.com/webserviceslive"   //live url
       // const val BASE_URL = "https://www.talktoastro.com/webserviceslivenew"
     //   const val BASE_URL = "https://ttadev.in/ttaweb/ttawebchat1/webserviceslive" //development url

        //        const val BASE_URL = "https://www.talktoastro.com/webservices2"
        //const val BASE_URL = "https://www.talktoastro.com/destinigowebsol.com/webservices"

        const val SAVE_TOKEN = "${BASE_URL}/udid_new.php"
        const val LOGIN = "${BASE_URL}/login.php"
        const val CHANGEASTROSTATUS = "${BASE_URL}/changeastrostatus.php"
        const val CHAT_STATUS="changechatstatus.php"
        const val CHANGEASTROCHATSTATUS = "$BASE_URL/"+CHAT_STATUS
        const val ACTIVE_ARTICLES = "${BASE_URL}/articles.php"
        const val USERPROFILE = "${BASE_URL}/user.profile.php"
        const val ASTROLOGER_LIST = "${BASE_URL}/astrologers.php"
        const val ASTRO_PROFILE = "${BASE_URL}/astrologer.profile.php"
        //{"userID":"469"}

        const val EDITUSERPROFILE = "${BASE_URL}/user.profile.edit.php"
        const val USERWALLETHISTORY = "${BASE_URL}/user.wallet.php"
        const val USERREPORTHISTORY ="${BASE_URL}/astrologer.report.php"
           //  "https://www.ttadev.in/webservices/astrologer.report.php" //"${BASE_URL}/astrologer.report.php"
        const val REPORTCOMMENTS = "${BASE_URL}/report.comments.php"
        const val REPORT_REQIUREMENT = "${BASE_URL}/reportrequirement.php"
        const val REPORT_COMMENTS_SUBMIT = "${BASE_URL}/report.comments.submit.php"
        const val VERIFYOTP = "${BASE_URL}/verifyotp.php"
        const val APP_NOTIFICATION="${BASE_URL}/appNotification.php"
        const val Astro_Call_Parameter="${BASE_URL}/astroCallParameters.php"

        const val REPORT_SUBMIT = "${BASE_URL}/report.submit.php"

        //{"reportID":"32","astroID":"472","reportComment":"this is your report"}
        const val REPORT_COMMENTS = "${BASE_URL}/report.comments.submit.php"
        //{"reportID":"32","userID":"469","reportComment":"this is comment from user"}
        //
        //if logged in user is astrologer
        //
        //{"reportID":"32","userID":"472","reportComment":"this is comment from astrologer"}

        const val REPORT_COMPLETED = "${BASE_URL}/report.completed.php"

        var ChatNotification = false
        var AgoraMsgActivity = false

        //{"reportID":"32","userID":"469"}
        //
        //it has condition.. this must be userID only not any astrologer id

        const val FORGOT_PASSWORD = "${BASE_URL}/user.changepassword.php"
        const val AskFreeQuestion = "$BASE_URL/hubQuestion.php"
        const val HUBQUESTIONSUBMIT = "$BASE_URL/hubQuestionSubmit.php"
        const val  hubAnswer = "$BASE_URL/hubAnswer.php"
        const val  hubAnswerSubmit = "$BASE_URL/hubAnswerSubmit.php"

        //{"oldPassword":"1111","newPassword":"123456","userID":"508"}

        const val USER_CALL_HISTORY = "${BASE_URL}/astrologer.phonecall.php"
        const val ASTRO_CHAT_HISTORY = "${BASE_URL}/astrologerchathistory.php"
        const val GET_ASTRO_STATUS = "${BASE_URL}/getastrostatus.php"
        const val GET_ASTRO_CHAT_STATUS = "$BASE_URL/getchatstatus.php"
        const val PRIVACY_POLICY = "${BASE_URL}/privacypolicy.php"

        const val FREE_HOROSCOPE = "https://api.vedicrishiastro.com/v1/astro_details"
        const val LAT_LONG_FREEHOROSCOPE = "https://json.astrologyapi.com/v1/geo_details"
        const val GET_TIMEZONE = "https://json.astrologyapi.com/v1/timezone_with_dst"

        const val  RTMTOKEN="${BASE_URL}/RtmTokenBuilderSample.php"
       const val  CURRENT_APP_VERSION = tta.astrologerapp.talktoastro.BuildConfig.VERSION_CODE
       //const val  CURRENT_APP_VERSION=2
        const val GET_APP_VERSION = "${BASE_URL}/version.php"
        var isOtpSuccess=false
        var IS_INTERNET_AVAILABLE = true
        var IS_INTERNET_DIALOG_VISIBLE = false
        var SHOW_LOGS = true

        const val CALL_STRING = "Call"
        const val REPORT_STRING = "Report"
        const val CHAT_STRING = "Chat"
       // const val ChatBaseUrl ="https://ttadev.in/ttaweb/ttawebchat1/api/chat/"
        const val ChatBaseUrl = "https://www.talktoastro.com/api/chat/"
        var ClassName = " "


        /**
         * package name INAUGURAL - common for both rupee and dollar
         */
        const val INAUGURAL = "INAUGURAL"

        /**
         * packages names - rupee
         */
        const val PEARL = "PEARL"
        const val EMERALD = "EMERALD"
        const val RUBY = "RUBY"
        const val SAPPHIRE = "SAPPHIRE"
        const val DIAMOND = "DIAMOND"

        /**
         * packages name - dollar
         */
        const val BRONZE = "BRONZE"
        const val SILVER = "SILVER"
        const val GOLD = "GOLD"
        const val PLATINUM = "PLATINUM"
        const val TITANIUM = "TITANIUM"

        /**
         * razorpay ids (TEst keys.)
         */
        const val RAZORPAY_TEST_API_KEY = "rzp_test_lomwYRitMxxx8N"
        const val RAZORPAY_SECRET_KEY = "8btHLxzabYw2DzNMat7Yo3k6"

        /**
         * razorpay live production keys
         */
        const val RAZORPAY_LIVE_API_KEY = "rzp_live_c5qb8WxI0QtmLX"

        /**
         * paytm ids and keys (Production keys)
         */

        const val PAYTM_MERCHANTID = "qgWKMK94246468416526"
        const val PAYTM_MERCHANTKEY = "KoTLzusa!QiKgLaV"

        /**
         * paytm test api
         */
        const val PAYTM_MERCHANTID_TEST = "OhSGzD61488496045330"

        // agora
        const val AppId = "11a0c7835f9a48b7bbf8cfcd6692320b"

        /**
         * astrologyapi api user id and api secret key
         */
        const val ASTROLOGYAPI_USERID = "604505"
        const val ASTROLOGYAPI_API_KEY = "2f0dd4045806553240b24273e212dfba"

        /**
         * astro status
         */
        const val ONLINE = "Online"
        const val OFFLINE = "Offline"
        const val BUSY = "Busy"
        const val BUSYSTATUS = "busy"

        /**
         * Shared preferences constant values
         */
        const val AgoraToken ="agoraToken"
        const val USERID = "userid"
        const val OTP = "otp"
        const val PHONECODE = "phonecode"
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val MOBILE = "mobiile"
        const val BALANCE = "balance"
        const val REPORTCHECKED = "reportchecked"
        const val NAME = "name"
        const val STATUS = "status"
        const val EMAIL = "email"
        const val NOTIFYCOUNT = "notifyCount"
        const val CHECKNOTIFYASTROID = "notifyAstroID"
        const val CHATMESSAGE = "Chat Message"
        const val DEVICETOKEN = "token"
        const val  fcmtoken="tokn"

        /**
         * Converting constant for rupee to dollar and dollar to rupee
         */
        const val constConverterVal = 65

        const val talkToAstroURL = "https://www.talktoastro.com/"
    }
}

interface Constants {
    companion object {
        /** Key into an Intent's extras data that points to a [Channel] object.  */
        val EXTRA_CHANNEL = "com.twilio.chat.Channel"

        /** Key into an Intent's extras data that contains Channel SID.  */
        val EXTRA_CHANNEL_SID = "C_SID"
    }
}