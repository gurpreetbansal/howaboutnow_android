package com.how_about_now.app.fragment.card_stack

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.how_about_now.app.R
import com.how_about_now.app.activity.MainActivity
import com.how_about_now.app.data.near_by_me.LikeAndDisLikeEntity
import com.how_about_now.app.data.near_by_me.NearByMeEntity
import com.how_about_now.app.data.near_by_me.NearByMeMsg
import com.how_about_now.app.data.near_by_me.NearByMeWrapper
import com.how_about_now.app.data.profile_data.EditProfileWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import com.how_about_now.app.utils.PermissionCallBack
import com.how_about_now.app.utils.PermissionsListener
import com.how_about_now.app.utils.PermissionsManager
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_descover.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class DiscoverFragment : BaseFragment(), CardStackListener, CardStackAdapter.ButtonsCallBack,
    PermissionsListener,
    PermissionCallBack.PermissionListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var lat: String? = ""
    private var longi: String? = ""

    val spotsArrayList = ArrayList<Spot>()

    private var nearByMeMsgList = ArrayList<NearByMeMsg>()
    private val manager by lazy { CardStackLayoutManager(baseActivity!!, this) }
    private val adapter by lazy { CardStackAdapter(createSpots(), this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_descover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as MainActivity).setToolbarVisibilityGone()
        init()
    }

    private fun init() {
        if (PermissionsManager.areLocationPermissionsGranted(baseActivity)) {
            getCurrentLocation()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(activity)
        }
        PermissionCallBack.getInstance(baseActivity!!).setButtonListener(this)


    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(baseActivity!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                lat = location?.latitude.toString()
                longi = location?.longitude.toString()
                hitUserNearByMeApi()
            }
    }

    override fun onPermissionCallBack(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        baseActivity?.showSnackBar(getString(R.string.user_location_permission_explanation))

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            getCurrentLocation()
        } else {
            baseActivity?.showSnackBar(getString(R.string.user_location_permission_not_granted))

            baseActivity?.finish()
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): Spot {
        return Spot(
            name = "Yasaka Shrine",
            userId = "7",
            city = "Kyoto",
            url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
        )
    }

    private fun createSpots(): List<Spot> {
//        val spots = ArrayList<Spot>()
//        spots.add(
//            Spot(
//                name = "Yasaka Shrine",
//                city = "Kyoto",
//                url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Fushimi Inari Shrine",
//                city = "Kyoto",
//                url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Bamboo Forest",
//                city = "Kyoto",
//                url = "https://source.unsplash.com/buF62ewDLcQ/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Brooklyn Bridge",
//                city = "New York",
//                url = "https://source.unsplash.com/THozNzxEP3g/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Empire State Building",
//                city = "New York",
//                url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "The statue of Liberty",
//                city = "New York",
//                url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Louvre Museum",
//                city = "Paris",
//                url = "https://source.unsplash.com/LrMWHKqilUw/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Eiffel Tower",
//                city = "Paris",
//                url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Big Ben",
//                city = "London",
//                url = "https://source.unsplash.com/CdVAUADdqEc/600x800"
//            )
//        )
//        spots.add(
//            Spot(
//                name = "Great Wall of China",
//                city = "China",
//                url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
//            )
//        )
        return spotsArrayList
    }

    override fun onSkipButton(effective_user_id:String) {
        hitLikeUnLikeApi(effective_user_id,AppConstants.PASS)

        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        card_stack_view.swipe()
    }

    override fun onRewindButton(effective_user_id:String) {
        val setting = RewindAnimationSetting.Builder()
            .setDirection(Direction.Bottom)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()
        manager.setRewindAnimationSetting(setting)
        card_stack_view.rewind()
    }

    override fun onLikeButton(effective_user_id:String) {
        hitLikeUnLikeApi(effective_user_id, AppConstants.LIKE)

        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        card_stack_view.swipe()
    }

    private fun hitUserNearByMeApi() {
        var userID = baseActivity!!.getProfileData().user_id
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.userNearByMeApi(
                NearByMeEntity(
                    50, 0, "30.3398", "76.3869", "", "female", userID.toString()
                )
            )
            call.enqueue(object : Callback<NearByMeWrapper> {
                override fun onResponse(
                    call: Call<NearByMeWrapper>?,
                    response: Response<NearByMeWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var nearByMeWrapper = response?.body()

                    if (nearByMeWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {

                        nearByMeMsgList.addAll(nearByMeWrapper!!.msg)
                        setDiscoverData(nearByMeMsgList)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<NearByMeWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun setDiscoverData(nearByMeMsgList: java.util.ArrayList<NearByMeMsg>) {
        for (i in 0 until nearByMeMsgList.size) {
            spotsArrayList.add(
                Spot(
                    name = nearByMeMsgList.get(i).first_name,
                    userId = nearByMeMsgList.get(i).effective_user_id,
                    city = nearByMeMsgList.get(i).about_me,
                    url = nearByMeMsgList.get(i).image1
                )
            )
        }

        setupCardStackView()

    }


    private fun hitLikeUnLikeApi(effectiveUserId: String, type: Int) {
        var userID = baseActivity!!.getProfileData().user_id
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.likeUnLikeApi(
                LikeAndDisLikeEntity(
                    effectiveUserId.toInt(), type.toString(), userID

                )
            )
            call.enqueue(object : Callback<EditProfileWrapper> {
                override fun onResponse(
                    call: Call<EditProfileWrapper>?,
                    response: Response<EditProfileWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var editProfileWrapper = response?.body()

                    if (editProfileWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity!!.showMessage(editProfileWrapper!!.msg.get(0).response)
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<EditProfileWrapper>?, t: Throwable?) {
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
