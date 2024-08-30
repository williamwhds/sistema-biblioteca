package com.example.biblioteca.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro

class RemoverLivroActivity : BaseBibliotecaActivity() {
    override fun onLivroClick(livro: Livro) {
        showRemoveDialog(livro)
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