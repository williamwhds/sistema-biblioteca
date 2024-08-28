// app/src/main/java/com/example/biblioteca/EditarLivroActivity.kt
package com.example.biblioteca

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro
import java.io.ByteArrayOutputStream

class EditarLivroActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_SELECT = 1
    private var selectedImage: Bitmap? = null
    private var imageViewCapa: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_livro)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerViewLivros: RecyclerView = findViewById(R.id.recyclerViewLivros)
        recyclerViewLivros.layoutManager = LinearLayoutManager(this)

        val db = DatabaseManager(this)
        val livros = db.getAllLivros()
        val adapter = LivroAdapter(livros) { livro ->
            showEditDialog(livro)
        }
        recyclerViewLivros.adapter = adapter
    }

    private fun showEditDialog(livro: Livro) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_livro, null)
        imageViewCapa = dialogView.findViewById(R.id.imageViewCapa)
        val buttonSelectImage = dialogView.findViewById<Button>(R.id.buttonSelectImage)
        val editTextTitulo = dialogView.findViewById<EditText>(R.id.editTextTitulo)
        val editTextAutor = dialogView.findViewById<EditText>(R.id.editTextAutor)
        val editTextISBN = dialogView.findViewById<EditText>(R.id.editTextISBN)
        val editTextAno = dialogView.findViewById<EditText>(R.id.editTextAno)
        val editTextEditora = dialogView.findViewById<EditText>(R.id.editTextEditora)
        val editTextEdicao = dialogView.findViewById<EditText>(R.id.editTextEdicao)

        editTextTitulo.setText(livro.titulo)
        editTextAutor.setText(livro.autor)
        editTextISBN.setText(livro.isbn)
        editTextAno.setText(livro.ano.toString())
        editTextEditora.setText(livro.editora)
        editTextEdicao.setText(livro.edicao.toString())

        livro.imagemCapa?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageViewCapa?.setImageBitmap(bitmap)
        }

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Livro")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                livro.titulo = editTextTitulo.text.toString()
                livro.autor = editTextAutor.text.toString()
                livro.isbn = editTextISBN.text.toString()
                livro.ano = editTextAno.text.toString().toInt()
                livro.editora = editTextEditora.text.toString()
                livro.edicao = editTextEdicao.text.toString().toInt()

                selectedImage?.let {
                    val stream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    livro.imagemCapa = stream.toByteArray()
                }

                val db = DatabaseManager(this)
                db.editarLivro(livro)
                recreate() // Recreate activity to refresh the list
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val inputStream = contentResolver.openInputStream(it)
                selectedImage = BitmapFactory.decodeStream(inputStream)
                imageViewCapa?.setImageBitmap(selectedImage)
            }
        }
    }
}