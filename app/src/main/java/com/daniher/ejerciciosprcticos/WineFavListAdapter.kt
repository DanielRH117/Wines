package com.daniher.ejerciciosprcticos

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class WineFavListAdapter: WineListAdapter() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val wine = getItem(position)
        (holder as ViewHolder).run {
            with(binding) {
                cbFavorite.apply {
                    isChecked= wine.isFavourite
                    visibility = View.VISIBLE
                }
            }
        }
    }
}