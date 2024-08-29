// com/example/biblioteca/view/UsuarioAdapter.kt
package com.example.biblioteca.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.model.Usuario

class UsuarioAdapter(
    private val usuarios: List<Usuario>,
    private val onItemClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNomeUsuario: TextView = itemView.findViewById(R.id.textViewNomeUsuario)
        val textViewEmailUsuario: TextView = itemView.findViewById(R.id.textViewEmailUsuario)
        val textViewTelefoneUsuario: TextView = itemView.findViewById(R.id.textViewTelefoneUsuario)
        val textViewEnderecoUsuario: TextView = itemView.findViewById(R.id.textViewEnderecoUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.textViewNomeUsuario.text = usuario.nome
        holder.textViewEmailUsuario.text = usuario.email
        holder.textViewTelefoneUsuario.text = usuario.telefone
        holder.textViewEnderecoUsuario.text = usuario.endereco

        holder.itemView.setOnClickListener {
            onItemClick(usuario)
        }
    }

    override fun getItemCount() = usuarios.size
}