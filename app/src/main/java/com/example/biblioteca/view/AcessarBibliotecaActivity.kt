package com.example.biblioteca.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager

class AcessarBibliotecaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acessar_biblioteca)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerViewLivros: RecyclerView = findViewById(R.id.recyclerViewLivros)
        recyclerViewLivros.layoutManager = LinearLayoutManager(this)

        val db = DatabaseManager(this)
        val livros = db.getAllLivros() // Método que retorna a lista de livros do banco de dados
        val adapter = LivroAdapter(livros) { livro ->
            // Se precisar de alguma ação ao clicar em um livro, adicionar aqui
        }
        recyclerViewLivros.adapter = adapter
    }
}