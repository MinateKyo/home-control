package com.andrewgiang.homecontrol.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.dagger.component.ControllerModule

class MainActivity : AppCompatActivity() {

    val controllerComponent: ControllerComponent by lazy {
        (application as App).applicationComponent
            .create(ControllerModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()


}
