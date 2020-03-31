package com.how_about_now.app.fragment.home_phase


import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.AboutUsWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_about_us.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class AboutUsFragment : BaseFragment() {

    private var isMainActivity: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isMainActivity = arguments?.getBoolean("isMainActivity", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        init()
    }

    private fun init() {
        hitAboutUsApi()
    }

    private fun hitAboutUsApi() {
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.aboutUsApi()
            call.enqueue(object : Callback<AboutUsWrapper> {
                override fun onResponse(
                    call: Call<AboutUsWrapper>?,
                    response: Response<AboutUsWrapper>?
                ) {
                    baseActivity?.hideLoading()
                    var aboutUsWrapper = response?.body()
                    if (aboutUsWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        desTV.setText(
                            HtmlCompat.fromHtml(
                                aboutUsWrapper!!.msg[0].description,
                                Html.FROM_HTML_MODE_LEGACY
                            ),
                            TextView.BufferType.SPANNABLE
                        )
                        titleTV.setText(aboutUsWrapper!!.msg[0].title)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<AboutUsWrapper>?, t: Throwable?) {
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
