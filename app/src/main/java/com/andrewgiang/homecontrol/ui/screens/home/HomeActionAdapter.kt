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

package com.andrewgiang.homecontrol.ui.screens.home

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.ui.ShapeBuilder
import kotlinx.android.synthetic.main.home_actions_layout.view.*

interface ActionClickListener {
    fun onClick(action: Action)
}

class HomeActionAdapter constructor(
    private val items: List<Action>,
    private val onActionClickListener: ActionClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_actions_layout, parent, false)
        return ViewHolder(view, onActionClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ViewHolder(
    private val view: View,
    private val onActionClickListener: ActionClickListener
) : RecyclerView.ViewHolder(view) {

    fun bind(item: Action) {
        val icon = item.icon

        val backgroundDrawable = ShapeBuilder(
            shape = GradientDrawable.OVAL,
            color = view.resources.getColor(icon.backgroundColor)
        ).build()

        itemView.icon.setIcon(icon.iconValue)
        itemView.icon.setColorResource(icon.iconColor)
        itemView.icon.setSizeDp(DEFAULT_ICON_SIZE_DP)
        itemView.icon.background = backgroundDrawable
        itemView.name.text = item.name
        view.icon.setOnClickListener {
            onActionClickListener.onClick(item)
        }
    }

    companion object {
        private const val DEFAULT_ICON_SIZE_DP = 42
    }
}