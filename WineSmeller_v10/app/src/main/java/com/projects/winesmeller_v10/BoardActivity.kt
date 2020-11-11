package com.projects.winesmeller_v10

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_board.drawer_layout_add_wine
import kotlinx.android.synthetic.main.activity_board.nav_view
import kotlinx.android.synthetic.main.searchable_spinner.*

class BoardActivity : AppCompatActivity() {

        //LOGS
    val LOG_INFO    = "LOG_INFO"
    val LOG_ERROR   = "LOG_ERROR"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setSupportActionBar(findViewById(R.id.fragment_my_toolbar))
        setTitle(R.string.activityTitle_board)

            // Agrega el botón de navegación al menu lateral
        Utilities.showHomeButton(true, supportActionBar, R.drawable.ic_menu)

            // Elimina la barra de notificaciones
//        Utilities.noShowNotificationBar(this.window)

            // Listener del menú lateral
        Utilities.setNavigationItemSelectedListener(nav_view, this )

            // Guardar sesión
        saveSession()

    }

    /*************************************************************************************
     * Las dos siguientes funciones se encargan de gestionar las opciones de la action bar
     *************************************************************************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {

            R.id.idOptionMenuSettings       -> {
                return true
            }

            R.id.idOptionMenuCloseSession   -> {
                Utilities.closeSession(this, getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE), this)
            }

            android.R.id.home               -> {
                drawer_layout_add_wine.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /*************************************************************************************
     * Pregunta si se pulsó la opción de guardar credenciales.
     * Si es true, los credenciales se guardan en el sharedPreferences
     *************************************************************************************/
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
}
