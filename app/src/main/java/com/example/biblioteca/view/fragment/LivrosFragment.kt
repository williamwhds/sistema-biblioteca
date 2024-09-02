package com.example.biblioteca.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.database.DatabaseManager
import com.example.biblioteca.model.Livro
import com.example.biblioteca.view.DetalhesLivroActivity
import com.example.biblioteca.view.adapter.LivroAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class LivrosFragment : Fragment() {
    private lateinit var db: DatabaseManager
    private lateinit var recyclerViewLivros: RecyclerView
    private lateinit var livroAdapter: LivroAdapter
    private var selectedImage: Bitmap? = null
    private var imageViewCapa: ImageView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DatabaseManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_livros, container, false)

        recyclerViewLivros = view.findViewById(R.id.recyclerViewLivros)
        recyclerViewLivros.layoutManager = GridLayoutManager(context, calculateSpanCount())

        val livros = db.getAllLivros()
        livroAdapter = LivroAdapter(livros) { livro ->
            val intent = Intent(context, DetalhesLivroActivity::class.java)
            intent.putExtra("LIVRO_ID", livro.id)
            startActivityForResult(intent, 1)
        }
        recyclerViewLivros.adapter = livroAdapter

        val fabAddLivro: FloatingActionButton = view.findViewById(R.id.fabAddLivro)
        fabAddLivro.setOnClickListener {
            showAddLivroDialog()
        }

        if (livros.isEmpty()) {
            Toast.makeText(context, "Nenhum livro cadastrado", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showAddLivroDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_form_livro, null)
        imageViewCapa = dialogView.findViewById(R.id.imageViewCapa)
        val buttonSelectImage = dialogView.findViewById<Button>(R.id.buttonSelectImage)
        val editTextTitulo = dialogView.findViewById<EditText>(R.id.editTextTitulo)
        val editTextAutor = dialogView.findViewById<EditText>(R.id.editTextAutor)
        val editTextISBN = dialogView.findViewById<EditText>(R.id.editTextISBN)
        val editTextAno = dialogView.findViewById<EditText>(R.id.editTextAno)
        val editTextEditora = dialogView.findViewById<EditText>(R.id.editTextEditora)
        val editTextEdicao = dialogView.findViewById<EditText>(R.id.editTextEdicao)

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Adicionar Livro")
            .setPositiveButton("Adicionar") { dialog, _ ->
                val titulo = editTextTitulo.text.toString()
                val autor = editTextAutor.text.toString()
                val isbn = editTextISBN.text.toString()
                val ano = editTextAno.text.toString().toInt()
                val editora = editTextEditora.text.toString()
                val edicao = editTextEdicao.text.toString().toInt()

                val livro = Livro(null, titulo, autor, isbn, ano, editora, edicao)

                selectedImage?.let {
                    val stream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    livro.imagemCapa = stream.toByteArray()
                }

                db.salvarLivro(livro)
                val updatedLivros = db.getAllLivros()
                livroAdapter.updateLivros(updatedLivros)
                Toast.makeText(context, "Livro adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Atualiza a imagem da capa do livro ao selecionar uma imagem da galeria
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val inputStream = context?.contentResolver?.openInputStream(it)
                selectedImage = BitmapFactory.decodeStream(inputStream)
                imageViewCapa?.setImageBitmap(selectedImage)
            }
        }
        // Se um livro foi removido ou editado, atualiza a lista de livros
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val livroRemovido = data?.getBooleanExtra("LIVRO_REMOVIDO", false) ?: false
            val livroEditado = data?.getBooleanExtra("LIVRO_EDITADO", false) ?: false
            if (livroRemovido || livroEditado) {
                refreshLivros()
            }
        }
    }

    private fun refreshLivros() {
        val livros = db.getAllLivros()
        livroAdapter.updateLivros(livros)
    }

    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val itemWidthDp = 150
        return (screenWidthDp / itemWidthDp).toInt().coerceAtLeast(2)
    }

    companion object {
        private const val REQUEST_IMAGE_SELECT = 1
    }
}
