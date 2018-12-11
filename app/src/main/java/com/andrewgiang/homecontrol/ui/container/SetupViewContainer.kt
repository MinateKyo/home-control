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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.toast
import com.andrewgiang.homecontrol.ui.controller.SetupControllerDirections
import com.andrewgiang.homecontrol.viewmodel.AuthState
import com.andrewgiang.homecontrol.viewmodel.SetupViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SetupViewContainer(
    inflater: LayoutInflater,
    container: ViewGroup?,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val viewModel: SetupViewModel

) : Container {
    override val containerView: View = inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onBindView() {
        viewModel.getData().observe(
            viewLifecycleOwner,
            Observer { data ->
                if (data.isLoading) {
                    progressBar.show()
                    nextButton.isEnabled = false
                } else {
                    progressBar.hide()
                    nextButton.isEnabled = true
                }
                if (data.authState == AuthState.AUTHENTICATED) {
                    navController.navigate(SetupControllerDirections.backToMain())
                }

                if (data.errorMessage != null) {
                    containerView.context.toast(data.errorMessage)
                }
            })

        nextButton.setOnClickListener {
            viewModel.onNextClick(urlText.text.toString())
        }
    }

    fun codeArgument(arguments: Bundle? = null) {
        viewModel.onAppLinkRedirect(arguments?.getString("code"))
    }
}