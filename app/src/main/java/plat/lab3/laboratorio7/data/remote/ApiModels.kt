package plat.lab3.laboratorio7.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

@Serializable
data class CharacterResponse(
    val info: PageInfo,
    val results: List<CharacterDto>
)

@Serializable
data class LocationResponse(
    val info: PageInfo,
    val results: List<LocationDto>
)