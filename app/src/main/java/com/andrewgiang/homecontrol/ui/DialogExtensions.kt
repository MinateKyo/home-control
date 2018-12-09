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

package com.andrewgiang.homecontrol.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.byViewId
import com.github.rongi.klaster.Klaster
import kotlinx.android.synthetic.main.icon_chooser_view.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue

typealias IconCallback = (dialog: MaterialDialog, color: MaterialDrawableBuilder.IconValue) -> Unit

const val DEFAULT_ICON_SPAN_COUNT = 4

@SuppressLint("CheckResult")
fun MaterialDialog.iconChooser(
    iconValues: Array<IconValue>,
    iconCallback: IconCallback
): MaterialDialog {
    customListAdapter(iconAdapter(iconValues, this, iconCallback))
    getRecyclerView()?.layoutManager = GridLayoutManager(windowContext, DEFAULT_ICON_SPAN_COUNT)
    return this
}

fun iconAdapter(
    iconValues: Array<IconValue>,
    dialog: MaterialDialog,
    iconCallback: IconCallback
): RecyclerView.Adapter<RecyclerView.ViewHolder> {
    return Klaster.get()
        .itemCount(iconValues.size)
        .byViewId(R.layout.icon_chooser_view)
        .bind { position ->
            val iconValue = iconValues[position]
            iconView.setIcon(iconValue)
            iconView.setColorResource(R.color.md_white_1000)
            iconView.setOnClickListener {
                iconCallback.invoke(dialog, iconValue)
            }
        }.build()
}