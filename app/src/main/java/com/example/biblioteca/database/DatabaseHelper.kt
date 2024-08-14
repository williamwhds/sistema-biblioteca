package com.example.biblioteca.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableLivro = """
            CREATE TABLE livros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                autor TEXT,
                isbn TEXT,
                ano INTEGER,
                editora TEXT,
                edicao INTEGER,
                imagemCapa BLOB
            )
        """.trimIndent()
        db.execSQL(createTableLivro)

        val createTableUsuario = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT,
                telefone TEXT,
                endereco TEXT
            )
        """.trimIndent()
        db.execSQL(createTableUsuario)

        val createTableEmprestimo = """
            CREATE TABLE emprestimos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dataEmprestimo TEXT,
                dataDevolucao TEXT,
                dataDevolucaoEfetiva TEXT,
                devolvido INTEGER,
                usuarioId INTEGER,
                livroId INTEGER,
                FOREIGN KEY(usuarioId) REFERENCES usuarios(id),
                FOREIGN KEY(livroId) REFERENCES livros(id)
            )
        """.trimIndent()
        db.execSQL(createTableEmprestimo)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS livros")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS emprestimos")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 1
    }
}