package com.acash.googlelensclone.imagelabeling

import androidx.core.content.ContextCompat
import com.acash.googlelensclone.BaseLensActivity

class ImageLabelingActivity : BaseLensActivity() {
    override val imageAnalyzer = ImageLabelingAnalyzer(this)

    override fun startScanner() {
        labelImage()
    }

    private fun labelImage(){
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),imageAnalyzer)
    }
}