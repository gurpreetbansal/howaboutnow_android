package com.how_about_now.app.custom_views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.how_about_now.app.utils.PrefStore

class SfRegulerTextView : AppCompatTextView {

    lateinit var store: PrefStore

    constructor(context: Context) : super(context) {
        store = PrefStore(context)
        setTypeface(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        store = PrefStore(context)
        setTypeface(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        store = PrefStore(context)
        setTypeface(context)
    }

    private fun setTypeface(context: Context) {
//        if (store != null && store.getString(AppConstants.LANGUAGE, "ar").equals("en")) {
//            typeface = Typeface.createFromAsset(context.assets, "Poppins-Regular.otf")
//        } else {
            typeface = Typeface.createFromAsset(context.assets, "SFUIText-Regular.ttf")
//        }
    }
}