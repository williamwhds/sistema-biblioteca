package com.example.biblioteca.control

import com.example.biblioteca.model.Emprestimo
import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario

class Biblioteca {
    val livros: MutableList<Livro> = mutableListOf()
    val usuarios: MutableList<Usuario> = mutableListOf()
    val emprestimos: MutableList<Emprestimo> = mutableListOf()

    fun adicionarLivro(livro: Livro) {
        livros.add(livro)
    }

    fun adicionarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun adicionarEmprestimo(emprestimo: Emprestimo) {
        emprestimos.add(emprestimo)
    }

    fun removerLivro(livro: Livro) {
        livros.remove(livro)
    }

    fun removerUsuario(usuario: Usuario) {
        usuarios.remove(usuario)
    }

    fun removerEmprestimo(emprestimo: Emprestimo) {
        emprestimos.remove(emprestimo)
    }

    fun buscarLivroPorTitulo(titulo: String): Livro? {
        return livros.find { it.titulo == titulo }
    }

    fun buscarLivroPorAutor(autor: String): Livro? {
        return livros.find { it.autor == autor }
    }

    fun buscarLivroPorISBN(isbn: String): Livro? {
        return livros.find { it.isbn == isbn }
    }

    fun buscarLivroPorAno(ano: Int): Livro? {
        return livros.find { it.ano == ano }
    }

    fun buscarLivroPorEditora(editora: String): Livro? {
        return livros.find { it.editora == editora }
    }

    fun buscarLivroPorEdicao(edicao: Int): Livro? {
        return livros.find { it.edicao == edicao }
    }

    fun buscarUsuarioPorNome(nome: String): Usuario? {
        return usuarios.find { it.nome == nome }
    }

    fun buscarUsuarioPorEmail(email: String): Usuario? {
        return usuarios.find { it.email == email }
    }

    fun buscarUsuarioPorTelefone(telefone: String): Usuario? {
        return usuarios.find { it.telefone == telefone }
    }

    fun buscarUsuarioPorEndereco(endereco: String): Usuario? {
        return usuarios.find { it.endereco == endereco }
    }

    fun buscarEmprestimoPorUsuario(usuario: Usuario): Emprestimo? {
        return emprestimos.find { it.usuario == usuario }
    }

    fun buscarEmprestimoPorLivro(livro: Livro): Emprestimo? {
        return emprestimos.find { it.livro == livro }
    }

    fun buscarEmprestimoPorDataEmprestimo(dataEmprestimo: String): Emprestimo? {
        return emprestimos.find { it.dataEmprestimo == dataEmprestimo }
    }

    fun buscarEmprestimoPorDataDevolucao(dataDevolucao: String): Emprestimo? {
        return emprestimos.find { it.dataDevolucao == dataDevolucao }
    }
}