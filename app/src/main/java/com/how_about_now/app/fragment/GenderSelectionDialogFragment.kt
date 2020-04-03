package com.how_about_now.app.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.how_about_now.app.R
import com.how_about_now.app.utils.GenderSelectionCallBack
import kotlinx.android.synthetic.main.dialog_gender_selection.*


class GenderSelectionDialogFragment : BaseDialogFragment(), View.OnClickListener {


    private var outputfileUri: Uri? = null

    override fun onClick(v: View?) {
        when (v) {
            femaleTV -> {
                GenderSelectionCallBack.getInstance(baseActivity!!)
                    .onGenderSelectionListener("Female")
                dismiss()
            }
            maleTV -> {
                GenderSelectionCallBack.getInstance(baseActivity!!)
                    .onGenderSelectionListener("Male")
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_gender_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        maleTV.setOnClickListener(this)
        femaleTV.setOnClickListener(this)


    }


}