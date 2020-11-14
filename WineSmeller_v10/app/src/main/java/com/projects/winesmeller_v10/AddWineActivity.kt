package com.projects.winesmeller_v10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_board.*
import org.json.JSONObject
import java.net.URL


class AddWineActivity : AppCompatActivity() {

    //LOGS
    val LOG_INFO    = "LOG_INFO"
    val LOG_ERROR   = "LOG_ERROR"

        // Spinners a declarar
    lateinit var textView_spinner_aging        : TextView
    lateinit var textView_spinner_countries    : TextView
    lateinit var textView_spinner_wine_types    : TextView

        // Scanner
    lateinit var btn_scan : Button
    lateinit var textView_scan_code: TextView

        // Botones
    lateinit var btn_addWine : Button

        // Valores a recuperar
    lateinit var barcode    : EditText
    lateinit var name       : EditText
    lateinit var winery     : EditText
    lateinit var aging      : TextView
    lateinit var country    : TextView
    lateinit var denOfOrigin: EditText
    lateinit var wineType   : TextView
    lateinit var year       : EditText
    lateinit var vol        : EditText
    lateinit var grapeType  : EditText
    lateinit var ecologic   : CheckBox
    lateinit var notes      : EditText
    lateinit var ranting    : RatingBar

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

        spinners()

        scanner()

        saveData()

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

    /*************************************************************************************
     * Imlpementación de los spinners de la activity
     *************************************************************************************/
    private fun spinners() {
        textView_spinner_aging     = findViewById(R.id.text_view_spinner_aging)
        textView_spinner_countries = findViewById(R.id.text_view_spinner_countries)
        textView_spinner_wine_types = findViewById(R.id.text_view_spinner_wine_types)

        val listCountries  : Array<out String>   = resources.getStringArray(R.array.array_countries)
        val listAging      : Array<out String>   = resources.getStringArray(R.array.array_aging)
        val listWineTypes  : Array<out String>   = resources.getStringArray(R.array.array_wine_types)

        textView_spinner_aging.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                this,
                listAging,
                R.string.textView_addWine_aging,
                textView_spinner_aging
            )
        }

        textView_spinner_countries.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                this,
                listCountries,
                R.string.textView_addWine_country,
                textView_spinner_countries
            )
        }

        textView_spinner_wine_types.setOnClickListener {
            Utilities.spinnerSearch(
                this,
                this,
                listWineTypes,
                R.string.textView_addWine_typeOfWine,
                textView_spinner_wine_types
            )
        }
    }

    /*************************************************************************************
     * Implementación del Scanner
     ***********************************************************************************/
    private fun scanner() {
        btn_scan = findViewById(R.id.button_scan_code)
        textView_scan_code = findViewById(R.id.edit_text_barCode)

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

    /*************************************************************************************
     * Resultados del Scann
     ***********************************************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("BoardActivity", "Scanned")
//                Toast.makeText(this, "Scanned -> " + result.contents, Toast.LENGTH_SHORT).show()
//                textView_scan_code.text = String.format("Scanned Result: %s", result)
                textView_scan_code.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*************************************************************************************
     * Implementación del botón Guardar
     ***********************************************************************************/
    private fun saveData() {
        barcode         = findViewById(R.id.edit_text_barCode)
        name            = findViewById(R.id.edit_text_name)
        winery          = findViewById(R.id.edit_text_winery)
        aging           = findViewById(R.id.text_view_spinner_aging)
        country         = findViewById(R.id.text_view_spinner_countries)
        denOfOrigin     = findViewById(R.id.edit_text_denomination_of_origin)
        wineType        = findViewById(R.id.text_view_spinner_wine_types)
        year            = findViewById(R.id.edit_text_year)
        vol             = findViewById(R.id.edit_text_alcohol)
        grapeType       = findViewById(R.id.edit_text_type_of_grape)
        ecologic        = findViewById(R.id.check_box_ecologic)
        notes           = findViewById(R.id.edit_text_notes)
        ranting         = findViewById(R.id.rating_bar_rating)
        btn_addWine     = findViewById(R.id.button_add_wine)

        btn_addWine.setOnClickListener {

//            saveWine_BBDD( barcode.text, name.text, winery.text, aging.text, country.text, denOfOrigin.text, wineType.text, year.text, vol.text, grapeType.text, ecologic.isChecked, notes.text, ranting.rating) {
            addWine_BBDD( "${Constants.URL_SERVER}/${Constants.SC_ADD_WINE}",
                barcode, name, winery, aging, country, denOfOrigin, wineType, year, vol, grapeType, ecologic, notes, ranting)
        }
    }

    /*************************************************************************************
     * Acceso a la BBDD para guardar el nuevo Vino con todos los datos pasados por parámetro
     ***********************************************************************************/
    private fun addWine_BBDD(
        URL : String,
        barcode: EditText?,
        name: EditText?,
        winery: EditText?,
        aging: TextView?,
        country: TextView?,
        denOfOrigin: EditText?,
        wineType: TextView?,
        year: EditText?,
        vol: EditText?,
        grapeType: EditText?,
        ecologic: CheckBox?,
        notes: EditText?,
        ranting: RatingBar?
    ) {
        var resultado       : Any   = ""
        var message         : Any   = ""

        val URL_TO_SEND : String = URL + "?name=${name?.text.toString().toUpperCase()}&vol=${vol?.text}&year=${year?.text}&winery=${winery?.text.toString().toUpperCase()}&" +
                "barcode=${barcode?.text}&denOfOrigin=${denOfOrigin?.text.toString().toUpperCase()}&ecologic=${ecologic?.isChecked}&aging=${aging?.text.toString().toUpperCase()}&" +
                "country=${country?.text.toString().toUpperCase()}&grapeType=${grapeType?.text.toString().toUpperCase()}&wineType=${wineType?.text.toString().toUpperCase()}&ranting=${ranting?.rating}&notes=${notes?.text}"

        println(URL_TO_SEND)

        val request = JsonObjectRequest( URL_TO_SEND, null, { response ->
            Log.i(LOG_INFO, "Response is: $response")
            val jsonRQ : JSONObject = JSONObject(response.toString())
            resultado   = jsonRQ.get("Success")
            message     = jsonRQ.get("Message")

            if ( resultado.toString() == "OK") {
                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                Utilities.showHome(this)
            } else {
                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
            }
        }, { error ->
            Log.e (LOG_ERROR, error.toString())
            error.printStackTrace()
        })

        val queue = Volley.newRequestQueue(this )
        queue.add(request)
    }

}
