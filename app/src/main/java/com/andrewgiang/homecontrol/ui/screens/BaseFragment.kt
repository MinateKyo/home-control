package com.andrewgiang.homecontrol.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.ui.MainActivity

abstract class BaseFragment : Fragment() {

    fun getControllerComponent(): ControllerComponent {
        return (activity as MainActivity).controllerComponent
    }

    @LayoutRes
    abstract fun getLayoutId(): Int


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

}