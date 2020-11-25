package com.projects.winesmeller_v10

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_add_wine.*
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_board.drawer_layout_add_wine
import kotlinx.android.synthetic.main.activity_board.nav_view
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.*


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
    lateinit var barcode    : TextView
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
    lateinit var typeOfGrape: TextView
    lateinit var typeOfWine : TextView
    lateinit var ranting    : RatingBar

        // Cámara
    lateinit var img_photo_front : ImageView
    lateinit var img_photo_back : ImageView
    var drawable : Drawable? = null



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

        findAllElemnts()

        spinners()

        scanner()

        saveData()

        openCamera_Click()

    }

    /*************************************************************************************
     * Implementación de cámara
     *************************************************************************************/
    private fun openCamera_Click() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED ) {
                // Pedir permiso al usuario
                val permissionCamera = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissionCamera, Constants.REQUEST_CAMERA_FRONT_PHOTO)
            } else {
                waitingClickToPhoto()
            }

        } else {
            waitingClickToPhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.REQUEST_CAMERA_FRONT_PHOTO -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    waitingClickToPhoto()
                else
                    Toast.makeText(this, R.string.text_no_permissions_camera, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*************************************************************************************
     * Abre la cámara del dispositivo
     *************************************************************************************/
    private fun waitingClickToPhoto() {
        img_photo_front = findViewById(R.id.front_image_of_the_wine)
        img_photo_front.setOnClickListener {
            val value = ContentValues()
            value.put(MediaStore.Images.Media.TITLE, R.string.text_new_image)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_FRONT_PHOTO)
        }

        img_photo_back = findViewById(R.id.back_image_of_the_wine)
        img_photo_back.setOnClickListener {
            val value = ContentValues()
            value.put(MediaStore.Images.Media.TITLE, R.string.text_new_image)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_BACK_PHOTO)
        }
    }

    /*************************************************************************************
     * Asociación de los elementos del layout a las variables para inicializarlas
     *************************************************************************************/
    private fun findAllElemnts() {
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
     * Implementación de los spinners de la activity
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
     * Resultados según el requestCode que le llegue:
     *  - Scanner
     *  - Cámara
     ***********************************************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            Constants.REQUEST_SCAN -> {
                if (result != null) {
                    if (result.contents == null) {
                        Toast.makeText(this, R.string.textView_cancel, Toast.LENGTH_SHORT).show()
                    } else {
                        textView_scan_code.text = result.contents
                        fillFieldsByBarcode(result.contents, "${Constants.URL_SERVER}/${Constants.SC_GET_FILLS_BY_BARCODE}")
                    }
                }
            }

            Constants.REQUEST_CAMERA_FRONT_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = data?.extras?.get("data") as Bitmap
                    val photoScaled = Bitmap.createScaledBitmap(photo,315,560,false)
                    drawable = BitmapDrawable(resources, photoScaled)
                    img_photo_front.background = drawable
                    val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT )
                    img_photo_front.layoutParams = params
                }
            }

            Constants.REQUEST_CAMERA_BACK_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = data?.extras?.get("data") as Bitmap
                    val photoScaled = Bitmap.createScaledBitmap(photo,315,560,false)
                    drawable = BitmapDrawable(resources, photoScaled)
                    img_photo_back.background = drawable
                    val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT )
                    img_photo_back.layoutParams = params
                }
            }
        }
    }

    /*************************************************************************************
     * Implementación del botón Guardar
     ***********************************************************************************/
    private fun saveData() {

        btn_addWine.setOnClickListener {

            // Comprobar si se han rellenado todos los datos "obligatorios" ? comprobar si el barcode lo tenemos en BBDD : continuar proceso normal
            // Comparar barcode en BBDD si está ? no se hace nada : se añade a la BBDD

            val barcodeST       = barcode.text.toString().toUpperCase(Locale.ROOT)
            val nameST          = name.text.toString().toUpperCase(Locale.ROOT)
            val wineryST        = winery.text.toString().toUpperCase(Locale.ROOT)
            val agingST         = aging.text.toString().toUpperCase(Locale.ROOT)
            val countryST       = country.text.toString().toUpperCase(Locale.ROOT)
            val denOfOriginST   = denOfOrigin.text.toString().toUpperCase(Locale.ROOT)
            val wineTypeST      = wineType.text.toString().toUpperCase(Locale.ROOT)
            val yearST          = year.text.toString().toUpperCase(Locale.ROOT)
            val volST           = vol.text.toString().toUpperCase(Locale.ROOT)
            val grapeTypeST     = grapeType.text.toString().toUpperCase(Locale.ROOT)
            val ecologicST      = ecologic.isChecked.toString().toUpperCase(Locale.ROOT)
            val notesST         = notes.text.toString().toUpperCase(Locale.ROOT)
            val rantingST       = ranting.rating.toString().toUpperCase(Locale.ROOT)

                // Comprueba si todos los campos necesarios están rellenos
            if ( barcodeST != "" && nameST != "" && wineryST != "" && agingST != "" && countryST != "" &&
                denOfOriginST != "" && wineTypeST != "" && yearST != "" && volST != "" && grapeTypeST != "" ) {

                    // consultamos en BBDD si el barcode existe
                existBarcode("${Constants.URL_SERVER}/${Constants.SC_EXIST_BARCODE}", barcodeST, nameST, wineryST, agingST, countryST,
                        denOfOriginST, wineTypeST, yearST, volST, grapeTypeST, ecologicST, notesST, rantingST)
            } else {
                // TODO : Guardar el vino en la bodega del usuario
                Utilities.showHome(this)
            }
        }
    }

    /*************************************************************************************
     * Acceso a la BBDD para consultar si existe el barcode del vino introducido
     ***********************************************************************************/
    private fun existBarcode(
            URL: String,
            barcodeST: String,
            nameST: String,
            wineryST: String,
            agingST: String,
            countryST: String,
            denOfOriginST: String,
            wineTypeST: String,
            yearST: String,
            volST: String,
            grapeTypeST: String,
            ecologicST: String,
            notesST: String,
            rantingST: String
    ) {
        var resultado       : Any   = ""
        var message         : Any   = ""

        val URL_TO_SEND : String = URL + "?barcode=${barcodeST}"

        val request = JsonObjectRequest(URL_TO_SEND, null, { response ->
            Log.i(LOG_INFO, "Response is: $response")
            val jsonRQ: JSONObject = JSONObject(response.toString())
            resultado = jsonRQ.get("Success")
            message = jsonRQ.get("Message")

            if (resultado.toString() == "OK") {
//                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                // TODO : Guardar el vino en la bodega del usuario
                Utilities.showHome(this)
            } else {
//                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                // TODO : Guardar el vino en la bodega del usuario
                addWine_BBDD("${Constants.URL_SERVER}/${Constants.SC_ADD_WINE}",
                        barcodeST, nameST, wineryST, agingST, countryST, denOfOriginST, wineTypeST, yearST, volST, grapeTypeST, ecologicST, notesST, rantingST)
            }
        }, { error ->
            Log.e(LOG_ERROR, error.toString())
            error.printStackTrace()
        })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    /*************************************************************************************
     * Acceso a la BBDD para guardar el nuevo Vino con todos los datos pasados por parámetro
     ***********************************************************************************/
    private fun addWine_BBDD(
            URL: String,
            barcode: String,
            name: String,
            winery: String,
            aging: String,
            country: String,
            denOfOrigin: String,
            wineType: String,
            year: String,
            vol: String,
            grapeType: String,
            ecologic: String,
            notes: String,
            ranting: String
    ) {
        var resultado       : Any   = ""
        var message         : Any   = ""

        val URL_TO_SEND : String = URL + "?name=${name}&vol=${vol}&year=${year}&winery=${winery}&" +
                "barcode=${barcode}&denOfOrigin=${denOfOrigin}&ecologic=${ecologic}&aging=${aging}&" +
                "country=${country}&grapeType=${grapeType}&wineType=${wineType}&ranting=${ranting}&notes=${notes}"

        val request = JsonObjectRequest(URL_TO_SEND, null, { response ->
            Log.i(LOG_INFO, "Response is: $response")
            val jsonRQ: JSONObject = JSONObject(response.toString())
            resultado = jsonRQ.get("Success")
            message = jsonRQ.get("Message")

            if (resultado.toString() == "OK") {
                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                Utilities.showHome(this)
            } else {
                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
            }
        }, { error ->
            Log.e(LOG_ERROR, error.toString())
            error.printStackTrace()
        })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    /*************************************************************************************
     * Acceso a la BBDD para obtener todos los datos del vino en el caso de que exista en BBDD
     ***********************************************************************************/
    private fun fillFieldsByBarcode(barcode: String, URL: String) {
        var resultado       : Any   = ""

        val URL_TO_SEND : String = URL + "?barcode=$barcode"

        val request = JsonObjectRequest(URL_TO_SEND, null, { response ->
            Log.i(LOG_INFO, "Response is: $response")
            val jsonRQ: JSONObject = JSONObject(response.toString())
            resultado = jsonRQ.get("Success")

            if (resultado.toString() == "OK") {
//                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                val array: JSONObject = jsonRQ.getJSONObject("0")
                fillAllFields(array)
            } else {
//                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                // No existe el barcode en BBDD. Por lo tanto no rellena campos
            }
        }, { error ->
            Log.e(LOG_ERROR, error.toString())
            error.printStackTrace()
        })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    /*************************************************************************************
     * Se rellenan todos los campos del layout con los datos obtenidos anteriormente de la BBDD
     ***********************************************************************************/
    private fun fillAllFields(array: JSONObject) {
        var barcodeRST      : Any   = ""
        var nameRST         : Any   = ""
        var volRST          : Any   = ""
        var yearRST         : Any   = ""
        var wineryRST       : Any   = ""
        var denOfOriginRST  : Any   = ""
        var ecologicRST     : Any   = ""
        var agingRST        : Any   = ""
        var countryRST      : Any   = ""
        var grapeTypeRST    : Any   = ""
        var wineTypeRST     : Any   = ""

        barcodeRST              = array.getString("BARCODE")
        nameRST                 = array.getString("NAME")
        volRST                  = array.getString("VOL")
        yearRST                 = array.getString("YEAR")
        wineryRST               = array.getString("WINERY")
        denOfOriginRST          = array.getString("DENOMINATION_OF_ORIGIN")
        ecologicRST             = array.getString("ECOLOGIC")
        agingRST                = array.getString("AGING")
        countryRST              = array.getString("COUNTRY")
        grapeTypeRST            = array.getString("TYPE_OF_GRAPE")
        wineTypeRST           = array.getString("TYPE_OF_WINE")

        barcode.setText(barcodeRST.toString())
        name.setText(nameRST.toString())
        vol.setText(volRST.toString())
        year.setText(yearRST.toString())
        winery.setText(wineryRST.toString())
        denOfOrigin.setText(denOfOriginRST.toString())
        if (ecologicRST.toString() == "TRUE") {
            ecologic.isChecked = true
        }
        aging.setText(agingRST.toString())
        country.setText(countryRST.toString())
        grapeType.setText(grapeTypeRST.toString())
        wineType.setText(wineTypeRST.toString())

    }

}
