// EmprestimosFragment.kt
package com.example.biblioteca.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Emprestimo
import com.example.biblioteca.model.Livro
import com.example.biblioteca.model.Usuario
import com.example.biblioteca.view.DetalhesEmprestimoActivity
import com.example.biblioteca.view.adapter.EmprestimoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class EmprestimosFragment : Fragment() {
    private lateinit var recyclerViewEmprestimos: RecyclerView
    private lateinit var fabAddEmprestimo: FloatingActionButton
    private lateinit var emprestimoAdapter: EmprestimoAdapter
    private lateinit var spinnerUsuario: Spinner
    private lateinit var spinnerLivro: Spinner
    private lateinit var textViewDataEmprestimo: TextView
    private lateinit var textViewDataDevolucao: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_emprestimos, container, false)

        recyclerViewEmprestimos = view.findViewById(R.id.recyclerViewEmprestimos)
        fabAddEmprestimo = view.findViewById(R.id.fabAddEmprestimo)

        recyclerViewEmprestimos.layoutManager = LinearLayoutManager(context)
        val db = DatabaseManager(requireContext())
        emprestimoAdapter = EmprestimoAdapter(listOf(), { emprestimo ->
            val intent = Intent(context, DetalhesEmprestimoActivity::class.java)
            intent.putExtra("EMPRESTIMO_ID", emprestimo.id)
            startActivityForResult(intent, 1)
        }, db)
        recyclerViewEmprestimos.adapter = emprestimoAdapter

        loadEmprestimos()

        fabAddEmprestimo.setOnClickListener {
            showAddEmprestimoDialog()
        }

        return view
    }

    private fun loadEmprestimos() {
        val db = DatabaseManager(requireContext())
        val emprestimos = db.getAllEmprestimos()
        if (emprestimos.isEmpty()) {
            Toast.makeText(context, "Nenhum empréstimo encontrado", Toast.LENGTH_SHORT).show()
        } else {
            emprestimoAdapter.updateEmprestimos(emprestimos)
        }
    }

    private fun showAddEmprestimoDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_form_emprestimos, null)
        spinnerUsuario = dialogView.findViewById(R.id.spinnerUsuario)
        spinnerLivro = dialogView.findViewById(R.id.spinnerLivro)
        textViewDataEmprestimo = dialogView.findViewById(R.id.textViewDataEmprestimo)
        textViewDataDevolucao = dialogView.findViewById(R.id.textViewDataDevolucao)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        textViewDataEmprestimo.text = sdf.format(Date())

        val db = DatabaseManager(requireContext())
        val usuarios = db.getAllUsuarios()
        val livros = db.getLivrosDisponiveis()

        val usuarioAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, usuarios)
        usuarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUsuario.adapter = usuarioAdapter

        val livroAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, livros)
        livroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLivro.adapter = livroAdapter

        textViewDataEmprestimo.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textViewDataEmprestimo.text = sdf.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        textViewDataDevolucao.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textViewDataDevolucao.text = sdf.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Adicionar Empréstimo")
            .setPositiveButton("Adicionar") { dialog, _ ->
                val usuario = spinnerUsuario.selectedItem as? Usuario
                val livro = spinnerLivro.selectedItem as? Livro
                val dataEmprestimo = textViewDataEmprestimo.text.toString()
                val dataDevolucao = textViewDataDevolucao.text.toString()

                if (usuario != null && livro != null) {
                    val emprestimo = Emprestimo(
                        dataEmprestimo = dataEmprestimo,
                        dataDevolucao = dataDevolucao,
                        usuario = usuario,
                        livro = livro
                    )
                    db.salvarEmprestimo(emprestimo)
                }
                Toast.makeText(context, "Empréstimo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                loadEmprestimos()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.let {
                when {
                    it.getBooleanExtra("EMPRESTIMO_EDITADO", false) -> {
                        Toast.makeText(context, "Empréstimo editado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    it.getBooleanExtra("EMPRESTIMO_DEVOLVIDO", false) -> {
                        Toast.makeText(context, "Empréstimo devolvido com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    it.getBooleanExtra("EMPRESTIMO_CANCELADO", false) -> {
                        Toast.makeText(context, "Empréstimo cancelado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                }
                loadEmprestimos()
            }
        }
    }
}