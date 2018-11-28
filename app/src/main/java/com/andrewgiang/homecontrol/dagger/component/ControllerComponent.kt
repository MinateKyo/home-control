package com.andrewgiang.homecontrol.dagger.component

import com.andrewgiang.homecontrol.ui.screens.home.HomeFragment
import com.andrewgiang.homecontrol.ui.screens.home.dashboard.DashboardFragment
import com.andrewgiang.homecontrol.ui.screens.setup.UrlSetupFragment
import dagger.Subcomponent

@Subcomponent(modules = [ControllerModule::class, ViewModelModule::class])
interface ControllerComponent {
    fun inject(fragment: UrlSetupFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: DashboardFragment)
}
