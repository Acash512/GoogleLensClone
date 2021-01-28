package com.acash.googlelensclone

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_base_lens.*

abstract class BaseLensActivity : AppCompatActivity() {
    protected var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    abstract val imageAnalyzer:ImageAnalysis.Analyzer
    protected lateinit var imageAnalysis: ImageAnalysis

    abstract fun startScanner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_lens)

        checkCameraPermissions()

        btnStartScanner.setOnClickListener{
            startScanner()
        }

    }

    private fun checkCameraPermissions() {
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
        else requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    protected open fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
                {
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                            .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    imageAnalysis = ImageAnalysis.Builder()
                            .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageAnalysis)
                },
                ContextCompat.getMainExecutor(this)
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode== CAMERA_PERMISSION_REQUEST_CODE){
            if(permissions[0]== Manifest.permission.CAMERA && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                startCamera()
            }else{
                MaterialAlertDialogBuilder(this).apply {
                    setTitle("Permission Error")
                    setMessage("Cannot Proceed Without Camera Permissions")
                    setPositiveButton("OK"){_,_->
                        finish()
                    }
                    setCancelable(false)
                }.show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}