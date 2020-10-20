package com.projects.winesmeller_v10

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_auth.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {

    val LOG_INFO    = "LOG_INFO"
    val LOG_ERROR   = "LOG_ERROR"
    val URL_SERVER  = "http://192.168.1.39:8080/developer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(findViewById(R.id.my_toolbar_auth))
        setTitle(R.string.activityTitle_auth)

            // Comprobar si saltar pantalla de autenticación
        startSession()

            // Listeners de los botones
        funSignUpAndLogIn()
    }

    private fun funSignUpAndLogIn() {

        idButton_SignUp.setOnClickListener {

            val email       = idEditText_Email.text.toString()
            val password    = idEditText_Password.text.toString()
            val checkBox= findViewById<CheckBox>(R.id.idCheckBox_SaveCredentials)

            accessSignUpAndLogIn("$URL_SERVER/userAuthentication.php?email=$email&password=$password", email, checkBox.isChecked )
        }

        idButton_Login.setOnClickListener {
            val email       = idEditText_Email.text.toString()
            val password    = idEditText_Password.text.toString()
            val checkBox= findViewById<CheckBox>(R.id.idCheckBox_SaveCredentials)

            accessSignUpAndLogIn("$URL_SERVER/userLogin.php?email=$email&password=$password", email, checkBox.isChecked)
        }
    }

    private fun accessSignUpAndLogIn(URL: String, email: String, check: Boolean) {

        var resultado       : Any   = ""
        var message         : Any   = ""

        val request = JsonObjectRequest( URL, null, { response ->
            Log.i(LOG_INFO, "Response is: $response")
            val jsonRQ : JSONObject = JSONObject(response.toString())
            resultado   = jsonRQ.get("Success")
            message     = jsonRQ.get("Message")

            if ( resultado.toString() == "OK") {
                Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                showHome(email, check)
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

    private fun showHome(email: String, check: Boolean) {

        val homeIntent = Intent(this, BoardActivity::class.java).apply {
            putExtra("email", email)
            putExtra("check", check)
        }
        startActivity(homeIntent)
    }

    private fun startSession() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE )
        val email = prefs.getString("email", null)

        if ( email != null ) {
            showHome(email, true)
        }
    }
}