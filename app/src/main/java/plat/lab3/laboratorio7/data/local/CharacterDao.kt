package plat.lab3.laboratorio7.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity): Long

    @Update
    suspend fun update(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM characters ORDER BY id")
    fun getAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id")
    fun getFavorites(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity?>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun count(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}