package plat.lab3.laboratorio7.data.local

import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.remote.CharacterDto
import plat.lab3.laboratorio7.data.remote.LocationDto

fun CharacterDto.toEntity() = CharacterEntity(
    id = id, name = name, status = status,
    species = species, gender = gender, image = image
)

fun LocationDto.toEntity() = LocationEntity(
    id = id, name = name, type = type, dimension = dimension
)

fun CharacterEntity.toModel() = Character(
    id = id, name = name, status = status, species = species,
    gender = gender, image = image, isFavorite = isFavorite
)

fun LocationEntity.toModel() = Location(
    id = id, name = name, type = type, dimension = dimension, isFavorite = isFavorite
)

fun Character.toEntity() = CharacterEntity(
    id = id, name = name, status = status, species = species,
    gender = gender, image = image, isFavorite = isFavorite
)

fun Location.toEntity() = LocationEntity(
    id = id, name = name, type = type, dimension = dimension, isFavorite = isFavorite
)