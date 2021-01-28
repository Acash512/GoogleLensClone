package com.acash.googlelensclone.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarCodeAnalyzer(private val context: Context) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("BARCODE", "Image Analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        Log.d(
                            "BARCODE", """
                            FORMAT = ${barcode.format}
                            VALUE = ${barcode.rawValue}
                        """.trimIndent()
                        )

                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(barcode.rawValue))
                        if (i.resolveActivity(context.packageManager) != null)
                            context.startActivity(i)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.e("BARCODE", "Error scanning Barcode", exception)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } ?: imageProxy.close()

    }
}