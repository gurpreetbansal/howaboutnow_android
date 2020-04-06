package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.notification_phase.GetNotificationMsg
import com.how_about_now.app.data.notification_phase.GetNotificationWrapper
import com.how_about_now.app.data.notification_phase.NotificationEntity
import com.how_about_now.app.data.notification_phase.NotificationWrapper
import com.how_about_now.app.data.profile_data.UserIdEntity
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
        hitGetNotificationStatusApi()
        backIV.setOnClickListener(this)
        submitBT.setOnClickListener(this)
        offersSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                social = 1
                offersSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                offersSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
                social = 0
            }

        }
        birthdaySW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                friendBirthday = 1
                birthdaySW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                friendBirthday = 0
                birthdaySW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        purchaseSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                purchanse = 1
                purchaseSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                purchanse = 0
                purchaseSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        someOneLikeYouSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                like = 1
                someOneLikeYouSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                like = 0
                someOneLikeYouSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        someVisitYourProfileSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                visitProfile = 1
                someVisitYourProfileSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                visitProfile = 0
                someVisitYourProfileSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        getNewMessageSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                newMessage = 1
                getNewMessageSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                newMessage = 0
                getNewMessageSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        whenYouHaveNewMatchSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                newMatch = 1
                whenYouHaveNewMatchSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                newMatch = 0
                whenYouHaveNewMatchSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
        whenYouGetNewRecommendationSW.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                recommendation = 1
                whenYouGetNewRecommendationSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom_track_bg
                    )
                )
            } else {
                recommendation = 0
                whenYouGetNewRecommendationSW.setTrackDrawable(
                    ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.custom__gray_track
                    )
                )
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            submitBT -> {
                hitEnableNotificationsApi()
            }

        }
    }

    private fun hitEnableNotificationsApi() {
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

    private fun hitGetNotificationStatusApi() {
        var msg = baseActivity!!.getProfileData()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.getNotificationStatusApi(
                UserIdEntity(msg.user_id)
            )
            call.enqueue(object : Callback<GetNotificationWrapper> {
                override fun onResponse(
                    call: Call<GetNotificationWrapper>?,
                    response: Response<GetNotificationWrapper>?
                ) {
                    baseActivity?.hideLoading()
                    var getNotificationWrapper = response?.body()
                    if (getNotificationWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        setNotificationData(getNotificationWrapper!!.msg)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<GetNotificationWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.log(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun setNotificationData(getNotificationArrayList: ArrayList<GetNotificationMsg>) {
        if (getNotificationArrayList.get(0).offer_promotions_noti.toInt() == 1) {
            offersSW.setChecked(true)
            offersSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            offersSW.setChecked(false)
            offersSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }

        if (getNotificationArrayList.get(0).friend_bday_noti.toInt() == 1) {
            birthdaySW.setChecked(true)
            birthdaySW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            birthdaySW.setChecked(false)
            birthdaySW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
        if (getNotificationArrayList.get(0).purchase_noti.toInt() == 1) {
            purchaseSW.setChecked(true)
            purchaseSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            purchaseSW.setChecked(false)
            purchaseSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }

        if (getNotificationArrayList.get(0).like_noti.toInt() == 1) {
            someOneLikeYouSW.setChecked(true)
            someOneLikeYouSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            someOneLikeYouSW.setChecked(false)
            someOneLikeYouSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
        if (getNotificationArrayList.get(0).new_message_noti.toInt() == 1) {
            getNewMessageSW.setChecked(true)
            getNewMessageSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            getNewMessageSW.setChecked(false)
            getNewMessageSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
        if (getNotificationArrayList.get(0).match_noti.toInt() == 1) {
            whenYouHaveNewMatchSW.setChecked(true)
            whenYouHaveNewMatchSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            whenYouHaveNewMatchSW.setChecked(false)
            whenYouHaveNewMatchSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
        if (getNotificationArrayList.get(0).recommend_noti.toInt() == 1) {
            whenYouGetNewRecommendationSW.setChecked(true)
            whenYouGetNewRecommendationSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            whenYouGetNewRecommendationSW.setChecked(false)
            whenYouGetNewRecommendationSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
        if (getNotificationArrayList.get(0).profile_visit_noti.toInt() == 1) {
            someVisitYourProfileSW.setChecked(true)
            someVisitYourProfileSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom_track_bg
                )
            )
        } else {
            someVisitYourProfileSW.setChecked(false)
            someVisitYourProfileSW.setTrackDrawable(
                ContextCompat.getDrawable(
                    baseActivity!!,
                    R.drawable.custom__gray_track
                )
            )
        }
    }

}
