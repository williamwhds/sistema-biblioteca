package com.example.biblioteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.biblioteca.view.fragment.ConfigFragment
import com.example.biblioteca.view.fragment.EmprestimosFragment
import com.example.biblioteca.view.fragment.LivrosFragment
import com.example.biblioteca.view.fragment.UsuariosFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_books -> {
                    loadFragment(LivrosFragment())
                    true
                }
                R.id.nav_users -> {
                    loadFragment(UsuariosFragment())
                    true
                }
                R.id.nav_loans -> {
                    loadFragment(EmprestimosFragment())
                    true
                }
                R.id.nav_config -> {
                    loadFragment(ConfigFragment())
                    true
                }
                else -> false
            }
        }

        // fragmento padrão é LivrosFragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_books
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
