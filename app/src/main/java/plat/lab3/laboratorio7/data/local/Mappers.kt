package plat.lab3.laboratorio7.data.local

import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.Location

fun CharacterEntity.toModel(): Character = Character (
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun LocationEntity.toModel(): Location = Location (
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun Location.toEntity(): LocationEntity = LocationEntity (
    id = id,
    name = name,
    type = type,
    dimension = dimension
)