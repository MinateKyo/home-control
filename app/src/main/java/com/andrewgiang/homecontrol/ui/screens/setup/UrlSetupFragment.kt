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

package com.andrewgiang.homecontrol.ui.screens.setup

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.ui.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class UrlSetupFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutId(): Int {
        return R.layout.fragment_sign_in
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getControllerComponent().inject(this)
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(UrlViewModel::class.java)

        viewModel.getData().observe(this,
            Observer { data ->
                if (data.isLoading) {
                    progressBar.show()
                    nextButton.isEnabled = false
                } else {
                    progressBar.hide()
                    nextButton.isEnabled = true
                }
                if (data.authState == AuthState.AUTHENTICATED) {
                    findNavController().navigate(UrlSetupFragmentDirections.backToMain())
                }

                if (data.errorMessage != null) {
                    Toast.makeText(context, data.errorMessage, Toast.LENGTH_SHORT).show()
                }
            })

        viewModel.onAppLinkRedirect(arguments?.getString("code"))

        nextButton.setOnClickListener {
            viewModel.onNextClick(urlText.text.toString())
        }
    }
}
