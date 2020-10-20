package com.projects.winesmeller_v10

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

class BoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setSupportActionBar(findViewById(R.id.fragment_my_toolbar))
        setTitle(R.string.activityTitle_board)

        // Guardar sesión
        saveSession()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.idOptionMenuSettings) {
            return true
        }

        if (id == R.id.idOptionMenuCloseSession) {
            closeSession()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveSession(){

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val check = bundle?.getBoolean("check")

        if (check != null && check == true) {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE ).edit()
            prefs.putString("email", email)
            prefs.putBoolean("check", check)
            prefs.apply()
        }
    }

    private fun closeSession() {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Desea cerrar la sesión?")
            .setPositiveButton(R.string.textButton_cancel){dialog, which ->
            }
            .setNegativeButton(R.string.textButton_confirm){dialog, which ->
                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                showAuth()
            }.create().show()
    }

    private fun showAuth() {
        val loginIntent = Intent(this, AuthActivity::class.java)
        startActivity(loginIntent)
    }
}
