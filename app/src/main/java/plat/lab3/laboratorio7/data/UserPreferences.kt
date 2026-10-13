package plat.lab3.laboratorio7.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    private val nameKey = stringPreferencesKey("name")

    val name: Flow<String?> = context.dataStore.data.map {prefs -> prefs[nameKey]}

    suspend fun saveName(name:String){
        context.dataStore.edit {prefs -> prefs[nameKey] = name}
    }

    suspend fun clearName(){
        context.dataStore.edit {prefs -> prefs.remove(nameKey)}
    }
}