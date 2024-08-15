package com.example.biblioteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonAdicionar: Button = findViewById(R.id.buttonAdicionar)
        val buttonRemover: Button = findViewById(R.id.buttonRemover)
        val buttonEditar: Button = findViewById(R.id.buttonEditar)
        val buttonAcessar: Button = findViewById(R.id.buttonAcessar)
        val buttonEmprestar: Button = findViewById(R.id.buttonEmprestar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonAdicionar.setOnClickListener {
            val intent = Intent(this, AddLivroActivity::class.java)
            startActivity(intent)
        }

        buttonRemover.setOnClickListener {
            // Código mudar de tela
        }

        buttonEditar.setOnClickListener {
            // Código mudar de tela
        }

        buttonAcessar.setOnClickListener {
            // Código mudar de tela
        }

        buttonEmprestar.setOnClickListener {
            // Código mudar de tela
        }
    }
}