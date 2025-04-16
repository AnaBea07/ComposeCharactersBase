package com.example.composecharactersbase


data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String
)
