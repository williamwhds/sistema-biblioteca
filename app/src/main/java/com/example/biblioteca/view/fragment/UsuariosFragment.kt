package com.example.biblioteca.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Usuario
import com.example.biblioteca.view.DetalhesUsuarioActivity
import com.example.biblioteca.view.adapter.UsuarioAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsuariosFragment : Fragment() {
    private lateinit var db: DatabaseManager
    private lateinit var recyclerViewUsuarios: RecyclerView
    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DatabaseManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_usuarios, container, false)

        recyclerViewUsuarios = view.findViewById(R.id.recyclerViewUsuarios)
        recyclerViewUsuarios.layoutManager = LinearLayoutManager(context)

        val usuarios = db.getAllUsuarios()
        usuarioAdapter = UsuarioAdapter(usuarios) { usuario ->
            val intent = Intent(context, DetalhesUsuarioActivity::class.java)
            intent.putExtra("USUARIO_ID", usuario.id)
            startActivityForResult(intent, REQUEST_DETALHES_USUARIO)
        }
        recyclerViewUsuarios.adapter = usuarioAdapter

        val fabAddUsuario: FloatingActionButton = view.findViewById(R.id.fabAddLivro)
        fabAddUsuario.setOnClickListener {
            showAddUserDialog()
        }

        if (usuarios.isEmpty()) {
            Toast.makeText(context, "Nenhum usuário cadastrado.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_form_usuario, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Adicionar Usuário")
            .setPositiveButton("Adicionar") { dialog, _ ->
                val nome = dialogView.findViewById<EditText>(R.id.editTextNome).text.toString()
                val email = dialogView.findViewById<EditText>(R.id.editTextEmail).text.toString()
                val telefone = dialogView.findViewById<EditText>(R.id.editTextTelefone).text.toString()
                val endereco = dialogView.findViewById<EditText>(R.id.editTextEndereco).text.toString()
                val usuario = Usuario(null, nome, email, telefone, endereco)
                db.salvarUsuario(usuario)
                val updatedUsuarios = db.getAllUsuarios()
                usuarioAdapter.updateUsuarios(updatedUsuarios)
                Toast.makeText(context, "Usuário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DETALHES_USUARIO && resultCode == Activity.RESULT_OK) {
            val usuarioRemovido = data?.getBooleanExtra("USUARIO_REMOVIDO", false) ?: false
            val usuarioEditado = data?.getBooleanExtra("USUARIO_EDITADO", false) ?: false
            if (usuarioRemovido || usuarioEditado) {
                refreshUsuarios()
            }
        }
    }

    private fun refreshUsuarios() {
        val usuarios = db.getAllUsuarios()
        usuarioAdapter.updateUsuarios(usuarios)
    }

    companion object {
        private const val REQUEST_DETALHES_USUARIO = 1
    }
}