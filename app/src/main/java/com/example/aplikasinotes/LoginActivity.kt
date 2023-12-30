package com.example.aplikasinotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        //button
        val btnMasuk: Button = findViewById(R.id.buttonLogin)
        val btnDaftar: TextView = findViewById(R.id.textViewDaftar)

        //instance text
        val txtEmailEdit: TextInputEditText  = findViewById(R.id.inputEmailEdit)
        val txtPasswordEdit: TextInputEditText = findViewById(R.id.inputPasswordEdit)

        //event button Masuk/login
        btnMasuk.setOnClickListener {

            //instance
            val dbHelper = NotesDatabaseHelper(this)

            //Access the underlying EditText from TextInputLayout
            val email = txtEmailEdit.text.toString().trim()
            val password = txtPasswordEdit.text.toString().trim()

            //check login
            val result: Boolean = dbHelper.checkLoginUser(email, password)
            if (result) {

                val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentLogin)
            } else {
                Toast.makeText(this, "Login Failed. Try Again", Toast.LENGTH_SHORT).show()
                txtEmailEdit.hint = "email"
                txtPasswordEdit.hint = "password"
            }
        }

        //event "Daftar"
        btnDaftar.setOnClickListener {
            val intentRegisterPasienActivity = Intent(this, RegisterActivity::class.java)
            startActivity(intentRegisterPasienActivity)
        }
    }
}