package com.demo.sparsetest

import android.content.res.AssetManager
import com.google.android.filament.Engine
import com.google.android.filament.IndirectLight
import com.google.android.filament.Skybox
import com.google.android.filament.utils.KTX1Loader
import java.nio.ByteBuffer


fun getIndirectLight(name: String, assets: AssetManager, engine: Engine): IndirectLight? {
    val indirectLight = KTX1Loader.createIndirectLight(
        engine,
        ByteBuffer.wrap(IOUtils.readAsset(assets, "envs/${name}_ibl.ktx"))
    )
    indirectLight.intensity = 20_000.0f
    return indirectLight
}

fun getSkyBox(name: String, assets: AssetManager, engine: Engine): Skybox {
    return KTX1Loader.createSkybox(
        engine,
        ByteBuffer.wrap(IOUtils.readAsset(assets, "envs/${name}_skybox.ktx"))
    )
}


