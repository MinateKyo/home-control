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

package com.andrewgiang.homecontrol.ui.screens.add.action

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.ui.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_action.*
import javax.inject.Inject

class AddActionFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddActionViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_action
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getControllerComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddActionViewModel::class.java)
        val actionView = ActionView(
            viewModel,
            requireContext(),
            viewLifecycleOwner,
            findNavController(),
            Views(
                entityGroup,
                entityChipGroup,
                domainServiceSpinner,
                addButton,
                displayNameField,
                isShortcutCheckBox
            )
        )
        actionView.setup()
    }
}
