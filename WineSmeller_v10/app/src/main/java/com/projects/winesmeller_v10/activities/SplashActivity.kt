package com.projects.winesmeller_v10.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.projects.winesmeller_v10.others.Constants
import com.projects.winesmeller_v10.R
import com.projects.winesmeller_v10.others.Utilities

class SplashActivity : AppCompatActivity() {

    lateinit var title : ImageView
    lateinit var letters : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

            //Elimina barra de notificaciones
//        Utilities.noShowNotificationBar(this.window)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Creando animación inicial
        val animationTitle = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        title = findViewById(R.id.splash_title_text)
        title.startAnimation(animationTitle)

        val animationLetters = AnimationUtils.loadAnimation(this, R.anim.fromtop)
        letters = findViewById(R.id.splash_letters_text)
        letters.startAnimation(animationLetters)

            //Llamamos al método postDelayed
        Handler(Looper.getMainLooper()).postDelayed({
                // Comprobar si saltar pantalla de autenticación
            startSession()
        }, Constants.TIME_SPLASH_ACTIVITY)
    }

        // Comrpueba si se cerró la app guardando los credenciales o no.
    private fun startSession() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE )
        val email = prefs.getString("email", null)

        if ( email != null ) {
            showHome(email, true)
            finish()
        } else {
            Utilities.showAuth(this)
            finish()
        }
    }

    private fun showHome(email: String, check: Boolean) {

        val homeIntent = Intent(this, BoardActivity::class.java).apply {
            putExtra("email", email)
            putExtra("check", check)
        }
        startActivity(homeIntent)
    }
}