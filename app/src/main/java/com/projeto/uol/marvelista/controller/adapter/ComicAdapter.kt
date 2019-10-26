package com.projeto.uol.marvelista.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projeto.uol.marvelista.R
import com.projeto.uol.marvelista.model.comic.Comic
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_comic.view.*
import java.util.*

class ComicAdapter (var comics: MutableList<Comic> = mutableListOf(), val clickListener: (Comic) -> Unit): RecyclerView.Adapter<ComicAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_comic, parent, false) as View
      return ItemHolder(view)
    }

    override fun getItemCount(): Int = comics.size


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemComic = comics[position]
        holder.bindingComic(itemComic,clickListener)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var view: View = view
        private var comic: Comic? = null

        init {
          view.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

        fun bindingComic(comic: Comic, clickListener: (Comic) -> Unit) {
            this.comic = comic
            Picasso.with(view.context).load(comic.thumbnail.path+"."+comic.thumbnail.extension ).into(view.comicImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    if (view.progress_comic_image != null) {
                      view.progress_comic_image.visibility = View.GONE
                    }
                }

                override fun onError() {

                }
            })
            view.comicTitle.text = comic.title
            view.comicTitle.textLocale = Locale.ENGLISH
            view.setOnClickListener { clickListener(comic)}
        }
    }
}
