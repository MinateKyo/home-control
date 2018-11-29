/*
 * Copyright 2018 Andrew Giang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

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

    companion object {
        private const val LANDSCAPE_COLUMNS = 3
        private const val PORTRAIT_COLUMNS = 2
    }

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
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            LANDSCAPE_COLUMNS else PORTRAIT_COLUMNS
    }
}
