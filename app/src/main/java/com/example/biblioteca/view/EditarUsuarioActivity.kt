package com.example.biblioteca.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Usuario
import com.example.biblioteca.view.adapter.UsuarioAdapter

class EditarUsuarioActivity : AppCompatActivity() {
    private var selectedUsuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_usuario)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerViewUsuarios: RecyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerViewUsuarios.layoutManager = LinearLayoutManager(this)

        val db = DatabaseManager(this)
        val usuarios = db.getAllUsuarios()
        val adapter = UsuarioAdapter(usuarios) { usuario ->
            showEditDialog(usuario)
        }
        recyclerViewUsuarios.adapter = adapter
    }

    private fun showEditDialog(usuario: Usuario) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_form_usuario, null)
        val editTextNome = dialogView.findViewById<EditText>(R.id.editTextNome)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextTelefone = dialogView.findViewById<EditText>(R.id.editTextTelefone)
        val editTextEndereco = dialogView.findViewById<EditText>(R.id.editTextEndereco)

        editTextNome.setText(usuario.nome)
        editTextEmail.setText(usuario.email)
        editTextTelefone.setText(usuario.telefone)
        editTextEndereco.setText(usuario.endereco)

        AlertDialog.Builder(this)
            .setTitle("Editar Usuário")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                usuario.nome = editTextNome.text.toString()
                usuario.email = editTextEmail.text.toString()
                usuario.telefone = editTextTelefone.text.toString()
                usuario.endereco = editTextEndereco.text.toString()

                val db = DatabaseManager(this)
                db.editarUsuario(usuario)
                recreate() // Recreate activity to refresh the list
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}