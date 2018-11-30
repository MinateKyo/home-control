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

package com.andrewgiang.homecontrol

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.andrewgiang.homecontrol.ui.Nav
import okhttp3.HttpUrl
import okhttp3.Request

fun Request.Builder.addAuthHeader(token: String): Request.Builder {
    val name = "Authorization"
    val bearerToken = "Bearer $token"
    this.header(name, bearerToken)
    return this
}

fun HttpUrl.androidUri(): Uri {
    return Uri.parse(this.toString())
}

fun NavController.observer(): Observer<in Nav> {
    return Observer { navInfo ->
        when (navInfo) {
            is Nav.PopStack -> {
                popBackStack()
            }
            is Nav.Direction -> {
                navigate(navInfo.navDirections)
            }
        }
    }
}

fun List<String>.firstOrEmpty(): String {
    val firstOrNull = this.firstOrNull()
    return firstOrNull ?: ""
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}