package plat.lab3.laboratorio7.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object CharacterList

@Serializable
data class CharacterDetail(val id: Int)

@Serializable
object CharacterGraph

@Serializable
object LocationList

@Serializable
data class LocationDetail(val id:Int)

@Serializable
object LocationGraph

@Serializable
object Profiles