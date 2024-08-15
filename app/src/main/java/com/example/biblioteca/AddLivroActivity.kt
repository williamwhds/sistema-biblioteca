package com.example.biblioteca

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro
import java.io.ByteArrayOutputStream

class AddLivroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_livro)

        val buttonSalvar: Button = findViewById(R.id.buttonSalvar)
        val editTextTitulo: EditText = findViewById(R.id.editTextTitulo)
        val editTextAutor: EditText = findViewById(R.id.editTextAutor)
        val editTextISBN: EditText = findViewById(R.id.editTextISBN)
        val editTextAno: EditText = findViewById(R.id.editTextAno)
        val editTextEditora: EditText = findViewById(R.id.editTextEditora)
        val editTextEdicao: EditText = findViewById(R.id.editTextEdicao)
        val imageViewCapa: ImageView = findViewById(R.id.imageViewCapa)
        val buttonSelecionarImagem: Button = findViewById(R.id.buttonSelecionarImagem)

        val db = DatabaseManager(this)

        val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImage = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                imageViewCapa.setImageBitmap(bitmap)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonSelecionarImagem.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImage.launch(intent)
        }

        buttonSalvar.setOnClickListener {
            // Todas são obrigatórias, excepto a imagem da capa
            val titulo = editTextTitulo.text.toString()
            val autor = editTextAutor.text.toString()
            val isbn = editTextISBN.text.toString()
            val ano = editTextAno.text.toString().toInt()
            val editora = editTextEditora.text.toString()
            val edicao = editTextEdicao.text.toString().toInt()
            val imagemCapa = imageViewCapa.drawable

            val livro = Livro(db.ultimoIdLivro() + 1, titulo, autor, isbn, ano, editora, edicao)

            if (imagemCapa != null) {
                val stream = ByteArrayOutputStream()
                val bitmap = (imagemCapa as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                livro.imagemCapa = stream.toByteArray()
            }

            db.salvarLivro(livro)
        }
    }
}