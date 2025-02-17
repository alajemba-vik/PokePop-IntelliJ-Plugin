package com.alaje.intellijplugins.pokepop.image

import com.alaje.intellijplugins.pokepop.utils.ImageTypeExt

class PokemonImageLoader {

    var isLoaded = false
        private set

    private val mutablePokemonImagePaths = mutableListOf<String>()
    val pokemonImagePaths: List<String> = mutablePokemonImagePaths

    fun loadImages(): List<String> {
        mutablePokemonImagePaths.clear()
        isLoaded = false

        mutablePokemonImagePaths.addAll(generateFileNames())

        println("Loaded ${mutablePokemonImagePaths.size} images")

        mutablePokemonImagePaths.shuffle()

        isLoaded = true
        return mutablePokemonImagePaths
    }

    // TODO - Add a more reliable way to load images from a directory
    private fun generateFileNames(): List<String> {
        var currentPokemon = 0
        val list = mutableListOf<String>()

        while (true) {
            val fullName = "$pokemonDir$pokemonImagePrefix$currentPokemon${ImageTypeExt.GIF.value}"
            val existingPath = javaClass.getResource(fullName)?.path

            if (existingPath?.endsWith(ImageTypeExt.GIF.value) != true) {
                break
            }

            list.add(fullName)
            currentPokemon++
        }

        return list
    }
}


private const val pokemonDir = "/pokemons/"
private const val pokemonImagePrefix = "pokemon"