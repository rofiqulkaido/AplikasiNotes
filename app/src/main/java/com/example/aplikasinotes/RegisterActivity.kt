package com.example.aplikasinotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Input form
        val txtFullname: TextInputLayout = findViewById(R.id.nameInput)
        val txtEmail: TextInputLayout = findViewById(R.id.emailInput)
        val txtPassword: TextInputLayout = findViewById(R.id.passwordInput)

        // Instance button daftar
        val btnRegister: Button = findViewById(R.id.buttonDaftar)

        //aksi btnReg
        btnRegister.setOnClickListener {
            // Object class DatabaseHelper
            val databaseHelper = NotesDatabaseHelper(this)

            // Declare data
            val name: String = txtFullname.editText?.text.toString().trim()
            val email: String = txtEmail.editText?.text.toString().trim()
            val password: String = txtPassword.editText?.text.toString().trim()

            // Check data -> email sudah terdaftar atau belum
            val data: String = databaseHelper.checkDataUser(email)
            // Jika belum terdaftar
            if (data == "") {
                // Insert data
                databaseHelper.addAccountUser(name, email, password)

                // Show LoginActivity
                val intentLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentLogin)
            } else {

                // Jika email telah terdaftar
                Toast.makeText(
                    this@RegisterActivity,
                    "Register failed. Your email already registered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}