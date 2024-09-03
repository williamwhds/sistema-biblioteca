package com.example.biblioteca.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Emprestimo
import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetalhesEmprestimoActivity : AppCompatActivity() {

    private lateinit var imageViewLivro: ImageView
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewAutor: TextView
    private lateinit var textViewNomeUsuario: TextView
    private lateinit var textViewDataEmprestimo: TextView
    private lateinit var textViewDataDevolucao: TextView
    private lateinit var buttonDevolver: Button
    private lateinit var buttonCancelarEmprestimo: Button
    private lateinit var buttonEditarEmprestimo: Button

    private lateinit var db: DatabaseManager
    private var emprestimoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_emprestimo)

        imageViewLivro = findViewById(R.id.imageViewLivro)
        textViewTitulo = findViewById(R.id.textViewTitulo)
        textViewAutor = findViewById(R.id.textViewAutor)
        textViewNomeUsuario = findViewById(R.id.textViewNomeUsuario)
        textViewDataEmprestimo = findViewById(R.id.textViewDataEmprestimo)
        textViewDataDevolucao = findViewById(R.id.textViewDataDevolucao)
        buttonDevolver = findViewById(R.id.buttonDevolver)
        buttonCancelarEmprestimo = findViewById(R.id.buttonCancelarEmprestimo)
        buttonEditarEmprestimo = findViewById(R.id.buttonEditarEmprestimo)

        db = DatabaseManager(this)
        emprestimoId = intent.getIntExtra("EMPRESTIMO_ID", 0)

        loadEmprestimoDetails()

        buttonDevolver.setOnClickListener {
            devolverEmprestimo()
        }

        buttonCancelarEmprestimo.setOnClickListener {
            cancelarEmprestimo()
        }

        buttonEditarEmprestimo.setOnClickListener {
            showEditEmprestimoDialog()
        }
    }

    private fun loadEmprestimoDetails() {
        val emprestimo: Emprestimo? = db.getEmprestimo(emprestimoId)
        if (emprestimo != null) {
            textViewTitulo.text = emprestimo.livro.titulo
            textViewAutor.text = emprestimo.livro.autor
            textViewNomeUsuario.text = emprestimo.usuario.nome
            textViewDataEmprestimo.text = "Data de Empréstimo: ${emprestimo.dataEmprestimo}"
            textViewDataDevolucao.text = "Data de Devolução: ${emprestimo.dataDevolucao}"

            val imagemLivro: Bitmap? = emprestimo.livro.id?.let { db.getLivroImagem(it) }
            if (imagemLivro != null) {
                imageViewLivro.setImageBitmap(imagemLivro)
            } else {
                imageViewLivro.setImageResource(R.drawable.ic_placeholder)
            }
        }
    }

    private fun devolverEmprestimo() {
        db.devolverEmprestimo(emprestimoId)
        val resultIntent = Intent().putExtra("EMPRESTIMO_DEVOLVIDO", true)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun cancelarEmprestimo() {
        db.removerEmprestimo(emprestimoId)
        val resultIntent = Intent().putExtra("EMPRESTIMO_CANCELADO", true)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    // DetalhesEmprestimoActivity.kt
    private fun showEditEmprestimoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_form_emprestimos, null)
        val spinnerUsuario = dialogView.findViewById<Spinner>(R.id.spinnerUsuario)
        val spinnerLivro = dialogView.findViewById<Spinner>(R.id.spinnerLivro)
        val textViewDataEmprestimo = dialogView.findViewById<TextView>(R.id.textViewDataEmprestimo)
        val textViewDataDevolucao = dialogView.findViewById<TextView>(R.id.textViewDataDevolucao)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Preencher os campos com os detalhes do empréstimo atual
        val emprestimo = db.getEmprestimo(emprestimoId)
        textViewDataEmprestimo.text = emprestimo.dataEmprestimo
        textViewDataDevolucao.text = emprestimo.dataDevolucao

        val usuarios = db.getAllUsuarios()
        val livros = db.getLivrosDisponiveis()

        val usuarioAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarios)
        usuarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUsuario.adapter = usuarioAdapter
        spinnerUsuario.setSelection(usuarios.indexOf(emprestimo.usuario))

        val livroAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, livros)
        livroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLivro.adapter = livroAdapter
        spinnerLivro.setSelection(livros.indexOf(emprestimo.livro))

        textViewDataEmprestimo.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textViewDataEmprestimo.text = sdf.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        textViewDataDevolucao.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textViewDataDevolucao.text = sdf.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Empréstimo")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                val usuario = spinnerUsuario.selectedItem as? Usuario
                var livro = spinnerLivro.selectedItem as? Livro
                val dataEmprestimo = textViewDataEmprestimo.text.toString()
                val dataDevolucao = textViewDataDevolucao.text.toString()

                if (usuario != null) {
                    if (livro == null) {
                        livro = emprestimo.livro
                    }
                    val updatedEmprestimo = Emprestimo(
                        id = emprestimoId,
                        dataEmprestimo = dataEmprestimo,
                        dataDevolucao = dataDevolucao,
                        usuario = usuario,
                        livro = livro
                    )
                    db.editarEmprestimo(updatedEmprestimo)
                    loadEmprestimoDetails()
                    Toast.makeText(this, "Empréstimo atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent().putExtra("EMPRESTIMO_EDITADO", true)
                    setResult(RESULT_OK, resultIntent)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}