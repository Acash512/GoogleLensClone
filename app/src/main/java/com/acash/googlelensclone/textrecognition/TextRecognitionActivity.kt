package com.acash.googlelensclone.textrecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat
import com.acash.googlelensclone.BaseLensActivity

class TextRecognitionActivity : BaseLensActivity() {
    override val imageAnalyzer = TextAnalyzer(this)

    override fun startScanner() {
       recognizeText()
    }

    private fun recognizeText() {
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),imageAnalyzer)
    }
}