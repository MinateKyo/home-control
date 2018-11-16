package com.andrewgiang.homecontrol.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.assistantsdk.request.Service
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Icon
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue
import timber.log.Timber
import javax.inject.Inject


class HomeViewModel @Inject constructor(val holder: ApiHolder, dispatchProvider: DispatchProvider) :
    ScopeViewModel(dispatchProvider) {


    private val data = MutableLiveData<HomeState>()

    private val appAction = MutableLiveData<AppAction>()

    init {
        data.postValue(HomeState())
    }

    fun getData(): LiveData<HomeState> {
        return data
    }

    fun getAppActions(): LiveData<AppAction> {
        return appAction
    }

    fun onClick(action: Action) {
        when (action.service) {
            is AppAction -> {
                appAction.postValue(action.service)
            }
            else -> {
                invokeApiAction(action)
            }
        }
    }

    private fun invokeApiAction(action: Action) = launch {
        try {
            val list = holder.api.service(action.entityId, action.service).await()
            Timber.d(list.toString())
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

}


//TODO these are customizable dashboard actions, get these from a database
val hardCodedActions = listOf(
    Action(
        "group.all_lights",
        Service("light", "turn_off"),
        Icon(
            IconValue.LIGHTBULB,
            R.color.darkblue_600, R.color.darkblue_100
        ), "Off"
    ),
    Action(
        "group.all_lights",
        Service("light", "turn_on"),
        Icon(
            IconValue.LIGHTBULB,
            R.color.yellow_600, R.color.yellow_100
        ), "On"
    ),
    Action(
        "app.fullscreen",
        AppAction.FullScreen(),
        Icon(IconValue.FULLSCREEN),
        "Fullscreen"
    ),
    Action(
        "app.add_action",
        AppAction.AddAction(),
        Icon(IconValue.PLUS),
        "Add"
    )

)

data class HomeState(
    val actionIds: List<Action> = hardCodedActions,
    val isLoading: Boolean = false
)
