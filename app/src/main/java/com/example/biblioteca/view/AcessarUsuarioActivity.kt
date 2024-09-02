package com.example.biblioteca.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.view.adapter.UsuarioAdapter

class AcessarUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acessar_usuario)
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
            // Se precisar de alguma ação ao clicar em um usuário, adicionar aqui
        }
        recyclerView.adapter = adapter
    }
}