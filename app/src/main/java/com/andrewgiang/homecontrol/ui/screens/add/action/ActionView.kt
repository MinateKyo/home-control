package com.andrewgiang.homecontrol.ui.screens.add.action

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class ActionView(
    val viewModel: AddActionViewModel,
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController,
    val views: Views
) {
    private val checkedSet = mutableSetOf<Entity>()

    fun setup() {
        viewModel.loadServices()
        viewModel.getEntities().observe(lifecycleOwner, observeEntityChange())
        viewModel.getViewState().observe(lifecycleOwner, observeViewStateChange())
        initializeClickListeners()
    }

    private fun observeViewStateChange(): Observer<ViewState> {
        return Observer { viewState ->
            if (viewState.shouldFinish) {
                navController.popBackStack()
            } else {
                views.domainServiceSpinner.adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    viewState.listDomainService
                )
            }
        }
    }

    private fun observeEntityChange(): Observer<List<Entity>> {
        return Observer { entities ->
            views.entityGroup.visibility = if (entities.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            views.entityChipGroup.removeAllViews()
            checkedSet.clear()
            entities.forEach { entity ->
                val chip = Chip(context)
                if (entities.size == 1) {
                    chip.isChecked = true
                }
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        checkedSet.add(entity)
                    } else {
                        checkedSet.remove(entity)
                    }
                }
                chip.setChipBackgroundColorResource(R.color.chip_bg_state)
                chip.isClickable = true
                chip.isCheckable = true
                chip.text = entity.entity_id
                views.entityChipGroup.addView(chip)
            }
        }
    }

    private fun initializeClickListeners() {
        views.domainServiceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    viewModel.onItemSelected(view.text.toString())
                }
            }
        }

        views.addButton.setOnClickListener {
            viewModel.addAction(
                views.domainServiceSpinner.selectedItem as String,
                checkedSet.toList().map { entity -> entity.entity_id },
                views.displayNameField.text.toString(),
                views.shortcutCheckBox.isChecked
            )
        }
    }
}

data class Views(
    val entityGroup: Group,
    val entityChipGroup: ChipGroup,
    val domainServiceSpinner: Spinner,
    val addButton: Button,
    val displayNameField: TextInputEditText,
    val shortcutCheckBox: MaterialCheckBox
)