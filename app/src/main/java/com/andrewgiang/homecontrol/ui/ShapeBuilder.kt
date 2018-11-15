package com.andrewgiang.homecontrol.ui

import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt

data class ShapeBuilder(
    val orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    val shape: Int, //TODO create int def annotation
    @ColorInt val color: Int
) {

    fun build(): GradientDrawable {
        val drawable =
            GradientDrawable(orientation, intArrayOf(color, color))
        drawable.shape = shape
        return drawable
    }
}



