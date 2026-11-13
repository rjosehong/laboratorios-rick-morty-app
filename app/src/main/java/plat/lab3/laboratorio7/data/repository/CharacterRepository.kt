package plat.lab3.laboratorio7.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.local.CharacterDao
import plat.lab3.laboratorio7.data.local.toEntity
import plat.lab3.laboratorio7.data.local.toModel
import plat.lab3.laboratorio7.data.remote.RickMortyApi

class CharacterRepository(
    private val dao: CharacterDao,
    private val api: RickMortyApi
) {

    fun observeAll(): Flow<List<Character>> =
        dao.getAll().map { list ->
            list.map { it.toModel() }
        }

    fun observeFavorites(): Flow<List<Character>> =
        dao.getFavorites().map { list ->
            list.map { it.toModel() }
        }

    fun observeById(id: Int): Flow<Character?> =
        dao.getById(id).map { entity ->
            entity?.toModel()
        }

    suspend fun add(character: Character) {
        dao.insert(
            character.copy(id = 0).toEntity()
        )
    }

    suspend fun update(character: Character) =
        dao.update(character.toEntity())

    suspend fun delete(id: Int) =
        dao.deleteById(id)

    suspend fun setFavorite(
        id: Int,
        isFavorite: Boolean
    ) = dao.updateFavorite(id, isFavorite)

    suspend fun hasData(): Boolean =
        dao.count() > 0

    suspend fun refreshFromApi() {
        val first = api.getCharacters(1)
        val results = first.results.toMutableList()

        for (page in 2..first.info.pages) {
            delay(1_000L)
            results.addAll(
                api.getCharacters(page).results
            )
        }

        dao.insertAll(
            results.map { it.toEntity() }
        )
    }

    suspend fun clear() =
        dao.deleteAll()
}