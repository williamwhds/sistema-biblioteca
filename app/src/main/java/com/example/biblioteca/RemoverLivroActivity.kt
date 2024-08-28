package com.example.biblioteca

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro

class RemoverLivroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remover_livro)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerViewLivros: RecyclerView = findViewById(R.id.recyclerViewLivros)
        recyclerViewLivros.layoutManager = LinearLayoutManager(this)

        val db = DatabaseManager(this)
        val livros = db.getAllLivros()
        val adapter = LivroAdapter(livros) { livro ->
            showRemoveDialog(livro)
        }
        recyclerViewLivros.adapter = adapter
    }

    private fun showRemoveDialog(livro: Livro) {
        AlertDialog.Builder(this)
            .setTitle("Remover Livro")
            .setMessage("Tem certeza que quer remover este livro?")
            .setPositiveButton("Sim") { _, _ ->
                val db = DatabaseManager(this)
                db.removerLivro(livro)
                recreate()
            }
            .setNegativeButton("Não", null)
            .show()
    }
}