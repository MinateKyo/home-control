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

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.andrewgiang.homecontrol.APP_ACTION_ICONS
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.model.DataHolder
import com.andrewgiang.homecontrol.data.model.applyFrom
import com.andrewgiang.homecontrol.observer
import com.andrewgiang.homecontrol.ui.ShapeBuilder
import com.andrewgiang.homecontrol.ui.iconChooser
import com.andrewgiang.homecontrol.viewmodel.IconEditViewModel
import kotlinx.android.synthetic.main.home_actions_layout.*
import kotlinx.android.synthetic.main.icon_edit_controller.*

class IconEditContainer(
    inflater: LayoutInflater,
    container: ViewGroup?,
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val viewModel: IconEditViewModel
) : Container {

    override val containerView: View = inflater.inflate(R.layout.icon_edit_controller, container, false)

    override fun onBindView() {
        viewModel.getNavState().observe(lifecycleOwner, navController.observer())
        viewModel.getUiModel().observe(lifecycleOwner, Observer { ui ->
            icon.applyFrom(ui.homeIcon)
            name.text = ui.displayName
            backgroundColor.background = ShapeBuilder(color = ui.homeIcon.backgroundColorInt).build()
            iconColorDisplay.background = ShapeBuilder(color = ui.homeIcon.iconColorInt).build()
        })
        setupDialogs()
        displayNameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun handleBundleArgs(data: DataHolder) {
        addAction.setOnClickListener {
            viewModel.onAddButtonClicked(
                data.getSelected(),
                data.domainService,
                displayNameText.text.toString(),
                isShortcutCheckbox.isChecked
            )
        }
    }

    private fun setupDialogs() {
        iconLabel.setOnClickListener {
            MaterialDialog(containerView.context).show {
                title(R.string.choose_action_icon)
                iconChooser(APP_ACTION_ICONS) { dialog, iconValue ->
                    viewModel.onIconSelected(iconValue)
                    dialog.dismiss()
                }
            }
        }

        iconColorLabel.setOnClickListener {
            MaterialDialog(containerView.context).show {
                title(R.string.choose_color)
                colorChooser(getColors(R.array.material_icon_colors), allowCustomArgb = true) { dialog, color ->
                    viewModel.onIconColorSelected(color)
                    dialog.dismiss()
                }
            }
        }

        backgroundColorLabel.setOnClickListener {
            MaterialDialog(containerView.context)
                .show {
                    title(R.string.choose_background)
                    colorChooser(getColors(R.array.material_bg), allowCustomArgb = true) { dialog, color ->
                        viewModel.onBackgroundColorSelected(color)
                        dialog.dismiss()
                    }
                }
        }
    }

    private fun getColors(@ArrayRes colorArray: Int): IntArray {
        return containerView.context.resources.getIntArray(colorArray)
    }
}