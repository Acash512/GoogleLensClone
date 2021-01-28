package com.acash.googlelensclone.barcode

import androidx.core.content.ContextCompat
import com.acash.googlelensclone.BaseLensActivity

class BarCodeActivity : BaseLensActivity() {

    override val imageAnalyzer = BarCodeAnalyzer(this)
    override fun startScanner() {
        scanBarcode()
    }

    private fun scanBarcode() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }

}
