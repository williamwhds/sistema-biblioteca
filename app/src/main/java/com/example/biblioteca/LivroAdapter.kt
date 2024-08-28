package com.example.biblioteca

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.model.Livro

class LivroAdapter(private val livros: List<Livro>) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewCapa: ImageView = itemView.findViewById(R.id.imageViewCapa)
        val textViewNomeLivro: TextView = itemView.findViewById(R.id.textViewNomeLivro)
        val textViewAutorLivro: TextView = itemView.findViewById(R.id.textViewAutorLivro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        holder.textViewNomeLivro.text = livro.titulo
        holder.textViewAutorLivro.text = livro.autor

        // Transformar ByteArray em Bitmap e definir no ImageView
        livro.imagemCapa?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageViewCapa.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount() = livros.size
}