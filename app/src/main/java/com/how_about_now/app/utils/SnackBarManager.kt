package com.how_about_now.app.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import com.nispok.snackbar.Snackbar
import com.nispok.snackbar.SnackbarManager
import com.nispok.snackbar.enums.SnackbarType
import com.nispok.snackbar.listeners.ActionClickListener

/**
 * Created by Anuj.kamboj on 11/05/2019.
 */

object SnackBarManager {

    private var networkSnackbar: Snackbar? = null

    fun SnackBar(message: String?, action: String?, actionClickListener: ActionClickListener?, activity: Activity) {
        if (networkSnackbar != null && networkSnackbar!!.isShowing && message == null)
            networkSnackbar!!.dismiss()
        if (networkSnackbar != null && networkSnackbar!!.isShowing)
            networkSnackbar!!.dismiss()
        if (message == null)
            return
        networkSnackbar = com.nispok.snackbar.Snackbar.with(activity) // context
            .text(message) // text to be displayed
            .type(SnackbarType.MULTI_LINE)
            .textColor(Color.WHITE) // change the text color
            .textTypeface(Typeface.DEFAULT) // change the text font
            .animation(true)
        //                        .color(Color.BLUE) // change the background color

        if (action != null && actionClickListener != null) {
            networkSnackbar!!.swipeToDismiss(false)
                .dismissOnActionClicked(false)
                .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .actionLabel(action) // action button label
                .actionColor(Color.RED) // action button label color
                .actionLabelTypeface(Typeface.DEFAULT_BOLD) // change the action button font
                .actionListener(actionClickListener)// action button's ActionClickListener;
        }
        SnackbarManager.show(networkSnackbar!!, activity) // activity where it is displayed

    }

    fun showSnackBar(message: String, activity: Activity) {
        SnackBar(message, null, null, activity)
    }

    fun showSnackBar(message: String, action: String, actionClickListener: ActionClickListener, activity: Activity) {
        SnackBar(message, action, actionClickListener, activity)
    }
}
