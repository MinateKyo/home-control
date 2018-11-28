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
