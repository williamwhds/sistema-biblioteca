package com.example.biblioteca.model

class Livro(
    var id: Int? = null,
    var titulo: String,
    var autor: String,
    var isbn: String,
    var ano: Int,
    var editora: String,
    var edicao: Int
) {
    var emprestado: Boolean = false
    var emprestimo: Emprestimo? = null
    var imagemCapa: ByteArray? = null
}