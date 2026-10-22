package plat.lab3.laboratorio7.data.local

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val isFavorite: Boolean = true

) {
}