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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.observer
import com.andrewgiang.homecontrol.toast
import com.andrewgiang.homecontrol.viewmodel.AddActionUiModel
import com.andrewgiang.homecontrol.viewmodel.AddActionViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_add_action.*

class AddActionViewContainer(
    inflater: LayoutInflater,
    container: ViewGroup,
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val viewModel: AddActionViewModel
) : LayoutContainer {

    override val containerView: View = inflater.inflate(R.layout.fragment_add_action, container, false)

    fun bindView() {
        viewModel.onBind()
        viewModel.getUiState().observe(lifecycleOwner, Observer { action ->
            when (action) {
                is AddActionUiModel.Loading -> {
                }
                is AddActionUiModel.Error -> {
                    containerView.context.toast(action.appError.msg)
                }
                is AddActionUiModel.ServiceLoaded -> {
                    showServices(action.services)
                }
                is AddActionUiModel.EntitiesLoaded -> {
                    val entities = action.entities
                    showEntities(action.shouldShowEntities)
                    clearChipsViews()
                    entities.forEach { entity ->
                        addChip(entity)
                    }
                }
            }
        })
        viewModel.getNavState().observe(lifecycleOwner, navController.observer())
        setupClickListeners()
    }

    private fun setupClickListeners() {
        domainServiceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    viewModel.onItemSelected(view.text.toString())
                }
            }
        }

        nextButton.setOnClickListener {
            if (domainServiceSpinner.selectedItem is String) {
                viewModel.onNextButtonClicked(domainServiceSpinner.selectedItem as String)
            }
        }
    }

    private fun showEntities(isVisible: Boolean) {
        entityGroup.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showServices(listDomainService: List<String>) {
        domainServiceSpinner.adapter = ArrayAdapter(
            containerView.context,
            android.R.layout.simple_list_item_1,
            listDomainService
        )
    }

    private fun clearChipsViews() {
        entityChipGroup.removeAllViews()
        viewModel.clearChips()
    }

    private fun addChip(entity: Entity) {
        val chip = Chip(containerView.context)
        chip.setTextColor(ContextCompat.getColor(containerView.context, R.color.md_white_1000))
        chip.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onChipChecked(entity, isChecked)
        }
        chip.setChipBackgroundColorResource(R.color.chip_bg_state)
        chip.isClickable = true
        chip.isCheckable = true
        chip.text = entity.getFriendlyName()
        entityChipGroup.addView(chip)
    }
}
