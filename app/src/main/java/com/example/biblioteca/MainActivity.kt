package com.example.biblioteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biblioteca.view.AcessarBibliotecaActivity
import com.example.biblioteca.view.AcessarUsuarioActivity
import com.example.biblioteca.view.AddLivroActivity
import com.example.biblioteca.view.AddUsuarioActivity
import com.example.biblioteca.view.EditarLivroActivity
import com.example.biblioteca.view.EditarUsuarioActivity
import com.example.biblioteca.view.RemoverLivroActivity
import com.example.biblioteca.view.RemoverUsuarioActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonAdicionar: Button = findViewById(R.id.buttonAdicionar)
        val buttonRemover: Button = findViewById(R.id.buttonRemover)
        val buttonEditar: Button = findViewById(R.id.buttonEditar)
        val buttonAcessar: Button = findViewById(R.id.buttonAcessar)

        val buttonAdicionarUsuario: Button = findViewById(R.id.buttonAdicionarUsuario)
        val buttonRemoverUsuario: Button = findViewById(R.id.buttonRemoverUsuario)
        val buttonEditarUsuario: Button = findViewById(R.id.buttonEditarUsuario)
        val buttonAcessarUsuario: Button = findViewById(R.id.buttonAcessarUsuario)

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
            val intent = Intent(this, RemoverLivroActivity::class.java)
            startActivity(intent)
        }

        buttonEditar.setOnClickListener {
            val intent = Intent(this, EditarLivroActivity::class.java)
            startActivity(intent)
        }

        buttonAcessar.setOnClickListener {
            val intent = Intent(this, AcessarBibliotecaActivity::class.java)
            startActivity(intent)
        }

        buttonAdicionarUsuario.setOnClickListener {
            val intent = Intent(this, AddUsuarioActivity::class.java)
            startActivity(intent)
        }

        buttonRemoverUsuario.setOnClickListener {
            val intent = Intent(this, RemoverUsuarioActivity::class.java)
            startActivity(intent)
        }

        buttonEditarUsuario.setOnClickListener {
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            startActivity(intent)
        }

        buttonAcessarUsuario.setOnClickListener {
            val intent = Intent(this, AcessarUsuarioActivity::class.java)
            startActivity(intent)
        }

        buttonEmprestar.setOnClickListener {
            // Código mudar de tela
        }
    }
}