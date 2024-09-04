package com.example.biblioteca.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro
import java.io.ByteArrayOutputStream

class DetalhesLivroActivity : AppCompatActivity() {

    private lateinit var db: DatabaseManager
    private lateinit var livro: Livro
    private var selectedImage: ByteArray? = null
    private var dialogImageViewCapa: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_livro)

        db = DatabaseManager(this)

        val livroId = intent.getIntExtra("LIVRO_ID", -1)
        if (livroId == -1) {
            Toast.makeText(this, "Livro não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        livro = db.getLivro(livroId)

        val imageViewCapa = findViewById<ImageView>(R.id.imageViewCapa)
        val textViewTitulo = findViewById<TextView>(R.id.textViewTitulo)
        val textViewAutor = findViewById<TextView>(R.id.textViewAutor)
        val textViewISBN = findViewById<TextView>(R.id.textViewISBN)
        val textViewAno = findViewById<TextView>(R.id.textViewAno)
        val textViewEditora = findViewById<TextView>(R.id.textViewEditora)
        val textViewEdicao = findViewById<TextView>(R.id.textViewEdicao)
        val buttonRemover = findViewById<Button>(R.id.buttonRemover)
        val buttonEditar = findViewById<Button>(R.id.buttonEditar)

        livro.imagemCapa?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageViewCapa.setImageBitmap(bitmap)
        }
        textViewTitulo.text = livro.titulo
        textViewAutor.text = livro.autor
        textViewISBN.text = livro.isbn
        textViewAno.text = livro.ano.toString()
        textViewEditora.text = livro.editora
        textViewEdicao.text = livro.edicao.toString()

        buttonRemover.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar Remoção")
                .setMessage("Você realmente quer remover este livro? Todos os empréstimos relacionados a este livro também serão removidos.")
                .setPositiveButton("Sim") { _, _ ->
                    db.removerLivro(livro)
                    val resultIntent = Intent().putExtra("LIVRO_REMOVIDO", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .setNegativeButton("Não", null)
                .show()
        }

        buttonEditar.setOnClickListener {
            showEditLivroDialog()
        }
    }

    private fun showEditLivroDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_form_livro, null)
        val editTextTitulo = dialogView.findViewById<EditText>(R.id.editTextTitulo)
        val editTextAutor = dialogView.findViewById<EditText>(R.id.editTextAutor)
        val editTextISBN = dialogView.findViewById<EditText>(R.id.editTextISBN)
        val editTextAno = dialogView.findViewById<EditText>(R.id.editTextAno)
        val editTextEditora = dialogView.findViewById<EditText>(R.id.editTextEditora)
        val editTextEdicao = dialogView.findViewById<EditText>(R.id.editTextEdicao)
        dialogImageViewCapa = dialogView.findViewById(R.id.imageViewCapa)
        val buttonSelectImage = dialogView.findViewById<Button>(R.id.buttonSelectImage)

        // Populate the dialog fields with the current book details
        editTextTitulo.setText(livro.titulo)
        editTextAutor.setText(livro.autor)
        editTextISBN.setText(livro.isbn)
        editTextAno.setText(livro.ano.toString())
        editTextEditora.setText(livro.editora)
        editTextEdicao.setText(livro.edicao.toString())

        livro.imagemCapa?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            dialogImageViewCapa?.setImageBitmap(bitmap)
        }

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Livro")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                // Update the book details in the database
                livro.titulo = editTextTitulo.text.toString()
                livro.autor = editTextAutor.text.toString()
                livro.isbn = editTextISBN.text.toString()
                livro.ano = editTextAno.text.toString().toInt()
                livro.editora = editTextEditora.text.toString()
                livro.edicao = editTextEdicao.text.toString().toInt()

                selectedImage?.let {
                    livro.imagemCapa = it
                }

                db.editarLivro(livro)

                // Refresh the book details in DetalhesLivroActivity
                findViewById<TextView>(R.id.textViewTitulo).text = livro.titulo
                findViewById<TextView>(R.id.textViewAutor).text = livro.autor
                findViewById<TextView>(R.id.textViewISBN).text = livro.isbn
                findViewById<TextView>(R.id.textViewAno).text = livro.ano.toString()
                findViewById<TextView>(R.id.textViewEditora).text = livro.editora
                findViewById<TextView>(R.id.textViewEdicao).text = livro.edicao.toString()

                livro.imagemCapa?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    findViewById<ImageView>(R.id.imageViewCapa).setImageBitmap(bitmap)
                }

                val resultIntent = Intent().putExtra("LIVRO_EDITADO", true)
                setResult(Activity.RESULT_OK, resultIntent)

                Toast.makeText(this, "Livro atualizado com sucesso", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val inputStream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                dialogImageViewCapa?.setImageBitmap(bitmap)

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                selectedImage = byteArrayOutputStream.toByteArray()
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_SELECT = 1
    }
}
