package com.example.biblioteca.view

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario

class RemoverUsuarioActivity : AppCompatActivity() {
    private var selectedUsuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remover_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = DatabaseManager(this)
        val usuarios = db.getAllUsuarios()

        val adapter = UsuarioAdapter(usuarios) { usuario ->
            selectedUsuario = usuario
            showRemoveDialog(usuario)
        }
        recyclerView.adapter = adapter
    }

    private fun showRemoveDialog(usuario: Usuario) {
        AlertDialog.Builder(this)
            .setTitle("Remover Usuário")
            .setMessage("Tem certeza que quer remover este usuário?")
            .setPositiveButton("Sim") { _, _ ->
                val db = DatabaseManager(this)
                db.removerUsuario(usuario)
                recreate()
            }
            .setNegativeButton("Não", null)
            .show()
    }
}