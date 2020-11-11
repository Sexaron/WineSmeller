package com.projects.winesmeller_v10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_board.*


class AddWineActivity : AppCompatActivity() {

        // Spinners a declarar
    lateinit var textView_spinner_aging        : TextView
    lateinit var textView_spinner_countries    : TextView
    lateinit var textView_spinner_wine_types    : TextView

        // Scanner
    lateinit var btn_scan : Button
    lateinit var textView_scan_code: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wine)
        setSupportActionBar(findViewById(R.id.fragment_my_toolbar))
        setTitle(R.string.activityTitle_addWine)

            // Agrega el botón de navegación al menu lateral
        Utilities.showHomeButton(true, supportActionBar, R.drawable.ic_menu)

            // Elimina la barra de notificaciones
//        Utilities.noShowNotificationBar(this.window)

            // Listener del menú lateral
        Utilities.setNavigationItemSelectedListener(nav_view, this)

            // Spinners
        textView_spinner_aging     = findViewById(R.id.text_view_spinner_aging)
        textView_spinner_countries = findViewById(R.id.text_view_spinner_countries)
        textView_spinner_wine_types = findViewById(R.id.text_view_spinner_wine_types)

        val listCountries  : Array<out String>   = resources.getStringArray(R.array.array_countries)
        val listAging      : Array<out String>   = resources.getStringArray(R.array.array_aging)
        val listWineTypes  : Array<out String>   = resources.getStringArray(R.array.array_wine_types)

        textView_spinner_aging.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                listAging,
                R.string.textView_addWine_aging,
                textView_spinner_aging
            )
        }

        textView_spinner_countries.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                listCountries,
                R.string.textView_addWine_country,
                textView_spinner_countries
            )
        }

        textView_spinner_wine_types.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                listWineTypes,
                R.string.textView_addWine_typeOfWine,
                textView_spinner_wine_types
            )
        }

            // Scan
        btn_scan = findViewById(R.id.button_scan_code)
        textView_scan_code = findViewById(R.id.editText_barCode)

        btn_scan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(true)
            intentIntegrator.setCaptureActivity(OrientationCaptureActivity::class.java)
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.initiateScan()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("BoardActivity", "Scanned")
                Toast.makeText(this, "Scanned -> " + result.contents, Toast.LENGTH_SHORT).show()
                textView_scan_code.text = String.format("Scanned Result: %s", result)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    /*************************************************************************************
     * Las dos siguientes funciones se encargan de gestionar las opciones de la action bar
     *************************************************************************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {

            R.id.idOptionMenuSettings -> {
                return true
            }

            R.id.idOptionMenuCloseSession -> {
                Utilities.closeSession(
                    this, getSharedPreferences(
                        getString(R.string.prefs_file),
                        MODE_PRIVATE
                    ), this
                )
            }

            android.R.id.home -> {
                drawer_layout_add_wine.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
