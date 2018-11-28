package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Entity
import kotlinx.android.synthetic.main.dashboard_light_view.view.*
import timber.log.Timber

class DashboardAdapter(val data: List<Entity>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_light_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = data[position]
        holder.bind(entity)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(entity: Entity) {

        val domain = entity.entity_id.split(".").first()
        itemView.overline.text = domain
        if (entity.attributes.containsKey("friendly_name")) {
            itemView.headline.text = entity.attributes["friendly_name"].toString()
        } else {
            itemView.headline.text = entity.entity_id
        }
        itemView.setOnClickListener {
            Timber.d(entity.toString())
        }
    }
}
