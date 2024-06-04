package com.demo.sparsetest

import com.google.android.filament.gltfio.Gltfio
import com.google.android.filament.utils.Utils
import java.util.concurrent.atomic.AtomicBoolean

object FilamentSoLoader {
    private const val TAG = "FilamentSoLoader"

    private val init = AtomicBoolean(false)

    @Synchronized
    fun load(): Boolean {

        if (init.get()) {
            return true
        }

        try {
            Gltfio.init()

            System.loadLibrary("filamat-jni")

            Utils.init()
            init.set(true)
        } catch (e: Throwable) {
            return false
        }
        return true
    }
}