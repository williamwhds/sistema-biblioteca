package com.example.biblioteca.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Usuario

class DetalhesUsuarioActivity : AppCompatActivity() {

    private lateinit var db: DatabaseManager
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_usuario)

        db = DatabaseManager(this)

        val usuarioId = intent.getIntExtra("USUARIO_ID", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        usuario = db.getUsuario(usuarioId)

        val textViewNome = findViewById<TextView>(R.id.textViewNome)
        val textViewEmail = findViewById<TextView>(R.id.textViewEmail)
        val textViewTelefone = findViewById<TextView>(R.id.textViewTelefone)
        val textViewEndereco = findViewById<TextView>(R.id.textViewEndereco)
        val buttonRemover = findViewById<Button>(R.id.buttonRemover)
        val buttonEditar = findViewById<Button>(R.id.buttonEditar)

        textViewNome.text = usuario.nome
        textViewEmail.text = usuario.email
        textViewTelefone.text = usuario.telefone
        textViewEndereco.text = usuario.endereco

        buttonRemover.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar Remoção")
                .setMessage("Você realmente quer remover este usuário? Todos os empréstimos relacionados a este usuário também serão removidos.")
                .setPositiveButton("Sim") { _, _ ->
                    db.removerUsuario(usuario)
                    val resultIntent = Intent().putExtra("USUARIO_REMOVIDO", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .setNegativeButton("Não", null)
                .show()
        }

        buttonEditar.setOnClickListener {
            showEditUsuarioDialog()
        }
    }

    private fun showEditUsuarioDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_form_usuario, null)
        val editTextNome = dialogView.findViewById<EditText>(R.id.editTextNome)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextTelefone = dialogView.findViewById<EditText>(R.id.editTextTelefone)
        val editTextEndereco = dialogView.findViewById<EditText>(R.id.editTextEndereco)

        editTextNome.setText(usuario.nome)
        editTextEmail.setText(usuario.email)
        editTextTelefone.setText(usuario.telefone)
        editTextEndereco.setText(usuario.endereco)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Usuário")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                usuario.nome = editTextNome.text.toString()
                usuario.email = editTextEmail.text.toString()
                usuario.telefone = editTextTelefone.text.toString()
                usuario.endereco = editTextEndereco.text.toString()

                db.editarUsuario(usuario)

                findViewById<TextView>(R.id.textViewNome).text = usuario.nome
                findViewById<TextView>(R.id.textViewEmail).text = usuario.email
                findViewById<TextView>(R.id.textViewTelefone).text = usuario.telefone
                findViewById<TextView>(R.id.textViewEndereco).text = usuario.endereco

                val resultIntent = Intent().putExtra("USUARIO_EDITADO", true)
                setResult(Activity.RESULT_OK, resultIntent)

                Toast.makeText(this, "Usuário atualizado com sucesso", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}