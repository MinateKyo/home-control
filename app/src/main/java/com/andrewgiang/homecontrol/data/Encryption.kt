package com.andrewgiang.homecontrol.data

import android.util.Base64
import com.facebook.crypto.Crypto
import com.facebook.crypto.Entity
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


interface Encryption {
    fun decrypt(value: String): String

    fun encrypt(value: String): String

}

class ConcealEncrption constructor(
    val entity: Entity,
    val crypto: Crypto
) : Encryption {

    init {
        if (!crypto.isAvailable) {
            Timber.e("Failure to load crypto")
        }
    }

    override fun encrypt(value: String): String {
        val bout = ByteArrayOutputStream()
        val cryptoStream = crypto.getCipherOutputStream(bout, entity)
        cryptoStream.write(value.toByteArray())
        cryptoStream.close()
        val result = Base64.encodeToString(bout.toByteArray(), Base64.DEFAULT)
        bout.close()
        return result
    }

    override fun decrypt(value: String): String {
        val bin = ByteArrayInputStream(Base64.decode(value, Base64.DEFAULT))
        val cryptoStream = crypto.getCipherInputStream(bin, entity)
        val bout = ByteArrayOutputStream()
        cryptoStream.copyTo(bout, bufferSize = 1024)
        cryptoStream.close()
        val result = String(bout.toByteArray())
        bin.close()
        bout.close()
        return result
    }

}