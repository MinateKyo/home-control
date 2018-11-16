package com.andrewgiang.homecontrol.dagger.component

import com.andrewgiang.homecontrol.ui.screens.MainFragment
import com.andrewgiang.homecontrol.ui.screens.home.HomeFragment
import com.andrewgiang.homecontrol.ui.screens.home.dashboard.DashboardFragment
import com.andrewgiang.homecontrol.ui.screens.setup.UrlSetupFragment
import dagger.Subcomponent

@Subcomponent(modules = [ControllerModule::class, ApiModule::class, ViewModelModule::class])
interface ControllerComponent {
    fun inject(fragment: MainFragment)
    fun inject(fragment: UrlSetupFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: DashboardFragment)
}


