package com.projeto.uol.marvelista.network

import com.projeto.uol.marvelista.model.character.CharacterWrapper
import com.projeto.uol.marvelista.model.comic.ComicWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelServiceInterface {
    @GET("characters")
    fun getAllCharacters(@Query("offset") offset : Int): Single<CharacterWrapper>

    @GET("characters")
    fun getCharactersByNameStartingWith(@Query("nameStartsWith") nameStart: String, @Query("offset") offset : Int): Single<CharacterWrapper>

    @GET("characters/{id}")
    fun getOneCharacter(@Path("id") searchVal :Int): Single<CharacterWrapper>

    @GET("comics")
    fun getAllComic(@Query("offset") offset : Int): Single<ComicWrapper>

    @GET("comics")
    fun getComicsByTitleStartingWith(@Query("titleStartsWith")nameStart: String, @Query("offset") offset : Int): Single<ComicWrapper>

    @GET("comics/{id}")
    fun getOneComic(@Path("id")searchVal: Int):Single<ComicWrapper>
}
