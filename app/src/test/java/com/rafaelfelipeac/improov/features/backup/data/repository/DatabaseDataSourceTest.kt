package com.rafaelfelipeac.improov.features.backup.data.repository

import com.google.gson.Gson
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoalDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoricDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createItemDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createJson
import com.rafaelfelipeac.improov.base.DataProviderTest.getDate
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.DatabaseDataSource
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DatabaseDataSourceTest {

    @Mock
    internal lateinit var goalDao: GoalDao

    @Mock
    internal lateinit var historicDao: HistoricDao

    @Mock
    internal lateinit var itemDao: ItemDao

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var databaseDataSource: DatabaseDataSource

    @Before
    fun setup() {
        databaseDataSource = DatabaseDataSource(goalDao, historicDao, itemDao, preferences, Gson())
    }

    @Test
    fun `GIVEN information in Dao and Preferences WHEN export is called THEN return a json for that information`() {
        runBlocking {
            // given
            val goals = listOf(createGoalDataModel())
            val historics = listOf(createHistoricDataModel())
            val items = listOf(createItemDataModel())
            val language = ""
            val welcome = false
            val name = ""
            val firstTimeAdd = false
            val firstTimeList = false

            val json = createJson(
                goals,
                historics,
                items,
                language,
                welcome,
                name,
                firstTimeAdd,
                firstTimeList
            )

            given(goalDao.getAll())
                .willReturn(goals)
            given(historicDao.getAll())
                .willReturn(historics)
            given(itemDao.getAll())
                .willReturn(items)
            given(preferences.firstTimeAdd)
                .willReturn(firstTimeAdd)
            given(preferences.firstTimeList)
                .willReturn(firstTimeList)
            given(preferences.name)
                .willReturn(name)
            given(preferences.language)
                .willReturn(language)
            given(preferences.welcome)
                .willReturn(welcome)

            // when
            val result = databaseDataSource.export()

            // then
            result equalTo json
        }
    }

    @Test
    fun `GIVEN a json with information for Dao and Preferences WHEN import is called THEN return true`() {
        runBlocking {
            // given
            val goals = listOf(createGoalDataModel())
            val historics = listOf(createHistoricDataModel())
            val items = listOf(createItemDataModel())
            val language = ""
            val welcome = false
            val name = ""
            val firstTimeAdd = false
            val firstTimeList = false

            val json = createJson(
                goals,
                historics,
                items,
                language,
                welcome,
                name,
                firstTimeAdd,
                firstTimeList
            )

            // when
            val result = databaseDataSource.import(json)

            // then
            result equalTo true
        }
    }

    @Test
    fun `GIVEN a json with invalid information WHEN import is called THEN return a false Boolean value`() {
        runBlocking {
            // given
            val json = "invalid json"

            // when
            val result = databaseDataSource.import(json)

            // then
            result equalTo false
        }
    }

    @Test
    fun `GIVEN a Long value WHEN getExportDate is called THEN return the same Long value`() {
        runBlocking {
            // given
            val date = getDate()

            given(preferences.exportDate)
                .willReturn(date)

            // when
            val result = databaseDataSource.getExportDate()

            // then
            result equalTo date
        }
    }

    @Test
    fun `GIVEN a Long value WHEN getImportDate is called THEN return the same Long value`() {
        runBlocking {
            // given
            val date = getDate()

            given(preferences.importDate)
                .willReturn(date)

            // when
            val result = databaseDataSource.getImportDate()

            // then
            result equalTo date
        }
    }
}
