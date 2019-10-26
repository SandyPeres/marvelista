package com.projeto.uol.marvelista.model.comic

import com.projeto.uol.marvelista.model.character.Thumbnail
import com.projeto.uol.marvelista.model.character.Url

data class Comic (val id: Int, val title: String, val description: String,
                  val thumbnail: Thumbnail, val urls: List<Url>, val images : List<Image>)
