package com.acash.googlelensclone.facedetection

import androidx.camera.core.CameraSelector
import androidx.core.content.ContextCompat
import com.acash.googlelensclone.BaseLensActivity

class FaceDetectionActivity : BaseLensActivity() {
    override val imageAnalyzer = FaceAnalyser()

    override fun startScanner() {
        detectFace()
    }

    private fun detectFace() {
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),imageAnalyzer)
    }

    override fun startCamera(){
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        super.startCamera()
    }
}