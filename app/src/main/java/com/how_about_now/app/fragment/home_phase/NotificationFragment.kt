package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.notification_phase.NotificationEntity
import com.how_about_now.app.data.notification_phase.NotificationWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : BaseFragment(), View.OnClickListener {

    var social: Int = 0
    var friendBirthday: Int = 0
    var purchanse: Int = 0
    var like: Int = 0
    var visitProfile: Int = 0
    var newMessage: Int = 0
    var newMatch: Int = 0
    var recommendation: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        backIV.setOnClickListener(this)
        submitBT.setOnClickListener(this)
        offersSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                social = 1
            } else social = 0

        }
        birthdaySW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                friendBirthday = 1
            } else friendBirthday = 0
        }
        purchaseSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                purchanse = 1
            } else purchanse = 0
        }
        someOneLikeYouSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                like = 1
            } else like = 0
        }
        someVisitYourProfileSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                visitProfile = 1
            } else visitProfile = 0
        }
        getNewMessageSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                newMessage = 1
            } else newMessage = 0
        }
        whenYouHaveNewMatchSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                newMatch = 1
            } else newMatch = 0
        }
        whenYouGetNewRecommendationSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                recommendation = 1
            } else recommendation = 0
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            submitBT -> {
                hitFeedbackApi()
            }

        }
    }

    private fun hitFeedbackApi() {
        var msg = baseActivity!!.getProfileData()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.enableNotificationsApi(
                NotificationEntity(
                    friendBirthday,
                    like,
                    newMatch,
                    newMessage,
                    social,
                    visitProfile,
                    purchanse,
                    recommendation,
                    msg.user_id
                )
            )
            call.enqueue(object : Callback<NotificationWrapper> {
                override fun onResponse(
                    call: Call<NotificationWrapper>?,
                    response: Response<NotificationWrapper>?
                ) {
                    baseActivity?.hideLoading()
                    var notificationWrapper = response?.body()
                    if (notificationWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity?.showMessage(notificationWrapper!!.msg[0].response)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<NotificationWrapper>?, t: Throwable?) {
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
