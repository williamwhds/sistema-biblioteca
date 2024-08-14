package com.example.biblioteca.model

import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario

class Emprestimo(
    var id: Int? = null,
    var dataEmprestimo: String,
    var dataDevolucao: String,
    usuario: Usuario,
    livro: Livro
) {
    var dataDevolucaoEfetiva: String = ""
    var devolvido: Boolean = false
    var usuario: Usuario? = usuario
    var livro: Livro? = livro
}
