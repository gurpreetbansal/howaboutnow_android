package com.how_about_now.app.fragment.login_phase

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.utils.PermissionCallBack
import com.how_about_now.app.utils.PermissionsListener
import com.how_about_now.app.utils.PermissionsManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment(), View.OnClickListener , PermissionsListener,
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
                baseActivity!!.gotoMainActivity()
            }
            forgotPasswordTV -> {
                var checkOutDialog = ForgotPasswordFragment()
                checkOutDialog.show(baseActivity!!.supportFragmentManager, "")
            }
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
}
