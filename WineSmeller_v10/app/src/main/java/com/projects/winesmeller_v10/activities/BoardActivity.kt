package com.projects.winesmeller_v10.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import com.projects.winesmeller_v10.R
import com.projects.winesmeller_v10.others.Utilities
import kotlinx.android.synthetic.main.activity_board.*

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
        Utilities.setNavigationItemSelectedListener(nav_view, this)

            // Guardar sesión
        saveSession()

            // Llamada a la función del botón flotante
        floatButtonAction()

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

            R.id.idOptionMenuSettings -> {
                return true
            }

            R.id.idOptionMenuCloseSession -> {
                Utilities.closeSession(this, getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE), this)
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

    /*************************************************************************************
     * Implementación del botón flotante de la pantalla
     *************************************************************************************/

    private fun floatButtonAction() {

        floatingActionButton.setOnClickListener {
            Utilities.showAddWine(this)
        }
    }
}
