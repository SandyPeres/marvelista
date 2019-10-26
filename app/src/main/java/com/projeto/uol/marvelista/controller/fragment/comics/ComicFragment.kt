package com.projeto.uol.marvelista.controller.fragment.comics

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.projeto.uol.marvelista.R
import com.projeto.uol.marvelista.network.MarvelHandler
import com.projeto.uol.marvelista.model.comic.ComicContainer
import com.projeto.uol.marvelista.model.comic.ComicWrapper
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comic.view.*
import java.util.*

class ComicFragment : Fragment() {

  companion object {
    fun newInstance(id: Int): Fragment {
      val fragment = ComicFragment()
      val bundle = Bundle()
      bundle.putInt("index", id)
      fragment.arguments = bundle
      return fragment
    }

  }

  @SuppressLint("CheckResult")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val id = arguments!!.getInt("index")
    var url = ""

    (activity as AppCompatActivity).supportActionBar!!.hide()

    val view = inflater.inflate(R.layout.fragment_comic, container, false)

    MarvelHandler.service.getOneComic(id)
      .subscribeOn(Schedulers.io())
      .retry(10)
      .onErrorReturn {
        println("error : ${it.message}")
        ComicWrapper(ComicContainer(emptyList()))
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { wrapper ->
        val comic = wrapper.data.results.get(0)

        view.comic_title.text = comic.title
        view.comic_summary.text = comic.description
        view.comic_title.textLocale = Locale.ENGLISH
        view.comic_summary.textLocale = Locale.ENGLISH
        Picasso.with(view.context).load(comic.thumbnail.path + "." + comic.thumbnail.extension)
          .placeholder(R.mipmap.ic_launcher_round)
          .into(view.comic_image, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
              if (view.progress_image != null) {
                view.progress_image.visibility = View.GONE
              }
            }

            override fun onError() {
              TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
          })
        url = comic.urls.get(0).url
        view.invalidate()
      }

    val websiteButton = view.go_to_website as Button

    websiteButton.setOnClickListener {
      val uris = Uri.parse(url)
      val intents = Intent(Intent.ACTION_VIEW, uris)
      val b = Bundle()
      b.putBoolean("new_window", true)
      intents.putExtras(b)
      view.context.startActivity(intents)
    }

    return view
  }
}

