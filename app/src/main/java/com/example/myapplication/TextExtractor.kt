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

                if(extractedIdAndPassword.isEmpty()) {
                    Toast.makeText(currentContext, "감지된 와이파이 이름이 없습니다", Toast.LENGTH_SHORT).show()
                }
                else {
                    val firstPair = extractedIdAndPassword.firstOrNull()

                    if (firstPair != null) {
                        val (id, password) = firstPair

                        val connectionLogic: (String, String) -> Unit = { enteredId, enteredPassword ->
                            wifiConnector.connectToWifi(enteredId, enteredPassword)
                        }

                        val alertDialogBuilder = AlertDialogBuilder(currentContext)
                        alertDialogBuilder.showAlertDialog(id, password, connectionLogic)
                    }
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
            if (line.containID()) {
                if (idIsNextLine) {
                    currentId = line.trim()
                    idIsNextLine = false
                } else {
                    currentId = line.substringAfterID().substringAfter(":").trim()
                }

                if (line.uppercase().trim() == "ID") {
                    idIsNextLine = true
                }
            }

            if (line.containPassword()) {
                if (passwordIsNextLine) {
                    currentPassword = line.trim()
                    passwordIsNextLine = false
                } else {
                    currentPassword = line.substringAfterPassword().substringAfter(":").trim()
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

    private val idList = arrayOf(
        "ID",
        "id",
        "네트워크",
        "아이디",
        "Wifi",
        "WIFI",
        "wifi"
    )

    private val passwordList = arrayOf(
        "PASSWORD",
        "password",
        "PW",
        "pw",
        "패스워드",
        "비번",
        "Pw"
    )

    private fun String.containID(): Boolean {
        return idList.any { this.contains(it) }
    }

    private fun String.substringAfterID(): String {
        for (strId in idList) {
            val substring = this.substringAfter(strId)
            if (substring != this) {
                return substring
            }
        }
        return this
    }

    private fun String.containPassword(): Boolean {
        return passwordList.any { this.contains(it) }
    }

    private fun String.substringAfterPassword(): String {
        for (strId in passwordList) {
            val substring = this.substringAfter(strId)
            if (substring != this) {
                return substring
            }
        }
        return this
    }
}