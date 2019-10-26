package com.projeto.uol.marvelista.controller.fragment.characters

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
import com.projeto.uol.marvelista.model.character.CharacterContainer
import com.projeto.uol.marvelista.model.character.CharacterWrapper
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_character.view.*
import java.util.*

class CharacterFragment : Fragment() {

    companion object {
        fun newInstance(id: Int): Fragment {
            val fragment =
              CharacterFragment()
            val bundle = Bundle()
            bundle.putInt("index", id)
            fragment.arguments = bundle
            return fragment
        }

    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id = arguments!!.getInt("index")
        var url = ""

        (activity as AppCompatActivity).supportActionBar!!.hide()

        val view = inflater.inflate(R.layout.fragment_character, container, false)

        MarvelHandler.service.getOneCharacter(id)
            .subscribeOn(Schedulers.io())
            .retry(10)
            .onErrorReturn {
                println("error : ${it.message}")
                CharacterWrapper(CharacterContainer(emptyList()))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                val character = wrapper.data.results.get(0)
                view.character_title.text = character.name
                view.character_title.textLocale = Locale.ENGLISH
                view.character_summary.textLocale = Locale.ENGLISH

                if (character.description.isEmpty()) view.character_summary.text =
                    "No available description" else view.character_summary.text = character.description

                Picasso.with(view.context).load(character.thumbnail.path + "." + character.thumbnail.extension)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(view.character_image, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            if (view.progress_image != null) {
                              view.progress_image.visibility = View.GONE
                            }
                        }

                        override fun onError() {

                        }
                    })
                url = character.urls.get(1).url

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


