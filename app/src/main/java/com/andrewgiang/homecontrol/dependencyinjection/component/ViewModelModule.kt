package com.andrewgiang.homecontrol.dependencyinjection.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andrewgiang.homecontrol.ui.screens.home.HomeViewModel
import com.andrewgiang.homecontrol.ui.screens.home.dashboard.DashboardViewModel
import com.andrewgiang.homecontrol.ui.screens.setup.UrlViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UrlViewModel::class)
    abstract fun modelUrl(viewModel: UrlViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun modelHome(viewModel: HomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun modelDashboard(viewModel: DashboardViewModel): ViewModel


}