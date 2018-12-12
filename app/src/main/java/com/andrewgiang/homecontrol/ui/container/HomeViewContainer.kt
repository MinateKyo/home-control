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

package com.andrewgiang.homecontrol.ui.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.observer
import com.andrewgiang.homecontrol.ui.ActionAdapter
import com.andrewgiang.homecontrol.ui.ActionClickListener
import com.andrewgiang.homecontrol.viewmodel.HomeUiModel
import com.andrewgiang.homecontrol.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class HomeViewContainer(
    inflater: LayoutInflater,
    container: ViewGroup?,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val viewModel: HomeViewModel
) : Container, ActionClickListener {

    private val adapter = ActionAdapter(emptyList(), this)

    override fun onBindView() {
        setupFab()
        actions.layoutManager = GridLayoutManager(containerView.context, GRID_SPAN_SIZE)
        actions.adapter = adapter

        viewModel.getViewState().observe(viewLifecycleOwner, onActionChanged())
        viewModel.getNavState().observe(viewLifecycleOwner, navController.observer())
    }

    companion object {
        private const val GRID_SPAN_SIZE = 3
    }

    override val containerView: View =
        inflater.inflate(R.layout.fragment_home, container, false)

    fun onShortcutClicked(actionId: Long?) {
        viewModel.onShortcutClick(actionId)
    }

    private fun setupFab() {
        val iconDrawable = MaterialDrawableBuilder
            .with(containerView.context)
            .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
            .setColorResource(android.R.color.white)
            .build()
        fab.setImageDrawable(iconDrawable)
        fab.setOnClickListener {
            viewModel.onAddActionClick()
        }
    }

    private fun onActionChanged(): Observer<HomeUiModel> {
        return Observer { state ->
            val loading = state.loading
            when (loading.isLoading) {
                true -> {
                    loadingDelegate.show(R.string.action_run_msg, loading.message)
                }
                false -> {
                    state.loading.appError?.let { error ->
                        loadingDelegate.error(error.msg)
                    } ?: loadingDelegate.dismiss()
                }
            }
            adapter.update(state.actionIds)
        }
    }

    override fun onClick(action: Action) {
        viewModel.onClick(action)
    }

    override fun onDelete(action: Action) {
        MaterialDialog(containerView.context).show {
            message(R.string.delete_confirmation_msg)
            positiveButton(R.string.confirm) {
                viewModel.onDelete(action)
                dismiss()
            }
            negativeButton(R.string.cancel) {
                dismiss()
            }
        }
    }
}
