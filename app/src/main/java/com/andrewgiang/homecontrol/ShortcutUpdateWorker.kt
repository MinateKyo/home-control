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

package com.andrewgiang.homecontrol

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import kotlinx.coroutines.runBlocking
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import javax.inject.Inject

class ShortcutUpdateWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    @Inject
    lateinit var shortcutManager: ShortcutManager
    @Inject
    lateinit var actionRepo: ActionRepo

    init {
        (context as App).applicationComponent.inject(this@ShortcutUpdateWorker)
    }

    companion object {
        const val SHORTCUT_LIMIT = 4
    }

    private val shortcutBuilder = Uri.parse("home-shortcut://action").buildUpon()

    override fun doWork(): Result = runBlocking {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val actions = actionRepo.getActions()
            shortcutManager.removeAllDynamicShortcuts()
            shortcutManager.dynamicShortcuts = actions
                .take(SHORTCUT_LIMIT)
                .filter { it.isShortcut }
                .map {
                    val intent = createIntent(it)
                    return@map toShortcutInfo(it, intent)
                }
        }
        return@runBlocking Result.SUCCESS
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun toShortcutInfo(
        it: Action,
        intent: Intent
    ): ShortcutInfo {
        return ShortcutInfo.Builder(context, it.name)
            .setShortLabel(it.name)
            .setIcon(createIconBitmap(it))
            .setIntent(intent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createIconBitmap(it: Action): Icon? {
        return Icon.createWithBitmap(
            MaterialDrawableBuilder.with(context)
                .setIcon(it.icon.iconValue)
                .setColorResource(it.icon.iconColor)
                .build()
                .toBitmap()
        )
    }

    private fun createIntent(it: Action): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            buildUri(it)
        )
    }

    private fun buildUri(it: Action) =
        shortcutBuilder.path(it.id.toString()).build()
}