package com.projects.winesmeller_v10.others

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.projects.winesmeller_v10.R
import com.projects.winesmeller_v10.activities.AddWineActivity
import com.projects.winesmeller_v10.activities.AuthActivity
import com.projects.winesmeller_v10.activities.BoardActivity
import com.projects.winesmeller_v10.activities.MyWineryActivity
import kotlinx.android.synthetic.main.searchable_spinner.*
import java.util.*
import kotlin.collections.ArrayList

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
        fun closeSession(context: Context, sharedPreferences: SharedPreferences, activity: Activity) {
            AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Desea cerrar la sesión?")
                .setPositiveButton(R.string.textButton_cancel){ dialog, which ->
                }
                .setNegativeButton(R.string.textButton_confirm) { dialog, which ->
                    val prefs = sharedPreferences.edit()
                    prefs.clear()
                    prefs.apply()
                    showAuth(context)
                    activity.finish()
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
         * Navega a la pantalla de Home
         *************************************************************************************/
        fun showHome(context: Context) {
            val loginIntent = Intent(context, BoardActivity::class.java)
            context.startActivity(loginIntent)
        }

        /*************************************************************************************
         * Navega a la pantalla de añadir nuevo vino
         *************************************************************************************/
        fun showAddWine( context: Context) {
            val loginIntent = Intent(context, AddWineActivity::class.java)
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

                    R.id.nav_myWinery -> {
                        val loginIntent = Intent(context, MyWineryActivity::class.java)
                        context.startActivity(loginIntent)
                    }
                }

                return@setNavigationItemSelectedListener true
            }
        }

        /*************************************************************************************
         * Implementación de un spinner search personalizado
         *************************************************************************************/
        fun spinnerSearch (
            context: Context,
            activity: Activity,
            list: Array<out String>,
            titleText: Int,
            textView : TextView
        ) {
            val dialog = Dialog(context)

                // Obtenemos medidas de la pantalla
            val metrics = DisplayMetrics()
//            activity.display?.getRealMetrics(metrics) //-> Para APIs mas nuevas
            activity.windowManager.defaultDisplay.getRealMetrics(metrics)   // -> Para APIs más viejas

                //Puede que este cálculo para móviles con versiones más viejas no funcione.
            val width = (metrics.widthPixels / 1.20).toInt()
            val height = (metrics.heightPixels / 1.20).toInt()

            dialog.setContentView(R.layout.searchable_spinner)
            dialog.window?.setLayout( width  , height )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.titleList.setText(titleText)

            if (titleText == R.string.textView_addWine_aging || titleText == R.string.textView_addWine_typeOfWine) {
                dialog.edit_text_searcher.isVisible = false
            }

            dialog.show()

            val editText: EditText = dialog.findViewById(R.id.edit_text_searcher)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            var auxAdapter      = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
            listView.adapter    = auxAdapter

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    auxAdapter = getNewList(s, list, context)
                    listView.adapter = auxAdapter
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            listView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                textView.setText(auxAdapter.getItem(position))
                dialog.dismiss()
            }
        }

        /*************************************************************************************
         * Función utilizada en la func. anterior "spinnerSearch"
         * Devuelve los resultados de la lista que contienen los caracteres pasados por el parámetro 's' ya adaptados
         *************************************************************************************/
        private fun getNewList(s: CharSequence?, list: Array<out String>, context: Context): ArrayAdapter<String> {

            val newList : ArrayList<String> = ArrayList<String>()
            list.forEach {
            if (it.toLowerCase(Locale.ROOT).contains(s.toString().toLowerCase(Locale.ROOT))) {
                newList.add(it)
                }
            }

            val auxAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newList )
            return auxAdapter
        }


        /*************************************************************************************
         * UTILIDADES OBSOLETAS
         *************************************************************************************/

            /*************************************************************************************
             * Implementación de un spinner search
             *************************************************************************************/

//           ****** ESTA PARTE DE CÓDIGO IRÍA EN LA ACTIVITY ***************
//            spinner_aging       = findViewById(R.id.spinner_aging       )
//            spinner_countries   = findViewById(R.id.spinner_countries   )
//            spinner_wine_types  = findViewById(R.id.spinner_wine_types  )
//
//            val listAging       : Array<out String>   = resources.getStringArray(R.array.array_aging      )
//            val listCountries   : Array<out String>   = resources.getStringArray(R.array.array_countries  )
//            val listWineTypes   : Array<out String>   = resources.getStringArray(R.array.array_wine_types )
//
//            Utilities.spinnerAdapter( spinner_aging     , this, listAging       )
//            Utilities.spinnerAdapter( spinner_countries , this, listCountries   )
//            Utilities.spinnerAdapter( spinner_wine_types, this, listWineTypes   )
//          *************************************************************************************/

//            fun spinnerAdapter( spinner: Spinner, context : Context, list: Array<out String> ) {
//                spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list )
//
//                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                    }
//
//                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
//                        if (position == 0 ){
//                            Toast.makeText(context, "Please selected Number", Toast.LENGTH_SHORT).show()
//                        } else run {
//                            var sNumber: String = parent?.getItemAtPosition(position).toString()
//                        }
//                    }
//                }
//            }

    }

}
