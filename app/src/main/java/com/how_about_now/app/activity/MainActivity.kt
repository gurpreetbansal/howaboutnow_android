package com.how_about_now.app.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.DrawerAdapter
import com.how_about_now.app.data.DrawerData
import com.how_about_now.app.fragment.card_stack.DiscoverFragment
import com.how_about_now.app.fragment.home_phase.FavouritesFragment
import com.how_about_now.app.fragment.home_phase.MatchesFragment
import com.how_about_now.app.fragment.home_phase.ProfileFragment
import com.how_about_now.app.fragment.home_phase.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener, DrawerAdapter.DrawerCallBack,
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val AGENDA = 1
    private val CALENDER = 2
    private val TODO = 3
    private val SCHEDULES = 0
    private val GRADES = 4
    private val SETTINGS = 5
    private val LOGOUT = 6
    private val drawerDataArrayList = ArrayList<DrawerData>()
    private var toggleButton: ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //            window.setStatusBarColor(0x00000000); // transparent
            window.statusBarColor =
                ContextCompat.getColor(this, R.color.white_color) // transparent
//            window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.toolbar_gradient_color))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        setContentView(R.layout.activity_main)
        setToolbar()
        initDrawer()
        init()
        setToolbarVisibilityGone()
    }

    private fun init() {

        bottom_navigation.setOnNavigationItemSelectedListener(this)

        drawer.setStatusBarBackgroundColor(
            resources.getColor(android.R.color.white)
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

//        headerLL.requestLayout()
//        headerLL.setOnClickListener(this)
        toggleButton = object : ActionBarDrawerToggle(
            this,
            drawer,
            R.string.app_name,
            R.string.app_name
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                mainLL.setTranslationX(slideX)
            }
        }
        drawer.addDrawerListener(toggleButton as ActionBarDrawerToggle)

        callFragment(MatchesFragment(), R.id.container)
    }

    @SuppressLint("WrongConstant")
    private fun initDrawer() {
        val title = resources.getStringArray(R.array.drawer_title)
        val icon = arrayOf<Int>(
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
        )
        for (i in icon.indices) {
            val data = DrawerData()
            data.icon = icon[i]
            data.title = title[i]
            drawerDataArrayList.add(data)
        }
//        drawerItemRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        drawerItemRV.adapter = DrawerAdapter(this, drawerDataArrayList, this)

    }

    override fun onClick(v: View?) {

    }

//    override fun onPostCreate(savedInstanceState: Bundle, persistentState: PersistableBundle) {
//        super.onPostCreate(savedInstanceState, persistentState)
//        toggleButton?.syncState()
//
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        /*if (fragment is ScheduleFragment ||
            fragment is AgendaFragment ||
            fragment is HomeFragment ||
            fragment is MyTaskFragment ||
            fragment is SettingsFragment ||
            fragment is CalenderFragment ||
            fragment is AnnualReportFragment
        ) {
            toggleButton?.setDrawerIndicatorEnabled(true)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.drawer_shape)
        } else*/ /*if (fragment is ClassDetailFragment ||
            fragment is PrivacyPolicyFragment ||
            fragment is AddClassTimeFragment ||
            fragment is ReferFriendFragment
        )*/ /*{
            toggleButton?.setDrawerIndicatorEnabled(false)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn_shape)
        } *//*else {
            toggleButton?.setDrawerIndicatorEnabled(false)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        }*/
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton!!.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is MatchesFragment ||
            fragment is FavouritesFragment ||
            fragment is DiscoverFragment ||
            fragment is SettingsFragment ||
            fragment is ProfileFragment
        ) {
            backAction()
//        } else if (supportFragmentManager.backStackEntryCount > 0) {
//            supportFragmentManager.popBackStack()
//        } else {
//            gotoFragment(ScheduleFragment(), R.id.container)
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("NewApi")
    private fun setToolbar() {
        TransitionManager.beginDelayedTransition(mainLL, AutoTransition())
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

    }

    fun setToolbarTitle(title: String?) {
        TransitionManager.beginDelayedTransition(mainLL, AutoTransition())
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        titleLL.visibility = View.VISIBLE
        toolLL.visibility = View.GONE
        titleTV.setText(title ?: "")

    }

    fun setToolbarColor() {
        TransitionManager.beginDelayedTransition(mainLL, AutoTransition())
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }

    fun setToolbarVisibilityGone() {
        TransitionManager.beginDelayedTransition(layout as ViewGroup, AutoTransition())
        layout.visibility = View.GONE

    }

    fun setToolbarVisibility() {
        TransitionManager.beginDelayedTransition(layout as ViewGroup, AutoTransition())
        layout.visibility = View.VISIBLE
    }

//    fun setToolbarTitle(title: String?, imageUrl: String?) {
//        titleLL.visibility = View.GONE
//        toolLL.visibility = View.VISIBLE
//        nameTV.setText(title ?: "")
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            Glide.with(this).load(imageUrl).into(profileCIV)
//        } else {
//            profileCIV.setImageResource(R.drawable.profile)
//        }
//
//    }

    override fun drawerCallBack(postion: Int) {

        var fragment: Fragment? = null

        when (postion) {
            AGENDA -> {
//                fragment = AgendaFragment()
//                drawer.closeDrawers()
            }
            CALENDER -> {
//                fragment = CalenderFragment()
//                drawer.closeDrawers()
            }
            TODO -> {
//                fragment = MyTaskFragment()
//                drawer.closeDrawers()
            }
            SCHEDULES -> {
//                fragment = ScheduleFragment()
//                drawer.closeDrawers()
            }
            GRADES -> {
//                fragment = AnnualReportFragment()
//                drawer.closeDrawers()
            }
            SETTINGS -> {
//                fragment = SettingsFragment()
//                drawer.closeDrawers()
            }
            LOGOUT -> {
                gotoLoginSignupActivity()
//                drawer.closeDrawers()
            }
        }
        if (fragment != null) {
            TransitionManager.beginDelayedTransition(drawer, AutoTransition())
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_discover -> {
                gotoFragment(DiscoverFragment(), R.id.container)
            }
            R.id.action_profile -> {
                gotoFragment(ProfileFragment(), R.id.container)
            }
            R.id.action_settings -> {
                gotoFragment(SettingsFragment(), R.id.container)
            }
            R.id.action_favorites -> {
                gotoFragment(FavouritesFragment(), R.id.container)
            }
            R.id.action_matches -> {
                gotoFragment(MatchesFragment(), R.id.container)
            }
        }
        return true
    }
}
