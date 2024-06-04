package com.demo.sparsetest

import android.content.res.AssetManager
import java.io.InputStream

object IOUtils {

    fun readAsset(asset: AssetManager, path: String): ByteArray {
        val inputStream = asset.open(path)
        val result = read(inputStream)
        inputStream.close()
        return result
    }

    fun read(inputStream: InputStream): ByteArray = inputStream.readBytes()


}