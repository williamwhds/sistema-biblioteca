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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// A FAZER:
// Comprimir a imagem ao salvar. Acho que quando uma imagem grande é inserida,
// a aplicação retorna um erro e não abre mais até que os dados sejam limpos.
//
// Quando um livro ou usuário é removido, os empréstimos relacionados a eles
// também devem ser removidos.
//
// O atributo livro.emprestado não é necessário, pois a informação de empréstimo
// está no banco de dados. Remover esse atributo e o atributo livro.emprestimo.
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
            put("usuarioId", emprestimo.usuario.id)
            put("livroId", emprestimo.livro.id)
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

    fun getAllEmprestimos(): List<Emprestimo> {
        val emprestimos = mutableListOf<Emprestimo>()
        val cursor = db.query("emprestimos", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val dataEmprestimo = cursor.getString(cursor.getColumnIndex("dataEmprestimo"))
            val dataDevolucao = cursor.getString(cursor.getColumnIndex("dataDevolucao"))
            val dataDevolucaoEfetiva = cursor.getString(cursor.getColumnIndex("dataDevolucaoEfetiva"))
            val devolvido = cursor.getInt(cursor.getColumnIndex("devolvido")) == 1
            val usuarioId = cursor.getInt(cursor.getColumnIndex("usuarioId"))
            val livroId = cursor.getInt(cursor.getColumnIndex("livroId"))
            val emprestimo = Emprestimo(id, dataEmprestimo, dataDevolucao, getUsuario(usuarioId), getLivro(livroId))
            emprestimo.dataDevolucaoEfetiva = dataDevolucaoEfetiva
            emprestimo.devolvido = devolvido
            emprestimos.add(emprestimo)
        }
        cursor.close()
        return emprestimos
    }

    fun getEmprestimo(idEmprestimo: Int): Emprestimo {
        val cursor = db.query("emprestimos", null, "id = ?", arrayOf(idEmprestimo.toString()), null, null, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndex("id"))
        val dataEmprestimo = cursor.getString(cursor.getColumnIndex("dataEmprestimo"))
        val dataDevolucao = cursor.getString(cursor.getColumnIndex("dataDevolucao"))
        val dataDevolucaoEfetiva = cursor.getString(cursor.getColumnIndex("dataDevolucaoEfetiva"))
        val devolvido = cursor.getInt(cursor.getColumnIndex("devolvido")) == 1
        val usuarioId = cursor.getInt(cursor.getColumnIndex("usuarioId"))
        val livroId = cursor.getInt(cursor.getColumnIndex("livroId"))
        val emprestimo = Emprestimo(id, dataEmprestimo, dataDevolucao, getUsuario(usuarioId), getLivro(livroId))
        emprestimo.dataDevolucaoEfetiva = dataDevolucaoEfetiva
        emprestimo.devolvido = devolvido
        cursor.close()
        return emprestimo
    }

    fun devolverEmprestimo(idEmprestimo: Int) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val values = ContentValues().apply {
            put("devolvido", 1)
            put("dataDevolucaoEfetiva", currentDate)
        }
        db.update("emprestimos", values, "id = ?", arrayOf(idEmprestimo.toString()))
    }

    fun removerEmprestimo(idEmprestimo: Int) {
        db.delete("emprestimos", "id = ?", arrayOf(idEmprestimo.toString()))
    }

    fun editarEmprestimo(emprestimo: Emprestimo) {
        val values = ContentValues().apply {
            put("dataEmprestimo", emprestimo.dataEmprestimo)
            put("dataDevolucao", emprestimo.dataDevolucao)
            put("dataDevolucaoEfetiva", emprestimo.dataDevolucaoEfetiva)
            put("devolvido", if (emprestimo.devolvido) 1 else 0)
            put("usuarioId", emprestimo.usuario.id)
            put("livroId", emprestimo.livro.id)
        }
        db.update("emprestimos", values, "id = ?", arrayOf(emprestimo.id.toString()))
    }

    fun getLivrosDisponiveis() : List<Livro> {
        val livros = mutableListOf<Livro>()
        val cursor = db.rawQuery("SELECT * FROM livros WHERE id NOT IN (SELECT livroId FROM emprestimos WHERE devolvido = 0)", null)
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

    fun getLivroImagem(livroid: Int): Bitmap? {
        val cursor = db.query("livros", arrayOf("imagemCapa"), "id = ?", arrayOf(livroid.toString()), null, null, null)
        cursor?.moveToFirst()
        val imageBytes = cursor?.getBlob(0)
        cursor?.close()
        return if (imageBytes != null) {
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } else {
            null
        }
    }

    fun getEmprestimoByLivroId(livroId: Int): Emprestimo? {
        val cursor = db.query("emprestimos", null, "livroId = ? AND devolvido = 0", arrayOf(livroId.toString()), null, null, null)
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val dataEmprestimo = cursor.getString(cursor.getColumnIndex("dataEmprestimo"))
            val dataDevolucao = cursor.getString(cursor.getColumnIndex("dataDevolucao"))
            val dataDevolucaoEfetiva = cursor.getString(cursor.getColumnIndex("dataDevolucaoEfetiva"))
            val devolvido = cursor.getInt(cursor.getColumnIndex("devolvido")) == 1
            val usuarioId = cursor.getInt(cursor.getColumnIndex("usuarioId"))
            val emprestimo = Emprestimo(id, dataEmprestimo, dataDevolucao, getUsuario(usuarioId), getLivro(livroId))
            emprestimo.dataDevolucaoEfetiva = dataDevolucaoEfetiva
            emprestimo.devolvido = devolvido
            cursor.close()
            emprestimo
        } else {
            cursor.close()
            null
        }
    }
}
