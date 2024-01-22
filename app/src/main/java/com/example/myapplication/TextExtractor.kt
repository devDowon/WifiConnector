package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextExtractor(private val currentContext: Context) {

    fun performTextRecognition(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                val extractedIdAndPassword = extractIdAndPassword(resultText)

                val wifiConnector = WifiConnector(currentContext)
                for ((id, password) in extractedIdAndPassword) {
                    wifiConnector.connectToWifi(id!!, password!!)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(currentContext, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun extractIdAndPassword(data: String): List<Pair<String?, String?>> {
        val lines = data.lines()
        val idAndPasswordList = mutableListOf<Pair<String?, String?>>()
        var currentId: String? = null
        var currentPassword: String? = null

        var idIsNextLine = false
        var passwordIsNextLine = false
        for (line in lines) {
            if (line.uppercase().contains("ID")) {
                if (idIsNextLine) {
                    currentId = line.trim()
                    idIsNextLine = false
                } else {
                    currentId = line.substringAfter("ID").substringAfter(":").trim()
                }

                if (line.uppercase().trim() == "ID") {
                    idIsNextLine = true
                }
            }

            if (line.uppercase().contains("PASSWORD")) {
                if (passwordIsNextLine) {
                    currentPassword = line.trim()
                    passwordIsNextLine = false
                } else {
                    currentPassword = line.substringAfter("PASSWORD").substringAfter(":").trim()
                }

                if (line.uppercase().trim() == "PASSWORD") {
                    passwordIsNextLine = true
                }
            }

            if (currentId != null && currentPassword != null) {
                idAndPasswordList.add(Pair(currentId, currentPassword))
                currentId = null
                currentPassword = null
            }
        }

        return idAndPasswordList
    }
}