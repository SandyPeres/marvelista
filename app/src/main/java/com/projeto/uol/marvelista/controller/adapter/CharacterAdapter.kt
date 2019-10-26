package com.projeto.uol.marvelista.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projeto.uol.marvelista.R
import com.squareup.picasso.Picasso
import com.projeto.uol.marvelista.model.character.Character
import kotlinx.android.synthetic.main.item_character.view.*
import java.util.*


class CharacterAdapter(
  var personagens: MutableList<Character> = mutableListOf(),
  val clickListener: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.ItemHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ItemHolder {
    val view = LayoutInflater.from(parent.context).inflate(
      R.layout.item_character,
      parent,
      false
    ) as View
    return ItemHolder(view)
  }

  override fun getItemCount(): Int = personagens.size

  override fun onBindViewHolder(holder: ItemHolder, position: Int) {
    val itemCharacter = personagens[position]
    holder.bindingCharacter(itemCharacter, clickListener)
  }


  class ItemHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private var view: View = view
    private var personagem: Character? = null

    init {
      view.setOnClickListener(this)
    }

    override fun onClick(v: View) {

    }

    fun bindingCharacter(personagem: Character, clickListener: (Character) -> Unit) {
      this.personagem = personagem
      var uri = personagem.thumbnail.path
      uri += "."
      uri += personagem.thumbnail.extension
      Picasso.with(view.context).load(uri)
        .error(R.mipmap.ic_launcher)
        .into(view.character_image_recyclerview, object : com.squareup.picasso.Callback {
          override fun onSuccess() {
            if (view.progress_character_image != null) {
              view.progress_character_image.visibility = View.GONE
            }
          }

          override fun onError() {

          }
        })

      view.characterName.text = personagem.name
      view.characterName.textLocale = Locale.ENGLISH
      view.setOnClickListener { clickListener(personagem) }

    }
  }
}
