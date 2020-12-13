package com.projects.winesmeller_v10.objects

import android.widget.CheckBox
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.projects.winesmeller_v10.R
import com.projects.winesmeller_v10.activities.AddWineActivity
import org.json.JSONObject
import java.util.*

class Wine() {

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

    var barcodeST       : String = ""
    var nameST          : String = ""
    var wineryST        : String = ""
    var agingST         : String = ""
    var countryST       : String = ""
    var denOfOriginST   : String = ""
    var wineTypeST      : String = ""
    var yearST          : String = ""
    var volST           : String = ""
    var grapeTypeST     : String = ""
    var ecologicST      : String = ""
    var notesST         : String = ""
    var typeOfGrapeST   : String = ""
    var typeOfWineST    : String = ""
    var rantingST       : String = ""

    /*************************************************************************************
     * Busca los elementos de la view, y los asigna a cada una de las variables para inicializarlas
     ***********************************************************************************/
    fun searchAllElements(view: AddWineActivity) {
        barcode        = view.findViewById(R.id.edit_text_barCode)
        name           = view.findViewById(R.id.edit_text_name)
        winery         = view.findViewById(R.id.edit_text_winery)
        aging          = view.findViewById(R.id.text_view_spinner_aging)
        country        = view.findViewById(R.id.text_view_spinner_countries)
        denOfOrigin    = view.findViewById(R.id.edit_text_denomination_of_origin)
        wineType       = view.findViewById(R.id.text_view_spinner_wine_types)
        year           = view.findViewById(R.id.edit_text_year)
        vol            = view.findViewById(R.id.edit_text_alcohol)
        grapeType      = view.findViewById(R.id.edit_text_type_of_grape)
        ecologic       = view.findViewById(R.id.check_box_ecologic)
        notes          = view.findViewById(R.id.edit_text_notes)
        ranting        = view.findViewById(R.id.rating_bar_rating)
    }

    /*************************************************************************************
     * Obtiene los textos de cada uno de los elementos y los guardamos en strings
     ***********************************************************************************/
    fun setTextOnWine() {
        barcodeST       = barcode.text.toString().toUpperCase(Locale.ROOT)
        nameST          = name.text.toString().toUpperCase(Locale.ROOT)
        wineryST        = winery.text.toString().toUpperCase(Locale.ROOT)
        agingST         = aging.text.toString().toUpperCase(Locale.ROOT)
        countryST       = country.text.toString().toUpperCase(Locale.ROOT)
        denOfOriginST   = denOfOrigin.text.toString().toUpperCase(Locale.ROOT)
        wineTypeST      = wineType.text.toString().toUpperCase(Locale.ROOT)
        yearST          = year.text.toString().toUpperCase(Locale.ROOT)
        volST           = vol.text.toString().toUpperCase(Locale.ROOT)
        grapeTypeST     = grapeType.text.toString().toUpperCase(Locale.ROOT)
        ecologicST      = ecologic.isChecked.toString().toUpperCase(Locale.ROOT)
        notesST         = notes.text.toString().toUpperCase(Locale.ROOT)
        rantingST       = ranting.rating.toString().toUpperCase(Locale.ROOT)
    }


    /*************************************************************************************
     * Comprueba si vienen todos los campos rellenos
     ***********************************************************************************/
    fun filledFieldsCheck(): Boolean {
        return barcodeST != "" && nameST != "" && wineryST != "" && agingST != "" && countryST != "" &&
                denOfOriginST != "" && wineTypeST != "" && yearST != "" && volST != "" && grapeTypeST != ""
    }


    /*************************************************************************************
     * Dar valores a los parámetros desde los datos obtenidos de la BBDD
     * (en el caso de que exista el vino en la BBDD)
     ***********************************************************************************/
    fun fillAllFieldsFromBBDD(array: JSONObject) {
        barcode.setText(array.getString("BARCODE"))
        name.setText(array.getString("NAME"))
        vol.setText(array.getString("VOL"))
        year.setText(array.getString("YEAR"))
        winery.setText(array.getString("WINERY"))
        denOfOrigin.setText(array.getString("DENOMINATION_OF_ORIGIN"))
        if (array.getString("ECOLOGIC") == "TRUE") {
            ecologic.isChecked = true
        }
        aging.setText(array.getString("AGING"))
        country.setText(array.getString("COUNTRY"))
        grapeType.setText(array.getString("TYPE_OF_GRAPE"))
        wineType.setText(array.getString("TYPE_OF_WINE"))
    }


}