package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.how_about_now.app.R
import com.how_about_now.app.adapter.ProfileAdapter
import com.how_about_now.app.data.profile_data.GetUserInfoMsg
import com.how_about_now.app.data.profile_data.GetUserInfoWrapper
import com.how_about_now.app.data.profile_data.UserIdEntity
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment(), View.OnClickListener {

    private var isHide: Boolean = true
    private var isHideOne: Boolean = true
    private var isHideTwo: Boolean = true
    private var getUserImagesArrayList = ArrayList<String>()
    private var getUserInfoMsgArrayList = ArrayList<GetUserInfoMsg>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        profileRV.layoutManager = GridLayoutManager(baseActivity!!, 3, RecyclerView.VERTICAL, false)


        questionTV.setOnClickListener(this)
        questionOneTV.setOnClickListener(this)
        questionTwoTV.setOnClickListener(this)
        editTV.setOnClickListener(this)

        hitGetUserInfoApi()
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

            questionThreeTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHideTwo) {
                    isHideTwo = true
                    answerThreeTV.visibility = View.VISIBLE
                    questionThreeTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideTwo = false
                    answerThreeTV.visibility = View.GONE
                    questionThreeTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            editTV -> {
                baseActivity!!.store.save("profileData",getUserInfoMsgArrayList)
                var bundle = Bundle()
                bundle.putParcelableArrayList("profileData", getUserInfoMsgArrayList)
                baseActivity!!.gotoFragment(EditProfileFragment(), R.id.container, bundle)
            }
        }
    }

    private fun hitGetUserInfoApi() {
        var msg = baseActivity!!.getProfileData()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "abcTest")
            val call = apiInterface.getUserInfoApi(
                UserIdEntity(msg.user_id)
            )
            call.enqueue(object : Callback<GetUserInfoWrapper> {
                override fun onResponse(
                    call: Call<GetUserInfoWrapper>?,
                    response: Response<GetUserInfoWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var getUserInfoWrapper = response?.body()

                    if (getUserInfoWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        getUserInfoMsgArrayList.add(getUserInfoWrapper!!.msg[0])
                        setUserData(getUserInfoWrapper!!.msg[0])

                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<GetUserInfoWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun setUserData(getUserInfoMsg: GetUserInfoMsg) {
        getUserImagesArrayList.clear()
        aboutUsTV.setText(getUserInfoMsg.about_me)
        genderSelectionTV.setText(getUserInfoMsg.gender)
        dobTV.setText(getUserInfoMsg.birthday)

        questionTV.setText(getUserInfoMsg.user_answer[0].question)
        answerTV.setText(getUserInfoMsg.user_answer[0].answer.toString())

        questionOneTV.setText(getUserInfoMsg.user_answer[1].question)
        answerOneTV.setText(getUserInfoMsg.user_answer[1].answer.toString())

        questionTwoTV.setText(getUserInfoMsg.user_answer[2].question)
        answerTwoTV.setText(getUserInfoMsg.user_answer[2].answer.toString())

        questionThreeTV.setText(getUserInfoMsg.user_answer[3].question)
        answerThreeTV.setText(getUserInfoMsg.user_answer[3].answer.toString())

        getUserImagesArrayList.add(getUserInfoMsg.image1)
        getUserImagesArrayList.add(getUserInfoMsg.image2)
        getUserImagesArrayList.add(getUserInfoMsg.image3)
        getUserImagesArrayList.add(getUserInfoMsg.image4)
        getUserImagesArrayList.add(getUserInfoMsg.image5)
        getUserImagesArrayList.add(getUserInfoMsg.image6)
        profileRV.adapter = ProfileAdapter(baseActivity!!, getUserImagesArrayList)
    }

}
