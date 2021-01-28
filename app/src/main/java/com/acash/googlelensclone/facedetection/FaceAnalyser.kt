package com.acash.googlelensclone.facedetection

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceAnalyser:ImageAnalysis.Analyzer {
    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()
    )

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it,imageProxy.imageInfo.rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { faces->
                    faces.forEach{face->
                        Log.d("FACE DETECTION","""
                            LEFT EYE : ${face.leftEyeOpenProbability}
                            RIGHT EYE : ${face.rightEyeOpenProbability}
                            SMILE : ${face.smilingProbability}
                        """.trimIndent())
                    }
                }
                .addOnFailureListener{exception->
                    Log.e("FACE DETECTION","Error Detecting Face",exception)
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }
        } ?: imageProxy.close()
    }
}