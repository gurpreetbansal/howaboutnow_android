package com.how_about_now.app.custom_views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.how_about_now.app.utils.PrefStore

class SfRegulerEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        setTypeface(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setTypeface(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setTypeface(context)
    }

    private fun setTypeface(context: Context) {
            typeface = Typeface.createFromAsset(context.assets, "SFUIText-Regular.ttf")
    }
}