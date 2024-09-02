package com.example.biblioteca.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.model.Livro

class LivroAdapter(
    private var livros: List<Livro>,
    private val onItemClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTituloLivro: TextView = itemView.findViewById(R.id.textViewNomeLivro)
        val textViewAutorLivro: TextView = itemView.findViewById(R.id.textViewAutorLivro)
        val imageViewCapaLivro: ImageView = itemView.findViewById(R.id.imageViewCapa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        holder.textViewTituloLivro.text = livro.titulo
        holder.textViewAutorLivro.text = livro.autor

        livro.imagemCapa?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageViewCapaLivro.setImageBitmap(bitmap)
        }

        holder.itemView.setOnClickListener {
            onItemClick(livro)
        }
    }

    override fun getItemCount() = livros.size

    fun updateLivros(newLivros: List<Livro>) {
        livros = newLivros
        notifyDataSetChanged()
    }
}
