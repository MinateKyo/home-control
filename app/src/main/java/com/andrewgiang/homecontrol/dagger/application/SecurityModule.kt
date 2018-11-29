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

package com.andrewgiang.homecontrol.dagger.application

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.andrewgiang.homecontrol.BuildConfig
import com.andrewgiang.homecontrol.data.AuthPrefs
import com.andrewgiang.homecontrol.data.AuthPrefsSecure
import com.andrewgiang.homecontrol.data.ConcealEncrption
import com.andrewgiang.homecontrol.data.Encryption
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.Crypto
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.facebook.crypto.keychain.KeyChain
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

@Module
class SecurityModule {

    @Provides
    fun sharedPrefs(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    fun sharedPrefsSecure(
        sharedPreferences: SharedPreferences,
        moshi: Moshi,
        encryption: Encryption
    ): AuthPrefs {
        return AuthPrefsSecure(sharedPreferences, moshi, encryption)
    }

    @ApplicationScope
    @Provides
    fun keychain(app: Application): KeyChain {
        return SharedPrefsBackedKeyChain(app, CryptoConfig.KEY_256)
    }

    @ApplicationScope
    @Provides
    fun crypto(keyChain: KeyChain): Crypto {
        return AndroidConceal.get().createDefaultCrypto(keyChain)
    }

    @ApplicationScope
    @Provides
    fun concealEntity(): Entity {
        return Entity.create(BuildConfig.APPLICATION_ID)
    }

    @ApplicationScope
    @Provides
    fun concealEncrption(entity: Entity, crypto: Crypto): Encryption {
        return ConcealEncrption(entity, crypto)
    }
}