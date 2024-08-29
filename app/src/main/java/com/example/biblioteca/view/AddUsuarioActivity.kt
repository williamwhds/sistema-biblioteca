package com.example.biblioteca.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.biblioteca.R
import com.example.biblioteca.model.Usuario
import com.example.biblioteca.database.DatabaseManager

class AddUsuarioActivity : AppCompatActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_usuario)

        databaseManager = DatabaseManager(this)

        val editTextNome: EditText = findViewById(R.id.editTextNome)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextTelefone: EditText = findViewById(R.id.editTextTelefone)
        val editTextEndereco: EditText = findViewById(R.id.editTextEndereco)
        val buttonAddUsuario: Button = findViewById(R.id.buttonAddUsuario)

        buttonAddUsuario.setOnClickListener {
            val nome = editTextNome.text.toString()
            val email = editTextEmail.text.toString()
            val telefone = editTextTelefone.text.toString()
            val endereco = editTextEndereco.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty() && telefone.isNotEmpty() && endereco.isNotEmpty()) {
                val usuario = Usuario(null, nome, email, telefone, endereco)
                databaseManager.salvarUsuario(usuario)
                Toast.makeText(this, "Usuário adicionado com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}