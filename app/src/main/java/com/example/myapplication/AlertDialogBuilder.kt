package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class AlertDialogBuilder(private val currentContext : Context) {
    fun showAlertDialog(id : String?, password : String?, onConnect: (String, String) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(currentContext)
        alertDialogBuilder.setTitle("Connect to Wi-Fi")

        val layout = LinearLayout(currentContext)
        layout.orientation = LinearLayout.VERTICAL


        val idLayout = LinearLayout(currentContext)
        idLayout.orientation = LinearLayout.HORIZONTAL

        val idLabel = TextView(currentContext)
        idLabel.text = "ID:"
        idLabel.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        idLabel.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        idLayout.addView(idLabel)

        val idEditText = EditText(currentContext)
        idEditText.hint = "Enter ID"
        idEditText.setText(id)
        idEditText.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        idLayout.addView(idEditText)


        val passwordLayout = LinearLayout(currentContext)
        passwordLayout.orientation = LinearLayout.HORIZONTAL

        val passwordLabel = TextView(currentContext)
        passwordLabel.text = "Password:"
        passwordLabel.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        passwordLabel.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        passwordLayout.addView(passwordLabel)

        val passwordEditText = EditText(currentContext)
        passwordEditText.hint = "Enter Password"
        passwordEditText.setText(password)
        passwordEditText.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        passwordLayout.addView(passwordEditText)


        layout.addView(idLayout)
        layout.addView(passwordLayout)

        alertDialogBuilder.setView(layout)


        alertDialogBuilder.setPositiveButton("Connect") { dialog, which ->
            // Retrieve values from EditText fields
            val enteredId = idEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            // Call connectToWifi with the entered ID and password
            onConnect(enteredId, enteredPassword)
        }

        // Show the AlertDialog
        alertDialogBuilder.show()
    }
}