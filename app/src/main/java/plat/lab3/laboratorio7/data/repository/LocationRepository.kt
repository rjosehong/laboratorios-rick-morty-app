package plat.lab3.laboratorio7.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.local.LocationDao
import plat.lab3.laboratorio7.data.local.toEntity
import plat.lab3.laboratorio7.data.local.toModel
import plat.lab3.laboratorio7.data.remote.RickMortyApi

enum class LocationFilter {
    ALL,
    FAVORITES,
    NOT_FAVORITES
}

class LocationRepository(
    private val dao: LocationDao,
    private val api: RickMortyApi
) {

    fun observe(
        filter: LocationFilter
    ): Flow<List<Location>> {
        val source = when (filter) {
            LocationFilter.ALL ->
                dao.getAll()

            LocationFilter.FAVORITES ->
                dao.getFavorites()

            LocationFilter.NOT_FAVORITES ->
                dao.getNonFavorites()
        }

        return source.map { list ->
            list.map { it.toModel() }
        }
    }

    fun observeById(id: Int): Flow<Location?> =
        dao.getById(id).map { entity ->
            entity?.toModel()
        }

    suspend fun setFavorite(
        id: Int,
        isFavorite: Boolean
    ) = dao.updateFavorite(id, isFavorite)

    suspend fun hasData(): Boolean =
        dao.count() > 0

    suspend fun refreshFromApi() {
        val first = api.getLocations(1)
        val results = first.results.toMutableList()

        for (page in 2..first.info.pages) {
            delay(1_000L)
            results.addAll(
                api.getLocations(page).results
            )
        }

        dao.insertAll(
            results.map { it.toEntity() }
        )
    }

    suspend fun clear() =
        dao.deleteAll()
}