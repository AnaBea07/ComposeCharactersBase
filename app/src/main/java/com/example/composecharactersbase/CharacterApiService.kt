package com.example.composecharactersbase

import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): Pokemon

}