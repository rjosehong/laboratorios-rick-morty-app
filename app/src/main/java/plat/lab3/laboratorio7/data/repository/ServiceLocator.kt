package plat.lab3.laboratorio7.data.repository

import android.content.Context
import plat.lab3.laboratorio7.data.local.AppDatabase
import plat.lab3.laboratorio7.data.remote.KtorClient

object ServiceLocator {
    fun characterRepository(context: Context) = CharacterRepository(
        AppDatabase.getInstance(context).characterDao(),
        KtorClient.api
    )

    fun locationRepository(context: Context) = LocationRepository(
        AppDatabase.getInstance(context).locationDao(),
        KtorClient.api
    )
}
