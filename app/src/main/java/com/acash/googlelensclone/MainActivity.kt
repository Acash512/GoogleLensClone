package com.acash.googlelensclone

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.acash.googlelensclone.barcode.BarCodeActivity
import com.acash.googlelensclone.facedetection.FaceDetectionActivity
import com.acash.googlelensclone.imagelabeling.ImageLabelingActivity
import com.acash.googlelensclone.textrecognition.TextRecognitionActivity
import kotlinx.android.synthetic.main.activity_main.*

const val OPEN_CAMERA_REQUEST_CODE = 1234
const val PHOTO_EXTRAS = "data"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTakePhotoExt.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE)
        }

        btnCameraActivity.setOnClickListener{
            startActivity(Intent(this,CameraActivity::class.java))
        }

        btnBarcodeActivity.setOnClickListener{
            startActivity(Intent(this,BarCodeActivity::class.java))
        }

        btnFaceDetectionActivity.setOnClickListener{
            startActivity(Intent(this,FaceDetectionActivity::class.java))
        }

        btnImageLabelingActivity.setOnClickListener{
            startActivity(Intent(this,ImageLabelingActivity::class.java))
        }

        btnTextRecognitionActivity.setOnClickListener{
            startActivity(Intent(this,TextRecognitionActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode== OPEN_CAMERA_REQUEST_CODE){
            val bitmap = data?.extras?.get(PHOTO_EXTRAS) as Bitmap
            capturedImg.setImageBitmap(bitmap)
            capturedImg.visibility = View.VISIBLE
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}