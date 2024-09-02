// app/src/main/java/com/example/biblioteca/database/DatabaseManager.kt
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

// A FAZER:
// Comprimir a imagem ao salvar. Acho que quando uma imagem grande é inserida,
// a aplicação retorna um erro e não abre mais até que os dados sejam limpos.
//
//

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

    fun removerUsuario(usuario: Usuario) {
        db.delete("usuarios", "id = ?", arrayOf(usuario.id.toString()))
    }

    fun editarUsuario(usuario: Usuario) {
        val values = ContentValues().apply {
            put("nome", usuario.nome)
            put("email", usuario.email)
            put("telefone", usuario.telefone)
            put("endereco", usuario.endereco)
        }
        db.update("usuarios", values, "id = ?", arrayOf(usuario.id.toString()))
    }

    fun getAllUsuarios(): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val cursor = db.query("usuarios", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val nome = cursor.getString(cursor.getColumnIndex("nome"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val telefone = cursor.getString(cursor.getColumnIndex("telefone"))
            val endereco = cursor.getString(cursor.getColumnIndex("endereco"))
            val usuario = Usuario(id, nome, email, telefone, endereco)
            usuarios.add(usuario)
        }
        cursor.close()
        return usuarios
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

    fun getAllLivros(): List<Livro> {
        val livros = mutableListOf<Livro>()
        val cursor = db.query("livros", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val autor = cursor.getString(cursor.getColumnIndex("autor"))
            val isbn = cursor.getString(cursor.getColumnIndex("isbn"))
            val ano = cursor.getInt(cursor.getColumnIndex("ano"))
            val editora = cursor.getString(cursor.getColumnIndex("editora"))
            val edicao = cursor.getInt(cursor.getColumnIndex("edicao"))
            val imagemCapa = cursor.getBlob(cursor.getColumnIndex("imagemCapa"))
            val livro = Livro(id, titulo, autor, isbn, ano, editora, edicao)
            livro.imagemCapa = imagemCapa
            livros.add(livro)
        }
        cursor.close()
        return livros
    }

    fun removerLivro(livro: Livro) {
        db.delete("livros", "id = ?", arrayOf(livro.id.toString()))
    }

    fun editarLivro(livro: Livro) {
        val values = ContentValues().apply {
            put("titulo", livro.titulo)
            put("autor", livro.autor)
            put("isbn", livro.isbn)
            put("ano", livro.ano)
            put("editora", livro.editora)
            put("edicao", livro.edicao)
            put("imagemCapa", livro.imagemCapa)
        }
        db.update("livros", values, "id = ?", arrayOf(livro.id.toString()))
    }

    fun getLivro(id: Int) : Livro {
        val cursor = db.query("livros", null, "id = ?", arrayOf(id.toString()), null, null, null)
        cursor.moveToFirst()
        val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
        val autor = cursor.getString(cursor.getColumnIndex("autor"))
        val isbn = cursor.getString(cursor.getColumnIndex("isbn"))
        val ano = cursor.getInt(cursor.getColumnIndex("ano"))
        val editora = cursor.getString(cursor.getColumnIndex("editora"))
        val edicao = cursor.getInt(cursor.getColumnIndex("edicao"))
        val imagemCapa = cursor.getBlob(cursor.getColumnIndex("imagemCapa"))
        val livro = Livro(id, titulo, autor, isbn, ano, editora, edicao)
        livro.imagemCapa = imagemCapa
        cursor.close()
        return livro
    }

    fun getUsuario(usuarioId: Int): Usuario {
        val cursor = db.query("usuarios", null, "id = ?", arrayOf(usuarioId.toString()), null, null, null)
        cursor.moveToFirst()
        val nome = cursor.getString(cursor.getColumnIndex("nome"))
        val email = cursor.getString(cursor.getColumnIndex("email"))
        val telefone = cursor.getString(cursor.getColumnIndex("telefone"))
        val endereco = cursor.getString(cursor.getColumnIndex("endereco"))
        val usuario = Usuario(usuarioId, nome, email, telefone, endereco)
        cursor.close()
        return usuario
    }
}
