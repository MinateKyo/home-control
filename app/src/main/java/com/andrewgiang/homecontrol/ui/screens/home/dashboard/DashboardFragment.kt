package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.ui.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject

class DashboardFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getControllerComponent().inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java)
        dashboardGrid.layoutManager =
                GridLayoutManager(
                    context,
                    getColumns()
                )
        viewModel.refreshData()

        viewModel.getEntities()
            .observe(this, Observer { list ->
                dashboardGrid.adapter = DashboardAdapter(list)
            })
    }

    private fun getColumns(): Int {
        val landscapeColumns = 3
        val portraitColumns = 2
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            landscapeColumns else portraitColumns
    }
}
