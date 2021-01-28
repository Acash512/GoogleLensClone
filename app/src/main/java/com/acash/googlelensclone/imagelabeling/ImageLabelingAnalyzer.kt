package com.acash.googlelensclone.imagelabeling

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.android.synthetic.main.activity_base_lens.*

class ImageLabelingAnalyzer(private val context: Context):ImageAnalysis.Analyzer {
    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.75F)
            .build()
    )

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it,imageProxy.imageInfo.rotationDegrees)
            labeler.process(inputImage)
                .addOnSuccessListener { labels->
                    labels.forEach{label->
                        Log.d("IMAGE LABELING","""
                            TEXT : ${label.text}
                            CONFIDENCE : ${label.confidence} 
                        """.trimIndent())
                        (context as Activity).tv.apply{
                            visibility = View.VISIBLE
                            text = """${label.text}(${label.confidence * 100}%)"""
                        }
                    }
                }
                .addOnFailureListener{exception->
                    Log.e("IMAGE LABELING","Error Labeling Image",exception)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
        } ?: imageProxy.close()
    }
}