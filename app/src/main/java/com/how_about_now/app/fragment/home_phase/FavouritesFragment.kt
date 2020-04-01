package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.FavouriteAdapter
import com.how_about_now.app.data.favoirite_phase.FavouriteEntity
import com.how_about_now.app.data.favoirite_phase.FavouriteWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import kotlinx.android.synthetic.main.fragment_favourites.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        favouriteRV.layoutManager =
            GridLayoutManager(baseActivity!!, 2, RecyclerView.VERTICAL, false)


        matchTV.setOnClickListener(this)
        likedYouTV.setOnClickListener(this)
        youLikedTV.setOnClickListener(this)

        hitFavouriteApi(AppConstants.YOU_LIKED)
    }

    override fun onClick(v: View?) {
        when (v) {
            youLikedTV -> {
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black_color))
                youLikedTV.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.text_selector)
                matchTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )
                likedYouTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )

                hitFavouriteApi(AppConstants.YOU_LIKED)
            }
            likedYouTV -> {
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black_color))
                likedYouTV.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.text_selector)
                matchTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )
                youLikedTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )

                hitFavouriteApi(AppConstants.LIKED_YOU)
            }
            matchTV -> {
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.gray_color))
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black_color))
                matchTV.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.text_selector)
                youLikedTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )
                likedYouTV.setBackgroundColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.white_color
                    )
                )

                hitFavouriteApi(AppConstants.MATCH)
            }
        }
    }

    private fun hitFavouriteApi(type: Int) {
        var msg = baseActivity!!.getProfileData()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.favouriteApi(
                FavouriteEntity(
                    type, msg.user_id
                )
            )
            call.enqueue(object : Callback<FavouriteWrapper> {
                override fun onResponse(
                    call: Call<FavouriteWrapper>?,
                    response: Response<FavouriteWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var nearByMeWrapper = response?.body()

                    if (nearByMeWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {

                        favouriteRV.adapter = FavouriteAdapter(baseActivity!!,nearByMeWrapper!!.msg)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<FavouriteWrapper>?, t: Throwable?) {
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
