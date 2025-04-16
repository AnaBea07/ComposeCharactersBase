package com.example.composecharactersbase.ui.theme

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val sprites: Sprites
)

data class Sprites(
    val frontDefault: String
)

