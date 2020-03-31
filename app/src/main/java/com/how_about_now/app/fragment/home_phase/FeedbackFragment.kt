package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.FeedbackEntity
import com.how_about_now.app.data.FeedbackWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_feedback.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class FeedbackFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        backIV.setOnClickListener(this)
        submitBT.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            submitBT -> {
                if (!desET.text.toString().trim().isEmpty()) {
                    hitFeedbackApi()
                } else {
                    baseActivity!!.showMessage("Please enter description")
                }
            }
        }
    }

    private fun hitFeedbackApi() {
        var msg = baseActivity!!.getProfileData()
        var des = desET.text.toString().trim()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.userFeedbackApi(
                FeedbackEntity(des, msg.user_id)
            )
            call.enqueue(object : Callback<FeedbackWrapper> {
                override fun onResponse(
                    call: Call<FeedbackWrapper>?,
                    response: Response<FeedbackWrapper>?
                ) {
                    baseActivity?.hideLoading()
                    var feedbackWrapper = response?.body()
                    if (feedbackWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity?.showMessage(feedbackWrapper!!.msg[0].response)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<FeedbackWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }
}
