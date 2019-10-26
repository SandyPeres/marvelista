package com.projeto.uol.marvelista.controller.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.projeto.uol.marvelista.R
import java.lang.Thread.sleep

class SplashScreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    this.supportActionBar!!.hide()

    Thread {
      sleep(5000)
      this.navigateToContent()
    }.start()
  }

  fun navigateToContent(){
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    this.finish()
  }
}
