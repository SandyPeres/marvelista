package com.projeto.uol.marvelista.controller.fragment.characters

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.projeto.uol.marvelista.controller.activity.MainActivity
import com.projeto.uol.marvelista.R
import com.projeto.uol.marvelista.network.MarvelHandler
import com.projeto.uol.marvelista.model.character.Character
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.recycler_view.view.*
import android.support.v7.widget.RecyclerView
import com.projeto.uol.marvelista.model.character.CharacterContainer
import com.projeto.uol.marvelista.model.character.CharacterWrapper
import com.projeto.uol.marvelista.controller.adapter.CharacterAdapter
import kotlinx.android.synthetic.main.activity_main.*


class CharacterListFragment : Fragment() {

  private lateinit var linearLayoutManager: LinearLayoutManager
  private lateinit var adapter: CharacterAdapter
  private var pesquisa: String = ""
  private var results = mutableListOf<Character>()


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    view?.my_recycler_view?.clearFocus()

    setHasOptionsMenu(true)
    (activity as AppCompatActivity).supportActionBar!!.show()
    val view = inflater.inflate(R.layout.recycler_view, container, false)

    linearLayoutManager = LinearLayoutManager(activity)
    view.my_recycler_view.layoutManager = linearLayoutManager


    if (results.size == 0) {
      getAllCharacters()
      adapter =
        CharacterAdapter(clickListener = { character: Character -> itemClicked(character) })
    } else if (savedInstanceState != null) {
      pesquisa = savedInstanceState.getString("search")
      getCharactersNameStartBy(pesquisa, view = view)
      adapter =
        CharacterAdapter(clickListener = { character: Character -> itemClicked(character) })
    } else {
      adapter =
        CharacterAdapter(
          results,
          clickListener = { character: Character -> itemClicked(character) })
    }

    view.my_recycler_view.adapter = adapter

    view.my_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)


        if (!recyclerView.canScrollVertically(1) && adapter.personagens.size >= 20) {
          if (pesquisa.isEmpty()) {
            getAllCharacters(adapter.personagens.size)
          } else {
            getCharactersNameStartBy(pesquisa, adapter.personagens.size, view)
          }

        }
      }
    })
    return view
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    val searchItem = menu.findItem(R.id.search)
    if (searchItem != null) {
      val searchView = searchItem.actionView as SearchView

      searchView.setOnCloseListener {
        SearchView.OnCloseListener {
          if (searchView.query.isNullOrBlank()) {
            adapter.personagens.clear()
            results.clear()
            getAllCharacters()
          }
          true
        }
        true
      }
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
          if (query != null) {
            pesquisa = query
            adapter.personagens.clear()
            results.clear()
            adapter.notifyDataSetChanged()
            recyclerVisibility(true)
            getCharactersNameStartBy(query, view = view)
          }

          searchView.clearFocus()
          return true
        }

        override fun onQueryTextChange(newQuery: String?): Boolean {
          if (newQuery.isNullOrBlank()) {
            adapter.personagens.clear()
            results.clear()
            getAllCharacters()
          }
          return true

        }


      })

    }

    super.onPrepareOptionsMenu(menu)
  }

  private fun itemClicked(character: Character) {

    (activity as MainActivity).goToFragment(CharacterFragment.newInstance(character.id))

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("search", pesquisa)
  }

  @SuppressLint("CheckResult")
  fun getCharactersNameStartBy(query: String, offset: Int = 0, view: View?) {
    MarvelHandler.service.getCharactersByNameStartingWith(query.toLowerCase(), offset)
      .subscribeOn(Schedulers.io())
      .retry(10)
      .onErrorReturn {
        println("error : ${it.message}")
        CharacterWrapper(CharacterContainer(emptyList()))
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ wrapper ->
        adapter.personagens.addAll(wrapper.data.results)
        results.addAll(wrapper.data.results)
        adapter.notifyDataSetChanged()

        if (results.isEmpty()) {
          view?.empty_view?.text = "Não foi possível encontrar ${pesquisa}."
          recyclerVisibility(false)
        }

      }, { println("Error: ${this}") })


  }

  @SuppressLint("CheckResult")
  fun getAllCharacters(offset: Int = 0) {
    MarvelHandler.service.getAllCharacters(offset)
      .subscribeOn(Schedulers.io())
      .retry(10)
      .onErrorReturn {
        CharacterWrapper(CharacterContainer(emptyList()))
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ wrapper ->
        adapter.personagens.addAll(wrapper.data.results)
        results.addAll(wrapper.data.results)
        adapter.notifyDataSetChanged()
      }, {
      })
  }

  fun recyclerVisibility(bool: Boolean) {
    if (bool) view?.my_recycler_view?.visibility =
      View.VISIBLE else view?.my_recycler_view?.visibility = View.GONE
  }


}
