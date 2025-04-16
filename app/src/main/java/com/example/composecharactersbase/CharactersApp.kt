package com.example.composecharactersbase

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.composecharactersbase.ui.theme.Pokemon



@Preview
@Composable
fun CharacterApp() {
    CharacterListScreen()
}

@Composable
fun CharacterListScreen() {
    var pokemons by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    var favoriteIds by remember { mutableStateOf<Set<Int>>(emptySet()) }

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)


    LaunchedEffect(true) {
        val loadedFavorites = prefs.getStringSet("favorites", emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()
        favoriteIds = loadedFavorites

        val list = mutableListOf<Pokemon>()
        for (id in 1..20) {
            try {
                val result = RetrofitInstance.api.getPokemon(id)
                list.add(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        pokemons = list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pokemons) { pokemon ->
            CharacterCard(
                pokemon = pokemon,
                isFavorite = favoriteIds.contains(pokemon.id),
                onFavoriteClick = {
                    val updated = favoriteIds.toMutableSet()
                    if (updated.contains(pokemon.id)) {
                        updated.remove(pokemon.id)
                    } else {
                        updated.add(pokemon.id)
                    }
                    favoriteIds = updated


                    prefs.edit()
                        .putStringSet("favorites", updated.map { it.toString() }.toSet())
                        .apply()
                }
            )
        }
    }
}


@Composable
fun CharacterCard(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(pokemon.sprites.frontDefault),
                contentDescription = "Imagem do Pokémon",
                modifier = Modifier.size(100.dp)
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = pokemon.name.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
                Text(text = "Altura: ${pokemon.height}")
                Text(text = "Peso: ${pokemon.weight}")
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

