package com.d4rk.android.libs.apptoolkit.data.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * A class responsible for creating and configuring a Ktor HttpClient.
 *
 * This class provides a centralized way to create a pre-configured HttpClient instance with
 * common settings such as JSON content negotiation, request timeouts, and default request headers.
 */
class KtorClient {

    private val requestTimeout : Long = 10_000L

    /**
     * Creates and configures an [HttpClient] for making network requests.
     *
     * This function sets up the client with the following:
     * - **Android Engine:** Uses the Android engine for network operations.
     * - **Content Negotiation:** Configures JSON serialization and deserialization with pretty printing, lenient parsing and ignoring unknown keys.
     * - **Timeout Configuration:** Sets request, connect and socket timeouts.
     * - **Default Request Configuration:** Sets default content type and accept headers to JSON.
     *
     * @return An [HttpClient] instance ready for use.
     */
    fun createClient() : HttpClient {
        return HttpClient(engineFactory = Android) {
            install(plugin = ContentNegotiation) {
                json(json = Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(plugin = HttpTimeout) {
                requestTimeoutMillis = requestTimeout
                connectTimeoutMillis = requestTimeout
                socketTimeoutMillis = requestTimeout
            }

            install(plugin = DefaultRequest) {
                contentType(type = ContentType.Application.Json)
                accept(contentType = ContentType.Application.Json)
            }
        }
    }
}