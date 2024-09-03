// Arquivo: app/src/main/java/com/example/biblioteca/view/adapter/EmprestimoAdapter.kt
package com.example.biblioteca.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Emprestimo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class EmprestimoAdapter(
    private var emprestimos: List<Emprestimo>,
    private val onItemClick: (Emprestimo) -> Unit,
    private val db: DatabaseManager
) : RecyclerView.Adapter<EmprestimoAdapter.EmprestimoViewHolder>() {

    class EmprestimoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewEmprestimoInfo: TextView = itemView.findViewById(R.id.textViewEmprestimoInfo)
        val imageViewLivro: ImageView = itemView.findViewById(R.id.imageViewLivro)
        val textViewDataEmprestimo: TextView = itemView.findViewById(R.id.textViewDataEmprestimo)
        val textViewDataDevolucao: TextView = itemView.findViewById(R.id.textViewDataDevolucao)
        val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmprestimoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_emprestimo, parent, false)
        return EmprestimoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmprestimoViewHolder, position: Int) {
        val emprestimo = emprestimos[position]
        holder.textViewEmprestimoInfo.text = "${emprestimo.usuario.nome} pegou ${emprestimo.livro.titulo} emprestado"
        holder.textViewDataEmprestimo.text = "Data de Empréstimo: ${emprestimo.dataEmprestimo}"
        holder.textViewDataDevolucao.text = "Data de Devolução: ${emprestimo.dataDevolucao}"
        holder.textViewStatus.text = getStatusText(emprestimo)

        // Buscar a imagem do livro no banco de dados e configurar no ImageView
        val imagemLivro: Bitmap? = emprestimo.livro.id?.let { db.getLivroImagem(it) }
        if (imagemLivro != null) {
            holder.imageViewLivro.setImageBitmap(imagemLivro)
        } else {
            holder.imageViewLivro.setImageResource(R.drawable.ic_placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClick(emprestimo)
        }
    }

    override fun getItemCount() = emprestimos.size

    private fun getStatusText(emprestimo: Emprestimo): String {
        val dataAtual = Date()
        val dataDevolucao = stringToDate(emprestimo.dataDevolucao) ?: return "Data de devolução inválida"

        val diasRestantes = getDaysBetweenDates(dataAtual, dataDevolucao)

        if (emprestimo.devolvido) {
            val dataDevolucaoEfetiva = emprestimo.dataDevolucaoEfetiva?.let { stringToDate(it) }
            return if (dataDevolucaoEfetiva != null && dataDevolucaoEfetiva.after(dataDevolucao)) {
                "Devolvido com atraso em ${emprestimo.dataDevolucaoEfetiva}"
            } else {
                "Devolvido em ${emprestimo.dataDevolucaoEfetiva ?: "Data inválida"}"
            }
        }

        return when {
            diasRestantes < 0 -> "Atrasado"
            diasRestantes.toInt() == 0 -> "Devolver hoje"
            else -> "Devolver em $diasRestantes dias"
        }
    }

    private fun stringToDate(dateString: String): Date? {
        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    private fun getDaysBetweenDates(date1: Date, date2: Date): Long {
        val diff = date2.time - date1.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun updateEmprestimos(newEmprestimos: List<Emprestimo>) {
        emprestimos = newEmprestimos
        notifyDataSetChanged()
    }
}