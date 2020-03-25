package com.how_about_now.app.fragment.login_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.login_phase.ForgotPasswordEntity
import com.how_about_now.app.data.login_phase.ForgotPasswordWrapper
import com.how_about_now.app.fragment.BaseDialogFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : BaseDialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        closeText.setOnClickListener(this)
        sendPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            closeText -> {
                dismiss()
            }
            sendPassword -> {
                if (editTextProximanovaRegular.text.toString().trim().isEmpty()) {
                    baseActivity?.showMessage(getString(R.string.please_enter_your_email))
                } else hitForgotPasswordApi()

            }
        }
    }

    private fun hitForgotPasswordApi() {
        var email = editTextProximanovaRegular.text.toString().trim()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.forgotPasswordApi(
                ForgotPasswordEntity(email)
            )
            call.enqueue(object : Callback<ForgotPasswordWrapper> {
                override fun onResponse(
                    call: Call<ForgotPasswordWrapper>?,
                    response: Response<ForgotPasswordWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var forgotPasswordWrapper = response?.body()

                    if (forgotPasswordWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity?.showMessage(forgotPasswordWrapper!!.msg[0].response)
                        dismiss()
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordWrapper>?, t: Throwable?) {
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
