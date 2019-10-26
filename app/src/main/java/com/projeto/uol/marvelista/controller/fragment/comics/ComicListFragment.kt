package com.projeto.uol.marvelista.controller.fragment.comics

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.projeto.uol.marvelista.controller.activity.MainActivity
import com.projeto.uol.marvelista.R
import com.projeto.uol.marvelista.network.MarvelHandler
import com.projeto.uol.marvelista.model.comic.Comic
import com.projeto.uol.marvelista.model.comic.ComicContainer
import com.projeto.uol.marvelista.model.comic.ComicWrapper
import com.projeto.uol.marvelista.controller.adapter.ComicAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.recycler_view.view.*


class ComicListFragment : Fragment() {

  private lateinit var linearLayoutManager: LinearLayoutManager
  private lateinit var adapter: ComicAdapter
  private var pesquisa = ""
  private var results = mutableListOf<Comic>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    setHasOptionsMenu(true)
    (activity as AppCompatActivity).supportActionBar!!.show()

    val view = inflater.inflate(R.layout.recycler_view, container, false)

    linearLayoutManager = LinearLayoutManager(activity)
    view.my_recycler_view.layoutManager = linearLayoutManager

    if (results.size == 0 && pesquisa.isEmpty()) {
      getAllComics()
      adapter =
        ComicAdapter(clickListener = { comic: Comic ->
          itemClicked(comic)
        })
    } else if (savedInstanceState != null) {
      var oldQuery = savedInstanceState.getString("search")
      getComicsByName(oldQuery)
      adapter =
        ComicAdapter(clickListener = { comic: Comic ->
          itemClicked(comic)
        })
    } else {
      adapter = ComicAdapter(
        results,
        clickListener = { comic: Comic -> itemClicked(comic) })
    }

    view.my_recycler_view.adapter = adapter

    view.my_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)


        if (!recyclerView.canScrollVertically(1) && adapter.comics.size >= 20) {
          if (pesquisa.isEmpty()) {
            getAllComics(adapter.comics.size)
          } else {
            getComicsByName(pesquisa, adapter.comics.size)
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
            adapter.comics.clear()
            results.clear()
            getAllComics()
          }
          true
        }
        true
      }
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
          if (query != null) {
            pesquisa = query
            adapter.comics.clear()
            results.clear()
            adapter.notifyDataSetChanged()
            recyclerVisibility(true)

            getComicsByName(query)
          }
          searchView.clearFocus()
          return true
        }

        override fun onQueryTextChange(newQuery: String?): Boolean {
          if (newQuery.isNullOrBlank()) {
            adapter.comics.clear()
            results.clear()
            getAllComics()
          }
          return true
        }
      })

    }

    super.onPrepareOptionsMenu(menu)
  }

  private fun itemClicked(comic: Comic) {
    (activity as MainActivity).goToFragment(ComicFragment.newInstance(comic.id))

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("search", pesquisa)
  }

  @SuppressLint("CheckResult")
  fun getComicsByName(query: String, offset: Int = 0) {
    MarvelHandler.service.getComicsByTitleStartingWith(query.toLowerCase(), offset)
      .subscribeOn(Schedulers.io())
      .retry(10)
      .onErrorReturn {
        println("error : ${it.message}")
        ComicWrapper(ComicContainer(emptyList()))
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { wrapper ->
        adapter.comics.addAll(wrapper.data.results)
        results.addAll(wrapper.data.results)
        adapter.notifyDataSetChanged()

        if (results.isEmpty()) {
          view?.empty_view?.text = "Não foi possível encontrar ${pesquisa}."
          recyclerVisibility(false)
        }
      }
  }

  @SuppressLint("CheckResult")
  fun getAllComics(offset: Int = 0) {
    MarvelHandler.service.getAllComic(offset)
      .subscribeOn(Schedulers.io())
      .retry(10)
      .onErrorReturn {
        ComicWrapper(ComicContainer(emptyList()))
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { wrapper ->
        adapter.comics.addAll(wrapper.data.results)
        results.addAll(wrapper.data.results)
        adapter.notifyDataSetChanged()
      }
  }

  fun recyclerVisibility(bool: Boolean) {
    if (bool) view?.my_recycler_view?.visibility =
      View.VISIBLE else view?.my_recycler_view?.visibility = View.GONE
  }
}
