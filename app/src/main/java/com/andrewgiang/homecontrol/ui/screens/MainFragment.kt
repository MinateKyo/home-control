package com.andrewgiang.homecontrol.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.ui.screens.home.HomeFragment
import com.andrewgiang.homecontrol.ui.screens.home.SettingsFragment
import com.andrewgiang.homecontrol.ui.screens.home.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : BaseFragment() {

    @Inject
    lateinit var authManager: AuthManager

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    val pages = listOf(
        HomeFragment(),
        DashboardFragment(), SettingsFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        getControllerComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = PagerAdapter(fragmentManager!!, pages)
    }

    override fun onResume() {
        super.onResume()

        if (!authManager.isAuthenticated()) {
            findNavController().navigate(MainFragmentDirections.toSetupFragment())
        }
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewpager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    viewpager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    viewpager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
}


class PagerAdapter(fragmentManager: FragmentManager, val pages: List<Fragment>) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

}