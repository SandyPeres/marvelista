package com.projeto.uol.marvelista.controller.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.projeto.uol.marvelista.R
import com.projeto.uol.marvelista.controller.fragment.characters.CharacterListFragment
import com.projeto.uol.marvelista.controller.fragment.comics.ComicListFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    this.supportActionBar!!.hide()
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_content, CharacterListFragment())
    transaction.commit()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    this.supportActionBar!!.setDisplayShowTitleEnabled(false)
    menuInflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.comic_menu -> {
        goToFragment(ComicListFragment())
      }
      R.id.character_menu -> {
        goToFragment(CharacterListFragment())
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  fun goToFragment(frag: Fragment): Boolean {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_content, frag)
    transaction.addToBackStack(null)
    transaction.commit()
    return true
  }


}








