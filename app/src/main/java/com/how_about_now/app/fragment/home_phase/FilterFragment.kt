package com.how_about_now.app.fragment.home_phase

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.fragment.GenderSelectionDialogFragment
import com.how_about_now.app.utils.DiscreteSlider
import com.how_about_now.app.utils.DiscreteSlider.MODE_NORMAL
import com.how_about_now.app.utils.DiscreteSlider.OnValueChangedListener
import com.how_about_now.app.utils.GenderSelectionCallBack
import kotlinx.android.synthetic.main.fragment_filter.*


/**
 * A simple [Fragment] subclass.
 */
class FilterFragment : BaseFragment(), View.OnClickListener,
    GenderSelectionCallBack.GenderSelectionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSet()
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun dataSet() {
        genderOptionTV.setText(baseActivity!!.store.getString("gender", "Female")!!)
        distanceCalTV.setText(baseActivity!!.store.getString("distance", "50")!!+" "+"km")
        heightCalTV.setText(
            baseActivity!!.store.getString(
                "heightMinimum",
                "0"
            )!! + "-" + baseActivity!!.store.getString("heightMaximum", "200")!! + " " + "cm"
        )
        ageYearTV.setText(
            baseActivity!!.store.getString(
                "minProgress",
                "0"
            )!! + "-" + baseActivity!!.store.getString("maxProgress", "50")!! + " " + "yr"
        )

        ageDS.mode = DiscreteSlider.MODE_RANGE
        ageDS.minProgress = baseActivity!!.store.getString(
            "minProgress",
            "0"
        )!!.toInt()
        ageDS.maxProgress = baseActivity!!.store.getString(
            "maxProgress",
            "50"
        )!!.toInt()

        heightDS.mode = DiscreteSlider.MODE_RANGE
        heightDS.minProgress = baseActivity!!.store.getString(
            "heightMinimum",
            "0"
        )!!.toInt()
        heightDS.maxProgress = baseActivity!!.store.getString(
            "heightMaximum",
            "200"
        )!!.toInt()

        distanceDS.mode = DiscreteSlider.MODE_RANGE
        distanceDS.maxProgress = baseActivity!!.store.getString(
            "distance",
            "50"
        )!!.toInt()
    }

    private fun init() {

        genderOptionTV.setOnClickListener(this)
        GenderSelectionCallBack.getInstance(baseActivity!!).setGenderSelectionListener(this)

//        heightCalTV.setText(heightDS.minProgress.toString() + "-" + heightDS.maxProgress.toString() + " " + "cm")
//        ageYearTV.setText(ageDS.minProgress.toString() + "-" + ageDS.maxProgress.toString() + " " + "yr")
//        distanceCalTV.setText(distanceDS.progress.toString() + " " + "km")


        ageDS.setOnValueChangedListener(object : OnValueChangedListener() {
            override fun onValueChanged(progress: Int, fromUser: Boolean) {
                super.onValueChanged(progress, fromUser)
            }

            override fun onValueChanged(
                minProgress: Int,
                maxProgress: Int,
                fromUser: Boolean
            ) {
                ageYearTV.setText(minProgress.toString() + "-" + maxProgress.toString() + " " + "yr")
                baseActivity!!.store.saveString("minProgress", minProgress.toString())
                baseActivity!!.store.saveString("maxProgress", maxProgress.toString())
                super.onValueChanged(minProgress, maxProgress, fromUser)
            }
        })

        distanceDS.setOnValueChangedListener(object : OnValueChangedListener() {
            override fun onValueChanged(progress: Int, fromUser: Boolean) {

                super.onValueChanged(progress, fromUser)
            }

            override fun onValueChanged(
                minProgress: Int,
                maxProgress: Int,
                fromUser: Boolean
            ) {
                distanceCalTV.setText(maxProgress.toString() + " " + "km")
                baseActivity!!.store.saveString("distance", maxProgress.toString())
                super.onValueChanged(minProgress, maxProgress, fromUser)
            }
        })

        heightDS.setOnValueChangedListener(object : OnValueChangedListener() {
            override fun onValueChanged(progress: Int, fromUser: Boolean) {
                super.onValueChanged(progress, fromUser)
            }

            override fun onValueChanged(
                minProgress: Int,
                maxProgress: Int,
                fromUser: Boolean
            ) {
                heightCalTV.setText(minProgress.toString() + "-" + maxProgress.toString() + " " + "cm")
                baseActivity!!.store.saveString("heightMinimum", minProgress.toString())
                baseActivity!!.store.saveString("heightMaximum", maxProgress.toString())
                super.onValueChanged(minProgress, maxProgress, fromUser)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            genderOptionTV -> {
                var genderSelectionDialogFragment = GenderSelectionDialogFragment()
                genderSelectionDialogFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    genderSelectionDialogFragment.getTag()
                )
            }
        }
    }

    override fun onGenderSelectionCallBack(gender: String?) {
        baseActivity!!.store.saveString("gender", gender!!)
        genderOptionTV.setText(gender)
    }

}
