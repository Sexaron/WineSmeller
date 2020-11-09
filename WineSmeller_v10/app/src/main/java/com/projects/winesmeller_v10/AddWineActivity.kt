package com.projects.winesmeller_v10

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_board.drawer_layout_add_wine
import kotlinx.android.synthetic.main.activity_board.nav_view

class AddWineActivity : AppCompatActivity() {

        // Spinners a declarar
//    lateinit var spinner_aging : Spinner
//    lateinit var spinner_countries : Spinner
//    lateinit var spinner_wine_types :
    lateinit var textView_spinner_aging        : TextView
    lateinit var textView_spinner_countries    : TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wine)
        setSupportActionBar(findViewById(R.id.fragment_my_toolbar))
        setTitle(R.string.activityTitle_addWine)

            // Agrega el botón de navegación al menu lateral
        Utilities.showHomeButton(true, supportActionBar, R.drawable.ic_menu)

            // Elimina la barra de notificaciones
        Utilities.noShowNotificationBar(this.window)

            // Listener del menú lateral
        Utilities.setNavigationItemSelectedListener(nav_view, this )

        textView_spinner_aging     = findViewById(R.id.text_view_spinner_aging     )
        textView_spinner_countries = findViewById(R.id.text_view_spinner_countries )

        val listCountries  : Array<out String>   = resources.getStringArray(R.array.array_countries )
        val listAging      : Array<out String>   = resources.getStringArray(R.array.array_aging     )



        textView_spinner_aging.setOnClickListener {
            Utilities.spinnerSearch(this, listAging, R.string.textView_addWine_aging, textView_spinner_aging)
        }

        textView_spinner_countries.setOnClickListener {
            Utilities.spinnerSearch(this, listCountries, R.string.textView_addWine_country, textView_spinner_countries)
        }

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
                Utilities.closeSession(this, getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE))
            }

            android.R.id.home               -> {
                drawer_layout_add_wine.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
