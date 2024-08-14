package com.example.biblioteca

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddLivroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var buttonSalvar: Button = findViewById<Button>(R.id.buttonSalvar);
        var editTextTitulo: EditText = findViewById(R.id.editTextTitulo);
        var editTextAutor: EditText = findViewById(R.id.editTextAutor);
        var editTextISBN: EditText = findViewById(R.id.editTextISBN);
        var editTextAno: EditText = findViewById(R.id.editTextAno);
        var editTextEditora: EditText = findViewById(R.id.editTextEditora);

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_livro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}