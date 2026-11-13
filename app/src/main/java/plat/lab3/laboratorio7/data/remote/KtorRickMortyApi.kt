package plat.lab3.laboratorio7.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorRickMortyApi(private val client: HttpClient) : RickMortyApi {
    override suspend fun getCharacters(page: Int): CharacterResponse =
        client.get("https://rickandmortyapi.com/api/character") {
            parameter("page", page)
        }.body()

    override suspend fun getLocations(page: Int): LocationResponse =
        client.get("https://rickandmortyapi.com/api/location") {
            parameter("page", page)
        }.body()
}
