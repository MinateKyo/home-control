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
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import javax.inject.Inject

class ActionShortcutManager @Inject constructor(
    private val shortcutManager: ShortcutManager,
    private val context: Context,
    moshi: Moshi
) {
    companion object {
        const val SHORTCUT_LIMIT = 4
    }
    private val shortcutUri = "home-shortcut://action/"
    private val actionBundleKey = "action_bundle_key"
    private val adapter: JsonAdapter<Data.ServiceData> = moshi.adapter(
        Data.ServiceData::class.java
    )

    fun update(actionIds: List<Action>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager.removeAllDynamicShortcuts()
            shortcutManager.dynamicShortcuts = actionIds
                .take(SHORTCUT_LIMIT)
                .filter { it.isShortcut }
                .map {
                    val intent = createIntent(it)
                    return@map toShortcutInfo(it, intent)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun toShortcutInfo(
        it: Action,
        intent: Intent
    ): ShortcutInfo {
        return ShortcutInfo
            .Builder(context, it.name)
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
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(shortcutUri)
        )
        val dataJson = toJson(it)
        intent.putExtra(actionBundleKey, dataJson)
        return intent
    }

    private fun toJson(action: Action): String? {
        if (action.data is Data.ServiceData) {
            return adapter.toJson(Data.ServiceData(action.data.entityId, action.data.domain, action.data.service))
        }
        return null
    }

    fun parseShortcutData(json: String?): Data.ServiceData? {
        return if (json != null) adapter.fromJson(json) else null
    }
}