package plat.lab3.laboratorio7.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object CharacterList

@Serializable
data class CharacterDetail(val id: Int)