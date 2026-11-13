package plat.lab3.laboratorio7.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM locations ORDER BY id")
    fun getAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isFavorite = 1 ORDER BY id")
    fun getFavorites(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isFavorite = 0 ORDER BY id")
    fun getNonFavorites(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getById(id: Int): Flow<LocationEntity?>

    @Query("UPDATE locations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun count(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}