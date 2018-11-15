package com.andrewgiang.homecontrol.data.model

import androidx.annotation.ColorRes
import com.andrewgiang.homecontrol.R
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

data class Icon(
    val iconValue: MaterialDrawableBuilder.IconValue,
    @ColorRes val iconColor: Int = R.color.default_icon_color,
    @ColorRes val backgroundColor: Int = R.color.default_icon_background
)