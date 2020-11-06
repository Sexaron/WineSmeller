package com.projects.winesmeller_v10

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView

class Utilities {

    companion object {

        //LOGS
        val LOG_INFO    = "LOG_INFO"
        val LOG_ERROR   = "LOG_ERROR"

        /*************************************************************************************
         * Instrucción para mostrar la pantalla completa. No se mostrará la barra de notificaciones
         *************************************************************************************/
        fun noShowNotificationBar(window: Window?) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        /*************************************************************************************
         * Instrucción para mostrar el botón de navegación al menú lateral de la app
         *************************************************************************************/
        fun showHomeButton(b: Boolean, supportActionBar: ActionBar?, icMenu: Int) {
            var actionBar : androidx.appcompat.app.ActionBar? = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(b)
            actionBar?.setHomeAsUpIndicator(icMenu)
        }

        /*************************************************************************************
         * Permite cerrar la sesión actual.
         * Borra los parámetros de la sesión
         *************************************************************************************/
        fun closeSession(context: Context, sharedPreferences: SharedPreferences) {
            AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Desea cerrar la sesión?")
                .setPositiveButton(R.string.textButton_cancel){dialog, which ->
                }
                .setNegativeButton(R.string.textButton_confirm) {dialog, which ->
                    val prefs = sharedPreferences.edit()
                    prefs.clear()
                    prefs.apply()
                    showAuth(context)
                }.create().show()
        }

        /*************************************************************************************
         * Navega a la pantalla de Authentication
         *************************************************************************************/
        fun showAuth(context: Context) {
            val loginIntent = Intent(context, AuthActivity::class.java)
            context.startActivity(loginIntent)
        }

        /*************************************************************************************
         * Listener del menú lateral
         *************************************************************************************/
        fun setNavigationItemSelectedListener( nav_view: NavigationView, context : Context ) {

            nav_view.setNavigationItemSelectedListener {

                when(it.itemId) {

                    R.id.nav_addWine -> {
                        val loginIntent = Intent(context, AddWineActivity::class.java)
                        context.startActivity(loginIntent)
                    }

                    R.id.nav_board -> {
                        val loginIntent = Intent(context, BoardActivity::class.java)
                        context.startActivity(loginIntent)
                    }
                }

                return@setNavigationItemSelectedListener true
            }
        }
    }

}
