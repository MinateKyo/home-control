package com.andrewgiang.homecontrol.dependencyinjection.application

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
