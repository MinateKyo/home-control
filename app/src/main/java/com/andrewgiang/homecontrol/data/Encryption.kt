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