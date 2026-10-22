package plat.lab3.laboratorio7.data.local

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import plat.lab3.laboratorio7.data.CharacterDb
import plat.lab3.laboratorio7.data.LocationDb

suspend fun AppDatabase.syncIfNeeded() {
    val CharacterDao = characterDao()
    val LocationDao = locationDao()

    if (CharacterDao.count() > 0 && LocationDao.count() > 0) return

    delay(4000)
    withTransaction {
        CharacterDao.insertAll(CharacterDb().getAllCharacters().map { it.toEntity() })
        LocationDao.insertAll(LocationDb().getAllLocations().map { it.toEntity() })
    }
}