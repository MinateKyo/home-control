package com.andrewgiang.homecontrol.ui.screens.add.action

import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import javax.inject.Inject

class AddActionViewModel @Inject constructor(
    actionRepo: ActionRepo,
    dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {

}