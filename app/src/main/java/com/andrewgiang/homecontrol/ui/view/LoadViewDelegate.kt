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

package com.andrewgiang.homecontrol.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

class LoadViewDelegate : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val MINIMUM_SHOW_TIME = 700L
    }
    private var startTime: Long = -1L
    private var snackbar: Snackbar? = null

    private val dismissRunnable: () -> Unit = {
        snackbar?.dismiss()
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        removeCallbacks(dismissRunnable)
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(dismissRunnable)
    }

    override fun onDraw(canvas: Canvas?) {}

    @Suppress("SpreadOperator")
    fun show(@StringRes stringRes: Int, vararg formatArgs: Any) {
        show(resources.getString(stringRes, *formatArgs))
    }

    fun show(message: String) {
        snackbar?.dismiss()
        Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).apply {
            startTime = System.currentTimeMillis()
            snackbar = this
            show()
        }
    }

    fun error(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun dismiss() {
        val diff = System.currentTimeMillis() - startTime
        if (diff < MINIMUM_SHOW_TIME) {
            postDelayed(dismissRunnable, MINIMUM_SHOW_TIME - diff)
        } else {
            snackbar?.dismiss()
        }
    }
}
