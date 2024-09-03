package com.example.biblioteca.model

import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario

class Emprestimo(
    var id: Int? = null,
    var dataEmprestimo: String,
    var dataDevolucao: String,
    var usuario: Usuario,
    var livro: Livro
) {
    var dataDevolucaoEfetiva: String? = null
    var devolvido: Boolean = false
}
