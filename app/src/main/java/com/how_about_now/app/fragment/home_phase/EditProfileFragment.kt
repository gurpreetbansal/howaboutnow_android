package com.how_about_now.app.fragment.home_phase

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.how_about_now.app.R
import com.how_about_now.app.adapter.EditProfileAdapter
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.fragment.BottomSheetFragment
import com.how_about_now.app.utils.BottomSheetImageCallBack
import com.how_about_now.app.utils.ItemMoveCallback
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : BaseFragment(), View.OnClickListener,
    EditProfileAdapter.AddImageCallBack, BottomSheetImageCallBack.ImageCallBackListener {

    private var isHide: Boolean = true
    private var isHideOne: Boolean = true
    private var isHideTwo: Boolean = true
    private var profileImagesUriArrayList = ArrayList<Uri>()
    private var editProfileAdapter: EditProfileAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        BottomSheetImageCallBack.getInstance(baseActivity).setImageCallBackListener(this)
        profileRV.layoutManager = GridLayoutManager(baseActivity!!, 3, RecyclerView.VERTICAL, false)

        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(profileRV)
        profileRV.adapter = editProfileAdapter

        questionTV.setOnClickListener(this)
        questionOneTV.setOnClickListener(this)
        questionTwoTV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            questionTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHide) {
                    isHide = true
                    answerTV.visibility = View.VISIBLE
                    questionTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHide = false
                    answerTV.visibility = View.GONE
                    questionTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            questionOneTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHideOne) {
                    isHideOne = true
                    answerOneTV.visibility = View.VISIBLE
                    questionOneTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideOne = false
                    answerOneTV.visibility = View.GONE
                    questionOneTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            questionTwoTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHideTwo) {
                    isHideTwo = true
                    answerTwoTV.visibility = View.VISIBLE
                    questionTwoTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideTwo = false
                    answerTwoTV.visibility = View.GONE
                    questionTwoTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
        }
    }

    override fun onAddImageListener(position: Int) {
        var bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(
            baseActivity!!.getSupportFragmentManager(),
            bottomSheetFragment.getTag()
        )
    }

    override fun onImageCallBackCallBack(file: File?, filePath: String?, uri: Uri?) {
        baseActivity?.showMessage(uri.toString())
        profileImagesUriArrayList.add(uri!!)
        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(profileRV)
        profileRV.adapter = editProfileAdapter
    }

}
