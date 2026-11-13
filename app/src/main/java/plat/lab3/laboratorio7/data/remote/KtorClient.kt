package plat.lab3.laboratorio7.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createHttpClient(
    engine: HttpClientEngine
): HttpClient = HttpClient(engine) {

    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }

    install(HttpRequestRetry) {
        maxRetries = 3

        retryIf { _, response ->
            response.status == HttpStatusCode.TooManyRequests
        }

        retryOnExceptionIf { _, cause ->
            cause is ResponseException &&
                    cause.response.status == HttpStatusCode.TooManyRequests
        }

        delayMillis { retry ->
            val failedResponse =
                response ?: (cause as? ResponseException)?.response

            val retryAfterSeconds = failedResponse
                ?.headers
                ?.get(HttpHeaders.RetryAfter)
                ?.toLongOrNull()
                ?.coerceAtLeast(0L)
                ?: 30L

            val backoffMillis = 30_000L * (1L shl (retry - 1))

            maxOf(
                backoffMillis,
                retryAfterSeconds * 1_000L
            )
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }
}

object KtorClient {

    private val client: HttpClient by lazy {
        createHttpClient(Android.create())
    }

    val api: RickMortyApi by lazy {
        KtorRickMortyApi(client)
    }
}