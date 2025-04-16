package com.example.composecharactersbase

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    var pokemons = mutableStateOf<List<Pokemon>>(emptyList())
        private set

    init {
        fetchPokemons()
    }

    private fun fetchPokemons() {
        viewModelScope.launch {
            try {
                val list = mutableListOf<Pokemon>()
                for (id in 1..20) {
                    val result = RetrofitInstance.api.getPokemon(id)
                    list.add(result)
                }
                pokemons.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}