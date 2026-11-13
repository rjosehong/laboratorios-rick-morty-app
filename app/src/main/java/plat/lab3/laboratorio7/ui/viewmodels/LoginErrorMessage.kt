package plat.lab3.laboratorio7.ui.viewmodels

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.ContentConvertException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlinx.serialization.SerializationException

internal enum class LoginOperation {
    READ_LOCAL_DATA, DOWNLOAD_CHARACTERS, DOWNLOAD_LOCATIONS, SAVE_SESSION
}

internal fun loginErrorMessage(error: Exception, operation: LoginOperation): String {
    if (operation == LoginOperation.READ_LOCAL_DATA) {
        return "No se pudieron leer los datos guardados en el dispositivo."
    }
    if (operation == LoginOperation.SAVE_SESSION) {
        return "No se pudo guardar la sesión en el dispositivo."
    }
    return when (error) {
        is ResponseException -> when (error.response.status.value) {
            429 -> "El servidor recibió demasiadas solicitudes. Espera un momento e intenta de nuevo."
            in 500..599 -> "El servidor no pudo completar la solicitud (HTTP ${error.response.status.value}). Intenta más tarde."
            else -> "El servidor rechazó la solicitud (HTTP ${error.response.status.value})."
        }
        is UnknownHostException -> "No se pudo encontrar el servidor. Revisa tu conexión o el DNS de la red."
        is HttpRequestTimeoutException, is ConnectTimeoutException,
        is SocketTimeoutException -> "Se agotó el tiempo de espera del servidor. Intenta de nuevo."
        is SSLException -> "No se pudo establecer una conexión segura con el servidor. Revisa la fecha y hora del dispositivo."
        is SerializationException, is ContentConvertException,
        is NoTransformationFoundException -> "No se pudo interpretar la respuesta del servidor."
        is IOException -> "Se interrumpió la comunicación con el servidor. Intenta de nuevo."
        else -> "No se pudo completar la carga de datos. Intenta de nuevo."
    }
}
