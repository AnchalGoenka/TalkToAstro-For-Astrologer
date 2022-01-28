package tta.astrologerapp.talktoastro.chat.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import tta.astrologerapp.talktoastro.R

class MessageTextView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    init {
        var font: String? = attrs.getAttributeValue(androidNS, "fontFamily")
        if (font == null) {
            font = "AlegreyaSans-Light"
        }
        //this.typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
        this.typeface = ResourcesCompat.getFont(context, R.font.open_sans)
    }

    companion object {
        private val androidNS = "http://schemas.android.com/apk/res/android"
    }
}
