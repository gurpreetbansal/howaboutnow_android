package com.how_about_now.app.fragment.login_phase

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.how_about_now.app.R
import com.how_about_now.app.data.login_phase.LoginEntity
import com.how_about_now.app.data.login_phase.SignUpWrapper
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import com.how_about_now.app.utils.PermissionCallBack
import com.how_about_now.app.utils.PermissionsListener
import com.how_about_now.app.utils.PermissionsManager
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.emailET
import kotlinx.android.synthetic.main.fragment_login.passwordET
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment(), View.OnClickListener, PermissionsListener,
    PermissionCallBack.PermissionListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var lat: String? = ""
    private var longi: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        signInBT.setOnClickListener(this)
        forgotPasswordTV.setOnClickListener(this)
        passCancelIV.setOnClickListener(this)
        emailCancelIV.setOnClickListener(this)

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
            }
    }

    override fun onClick(v: View?) {
        when (v) {
            signInBT -> {
                if (isValidParms())
                    hitLoginApi()
            }
            forgotPasswordTV -> {
                var checkOutDialog = ForgotPasswordFragment()
                checkOutDialog.show(baseActivity!!.supportFragmentManager, "")
            }
            emailCancelIV -> {
                emailET.setText("")
            }
            passCancelIV -> {
                passwordET.setText("")
            }
        }
    }

    private fun isValidParms(): Boolean {
        if (baseActivity?.isEmptyCheck(emailET)!!) {
            baseActivity?.showMessage("Please enter your email")
        } else if (!baseActivity?.isValidEmail(baseActivity?.getTextValue(emailET)!!)!!) {
            baseActivity?.showMessage("Please enter valid email")
        } else if (baseActivity?.isEmptyCheck(passwordET)!!) {
            baseActivity?.showMessage("Please enter your password")
        } else return true
        return false
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

    private fun hitLoginApi() {
        var email = emailET.text.toString().trim()
        var password = passwordET.text.toString().trim()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.loginApi(
                LoginEntity(
                    "Android", baseActivity?.getDeviceUniqueID()!!, email,
                    lat!!, longi!!, password
                )
            )
            call.enqueue(object : Callback<SignUpWrapper> {
                override fun onResponse(
                    call: Call<SignUpWrapper>?,
                    response: Response<SignUpWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var signUpWrapper = response?.body()

                    if (signUpWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity?.store?.saveString(
                            AppConstants.AUTH_TOKEN,
                            signUpWrapper!!.msg[0].device_token
                        )
                        baseActivity?.saveProfileData(signUpWrapper!!.msg[0])
                        baseActivity?.gotoMainActivity()
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<SignUpWrapper>?, t: Throwable?) {
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
