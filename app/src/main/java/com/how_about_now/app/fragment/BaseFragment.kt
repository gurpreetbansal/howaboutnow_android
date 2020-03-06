package com.how_about_now.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.how_about_now.app.activity.BaseActivity


/**
 * A simple [Fragment] subclass created by Anuj Kamboj.
 *
 */
open class BaseFragment : Fragment() {

    open var baseActivity: BaseActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText("")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity = activity as BaseActivity
    }

}
