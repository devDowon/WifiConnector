package com.example.myapplication

import android.content.ContentValues
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var pictureUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionManager = PermissionManager(this@MainActivity)
        permissionManager.requestMultiplePermission()
        setContent {
            MyApplicationTheme {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MakeButton()
                }
            }
        }
    }

    //카메라
    fun openCamera() {
        pictureUri = createImageFile()
        getTakePicture.launch(pictureUri)
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss", Locale.KOREA).format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }

    private val getTakePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val source = ImageDecoder.createSource(
                    applicationContext.contentResolver,
                    pictureUri!!
                )
                val bitmap = ImageDecoder.decodeBitmap(source)
                val textExtractor = TextExtractor(this@MainActivity)
                textExtractor.performTextRecognition(bitmap)
            }
        }

    //버튼 생성
    @Composable
    fun MakeButton() {
        Button(onClick = {
            openCamera()
        }) {
            Text(text = "카메라 열기")
        }
    }
}