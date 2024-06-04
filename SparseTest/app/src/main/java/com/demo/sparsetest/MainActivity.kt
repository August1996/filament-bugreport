package com.demo.sparsetest

import android.os.Bundle
import android.view.Choreographer
import android.view.SurfaceView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.ModelViewer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            FilamentSoLoader.load()
        }
    }

    private val renderCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            updateMorphTargets()

            modelViewer.render(frameTimeNanos)

            choreographer.postFrameCallback(this)
        }

    }

    private val choreographer: Choreographer by lazy {
        Choreographer.getInstance()
    }

    private val surfaceView: SurfaceView by lazy {
        findViewById(R.id.surfaceView)
    }

    private val spinner: Spinner by lazy {
        findViewById(R.id.spinner)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val modelAssetPath: List<String> by lazy {
        (assets.list("models") ?: emptyArray()).map { "models/$it" }.toList()
    }


    private val modelViewer: ModelViewer by lazy {
        ModelViewer(surfaceView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modelAssetPath)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                loadGLB(modelAssetPath[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        modelViewer.scene.skybox = getSkyBox("default_env", assets, modelViewer.engine)
        modelViewer.scene.indirectLight = getIndirectLight("default_env", assets, modelViewer.engine)
    }

    private fun loadGLB(path: String) {
        modelViewer.loadModelGlb(ByteBuffer.wrap(IOUtils.readAsset(assets, path)))
        modelViewer.transformToUnitCube(Float3(0.0f, -0.8f, -0.05f))
    }

    private fun updateMorphTargets() {
        val entity = modelViewer.asset?.getFirstEntityByName("Wolf3D_Avatar")
        if (entity != null) {
            val rcm = modelViewer.engine.renderableManager
            val ri = rcm.getInstance(entity)
            val weights = FloatArray(rcm.getMorphTargetCount(ri))
            val size = weights.size
            for (i in 0 until size) {
                weights[i] = seekBar.progress / 100F
            }
            rcm.setMorphWeights(ri, weights, 0)
        }
    }


    override fun onStart() {
        super.onStart()
        choreographer.postFrameCallback(renderCallback)
    }

    override fun onStop() {
        choreographer.removeFrameCallback(renderCallback)
        super.onStop()
    }

}