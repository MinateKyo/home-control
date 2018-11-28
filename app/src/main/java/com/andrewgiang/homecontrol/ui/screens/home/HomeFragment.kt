package com.andrewgiang.homecontrol.ui.screens.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.andrewgiang.homecontrol.ActionShortcutManager
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.ui.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import javax.inject.Inject

class HomeFragment : BaseFragment(), ActionClickListener {

    companion object {
        private const val GRID_SPAN_SIZE = 3
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var actionShortcutManager: ActionShortcutManager

    lateinit var viewModel: HomeViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getControllerComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFab()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.onShortcutClick(activity?.intent?.extras?.getString("action_bundle_key"))
        actions.layoutManager = GridLayoutManager(context, GRID_SPAN_SIZE)
        viewModel.getAppActions().observe(viewLifecycleOwner, handleAppAction())
        viewModel.getViewState().observe(viewLifecycleOwner, onActionChanged())
        viewModel.isAuthenticated.observe(viewLifecycleOwner, Observer { authenticated ->
            if (!authenticated) {
                findNavController().navigate(HomeFragmentDirections.toSetupFragment())
            }
        })
    }

    private fun setupFab() {
        val iconDrawable = MaterialDrawableBuilder
            .with(context)
            .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
            .setColorResource(android.R.color.white)
            .build()
        fab.setImageDrawable(iconDrawable)
        fab.setOnClickListener {
            viewModel.onClick(
                AppAction.AddAction()
            )
        }
    }

    private fun onActionChanged(): Observer<HomeState> {
        return Observer { state ->
            actionShortcutManager.update(state.actionIds)

            val adapter = HomeActionAdapter(state.actionIds, this)
            actions.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun handleAppAction(): Observer<AppAction> {
        return Observer { action ->
            when (action) {
                is AppAction.FullScreen -> {
                    setFullScreen()
                }
                is AppAction.AddAction -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddActionFragment())
                }
            }
        }
    }

    private fun setFullScreen() {
        activity?.window?.decorView?.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onClick(action: Action) {
        viewModel.onClick(action)
    }
}
