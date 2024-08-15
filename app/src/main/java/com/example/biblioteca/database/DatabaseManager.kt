package com.example.biblioteca.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.biblioteca.model.Emprestimo
import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario
import java.io.ByteArrayOutputStream

class DatabaseManager(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun salvarLivro(livro: Livro) {
        val values = ContentValues().apply {
            put("titulo", livro.titulo)
            put("autor", livro.autor)
            put("isbn", livro.isbn)
            put("ano", livro.ano)
            put("editora", livro.editora)
            put("edicao", livro.edicao)
            put("imagemCapa", livro.imagemCapa)
        }
        db.insert("livros", null, values)
    }

    fun salvarUsuario(usuario: Usuario) {
        val values = ContentValues().apply {
            put("nome", usuario.nome)
            put("email", usuario.email)
            put("telefone", usuario.telefone)
            put("endereco", usuario.endereco)
        }
        db.insert("usuarios", null, values)
    }

    fun salvarEmprestimo(emprestimo: Emprestimo) {
        val values = ContentValues().apply {
            put("dataEmprestimo", emprestimo.dataEmprestimo)
            put("dataDevolucao", emprestimo.dataDevolucao)
            put("dataDevolucaoEfetiva", emprestimo.dataDevolucaoEfetiva)
            put("devolvido", if (emprestimo.devolvido) 1 else 0)
            put("usuarioId", emprestimo.usuario?.id)
            put("livroId", emprestimo.livro?.id)
        }
        db.insert("emprestimos", null, values)
    }

    fun recuperarImagem(id: Int): Bitmap? {
        val cursor = db.query("livros", arrayOf("imagemCapa"), "id = ?", arrayOf(id.toString()), null, null, null)
        cursor?.moveToFirst()
        val imageBytes = cursor?.getBlob(0)
        cursor?.close()
        return if (imageBytes != null) {
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } else {
            null
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun ultimoIdLivro(): Int {
        val cursor = db.rawQuery("SELECT MAX(id) FROM livros", null)
        cursor.moveToFirst()
        val id = cursor.getInt(0)
        cursor.close()
        return id
    }
}