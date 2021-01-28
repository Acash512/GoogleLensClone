package com.acash.googlelensclone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

const val CAMERA_PERMISSION_REQUEST_CODE = 4321

class CameraActivity : AppCompatActivity() {
    private lateinit var imageCapture: ImageCapture
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        checkCameraPermissions()

        btnCaptureImg.setOnClickListener{
            takePicture()
        }

        btnChangeLens.setOnClickListener{
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
    }

    private fun takePicture() {
        if(::imageCapture.isInitialized){
            val file = File(externalMediaDirs.first(),"IMG_${System.currentTimeMillis()}.jpg")
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(outputFileOptions,ContextCompat.getMainExecutor(this),object:ImageCapture.OnImageSavedCallback{

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity,"Image Saved at ${file.absolutePath}",Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity,"Error Saving Image!Try Again",Toast.LENGTH_SHORT).show()
                }

            })
        }else{
            Toast.makeText(this,"Camera Not Found",Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermissions() {
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
        else requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                imageCapture = ImageCapture.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
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
            if(permissions[0]==Manifest.permission.CAMERA && grantResults[0]==PackageManager.PERMISSION_GRANTED){
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