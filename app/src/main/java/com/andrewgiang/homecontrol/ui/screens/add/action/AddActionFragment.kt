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
