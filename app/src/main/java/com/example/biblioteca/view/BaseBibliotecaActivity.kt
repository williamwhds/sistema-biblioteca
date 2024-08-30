// File: app/src/main/java/com/example/biblioteca/view/BaseBibliotecaActivity.kt
package com.example.biblioteca.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro

abstract class BaseBibliotecaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_biblioteca)

        val db = DatabaseManager(this)
        val livros = db.getAllLivros()
        val adapter = LivroAdapter(livros) { livro ->
            onLivroClick(livro)
        }

        val recyclerViewLivros: RecyclerView = findViewById(R.id.recyclerViewLivros)
        val spanCount = calculateSpanCount()
        val padding = calculatePadding(spanCount)
        recyclerViewLivros.layoutManager = GridLayoutManager(this, spanCount)
        recyclerViewLivros.adapter = adapter

        if (livros.isEmpty()) {
            Toast.makeText(this, "Nenhum livro cadastrado", Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun onLivroClick(livro: Livro)

    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val itemWidthDp = 150
        return (screenWidthDp / itemWidthDp).toInt().coerceAtLeast(2)
    }

    private fun calculatePadding(spanCount: Int): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val totalPaddingPx = screenWidthPx / 20
        return totalPaddingPx / (spanCount + 1)
    }
}