package com.andrewgiang.homecontrol.ui.screens.home

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.model.Action
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
        itemView.icon.setSizeDp(24)
        itemView.icon.background = backgroundDrawable
        itemView.name.text = item.name
        view.icon.setOnClickListener {
            onActionClickListener.onClick(item)
        }
    }
}