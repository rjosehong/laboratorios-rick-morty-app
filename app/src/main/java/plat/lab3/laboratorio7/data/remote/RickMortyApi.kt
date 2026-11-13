package plat.lab3.laboratorio7.data.remote

interface RickMortyApi {
    suspend fun getCharacters(page: Int): CharacterResponse

    suspend fun getLocations(page: Int): LocationResponse
}
