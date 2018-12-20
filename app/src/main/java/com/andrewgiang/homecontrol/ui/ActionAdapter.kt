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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.model.applyFrom
import kotlinx.android.synthetic.main.home_actions_layout.view.*

interface ActionClickListener {
    fun onClick(action: Action)
    fun onDelete(action: Action)
    fun onEdit(action: Action)
}

class ActionAdapter constructor(
    private var items: List<Action>,
    private val onActionClickListener: ActionClickListener
) : RecyclerView.Adapter<ViewHolder>() {

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

    fun update(newActions: List<Action>) {
        val diffResult = DiffUtil.calculateDiff(ActionDiffUtil(items, newActions))
        diffResult.dispatchUpdatesTo(this)
        this.items = newActions
    }
}

class ViewHolder(
    view: View,
    val onActionClickListener: ActionClickListener
) : RecyclerView.ViewHolder(view) {

    fun bind(item: Action) {
        val icon = item.icon
        itemView.icon.applyFrom(icon)
        itemView.name.text = item.name
        itemView.icon.setOnClickListener {
            onActionClickListener.onClick(item)
        }

        itemView.icon.setOnLongClickListener { view ->
            PopupMenu(view.context, view).apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_action -> {
                            onActionClickListener.onEdit(item)
                            true
                        }
                        R.id.delete_action -> {
                            onActionClickListener.onDelete(item)
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.action)
                show()
            }
            return@setOnLongClickListener true
        }
    }
}