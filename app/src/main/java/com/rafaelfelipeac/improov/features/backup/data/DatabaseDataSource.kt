package com.rafaelfelipeac.improov.features.backup.data

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.model.Database
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import java.util.Calendar

class DatabaseDataSource @Inject constructor(
    private val goalDao: GoalDao,
    private val historicDao: HistoricDao,
    private val itemDao: ItemDao,
    private val preferences: Preferences,
    private val gson: Gson
) : DatabaseRepository {

    override suspend fun export(): String {
        return withContext(Dispatchers.IO) {
            try {
                val json = gson.toJson(
                    Database(
                        preferences.language,
                        preferences.welcome,
                        preferences.name,
                        preferences.firstTimeList,
                        preferences.firstTimeAdd,
                        goalDao.getAll(),
                        itemDao.getAll(),
                        historicDao.getAll()
                    )
                )

                preferences.exportDate = Calendar.getInstance().timeInMillis

                json
            } catch (e: JsonParseException) {
                e.printStackTrace()

                ""
            }
        }
    }

    override suspend fun import(databaseBackup: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val database = gson.fromJson(databaseBackup, Database::class.java)

                goalDao.getAll().forEach { goalDao.delete(it) }
                itemDao.getAll().forEach { itemDao.delete(it) }
                historicDao.getAll().forEach { historicDao.delete(it) }

                if (database.goals != null) {
                    database.goals.forEach { goalDao.save(it) }
                }

                if (database.items != null) {
                    database.items.forEach { itemDao.save(it) }
                }

                if (database.historics != null) {
                    database.historics.forEach { historicDao.save(it) }
                }

                preferences.language = database.language
                preferences.welcome = database.welcome
                preferences.name = database.name
                preferences.firstTimeList = database.firstTimeList
                preferences.firstTimeAdd = database.firstTimeAdd

                preferences.importDate = Calendar.getInstance().timeInMillis

                true
            } catch (e: JsonParseException) {
                e.printStackTrace()

                false
            }
        }
    }

    override suspend fun getExportDate(): Long {
        return withContext(Dispatchers.IO) {
            preferences.exportDate
        }
    }

    override suspend fun getImportDate(): Long {
        return withContext(Dispatchers.IO) {
            preferences.importDate
        }
    }
}
