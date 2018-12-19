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
import com.andrewgiang.homecontrol.viewmodel.ActionViewModel
import com.andrewgiang.homecontrol.viewmodel.AddActionUiModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.add_action_controller.*

class ActionViewContainer(
    inflater: LayoutInflater,
    container: ViewGroup?,
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val viewModel: ActionViewModel
) : Container {
    override val containerView: View = inflater.inflate(R.layout.add_action_controller, container, false)

    override fun onBindView() {
        viewModel.getUiState().observe(lifecycleOwner, Observer { action ->
            when (action) {
                is AddActionUiModel.Loading -> {
                }
                is AddActionUiModel.Error -> {
                    containerView.context.toast(action.appError.msg)
                }
                is AddActionUiModel.ServiceLoaded -> {
                    showServices(action.services, 0)
                }
                is AddActionUiModel.EntitiesLoaded -> {
                    val entities = action.entities
                    shouldShowEntityGroup(action.shouldShowEntities)
                    showEntityChips(entities, emptyList())
                }
                is AddActionUiModel.EditMode -> {
                    showServices(action.services, action.selectedServiceIndex)
                    shouldShowEntityGroup(action.shouldShowEntities)
                    showEntityChips(action.entities, action.selectedEntityId)
                }
            }
        })
        viewModel.getNavState().observe(lifecycleOwner, navController.observer())
        setupClickListeners()
    }

    private fun showEntityChips(
        entities: List<Entity>,
        selectedEntityId: List<String>
    ) {
        clearChipsViews()
        val list = entities.sortedByDescending { selectedEntityId.contains(it.entity_id) }

        list.forEach { entity ->
            addChip(entity, selectedEntityId.contains(entity.entity_id))
        }
    }

    fun bindAction(actionId: Long) {
        viewModel.load(actionId)
    }

    private fun setupClickListeners() {
        nextButton.setOnClickListener {
            if (domainServiceSpinner.selectedItem is String) {
                viewModel.onNextButtonClicked(domainServiceSpinner.selectedItem as String)
            }
        }
    }

    private fun shouldShowEntityGroup(isVisible: Boolean) {
        entityGroup.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showServices(listDomainService: List<String>, selectedServiceIndex: Int) {
        domainServiceSpinner.adapter = ArrayAdapter(
            containerView.context,
            android.R.layout.simple_list_item_1,
            listDomainService
        )
        domainServiceSpinner.setSelection(selectedServiceIndex, false)
        domainServiceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    viewModel.onDomainServiceSelected(view.text.toString())
                }
            }
        }
    }

    private fun clearChipsViews() {
        entityChipGroup.removeAllViews()
        viewModel.clearChips()
    }

    private fun addChip(entity: Entity, isSelected: Boolean) {
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
        chip.isChecked = isSelected
    }
}
