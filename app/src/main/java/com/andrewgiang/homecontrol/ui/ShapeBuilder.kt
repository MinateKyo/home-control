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

import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt

data class ShapeBuilder(
    val orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    val shape: Int, // TODO create int def annotation
    @ColorInt val color: Int
) {

    fun build(): GradientDrawable {
        val drawable =
            GradientDrawable(orientation, intArrayOf(color, color))
        drawable.shape = shape
        return drawable
    }
}