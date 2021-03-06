package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModelMapper
import com.rafaelfelipeac.improov.features.goal.data.ItemDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ItemDataSourceTest {

    @Mock
    internal lateinit var itemDao: ItemDao

    @Mock
    internal lateinit var itemDataModelMapper: ItemDataModelMapper

    private lateinit var itemDataSource: ItemDataSource

    private val itemId = 1L

    @Before
    fun setup() {
        itemDataSource = ItemDataSource(itemDao, itemDataModelMapper)
    }

    @Test
    fun `GIVEN a itemId WHEN getItem is called THEN return a item with the specific itemId`() {
        runBlocking {
            // given
            val item = createItem(itemId).let { itemDataModelMapper.mapReverse(it) }
            given(itemDao.get(itemId))
                .willReturn(item)

            // when
            val result = itemDataSource.getItem(itemId)

            // then
            result equalTo item
        }
    }

    @Test
    fun `GIVEN a list of items WHEN getItems is called THEN return a list with the same items`() {
        runBlocking {
            // given
            val items = listOf(createItem(), createItem(), createItem())
                .let { itemDataModelMapper.mapListReverse(it) }
            given(itemDao.getAll())
                .willReturn(items)

            // when
            val result = itemDataSource.getItems()

            // then
            result equalTo items
        }
    }

    @Test
    fun `GIVEN a item WHEN save is called THEN return the itemId as a confirmation`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            given(itemDao.save(item.let { itemDataModelMapper.mapReverse(it) }))
                .willReturn(itemId)

            // when
            val result = itemDataSource.save(item)

            // then
            result equalTo itemId
        }
    }

    @Test
    fun `GIVEN a item with a specific itemId WHEN delete is called THEN return just a Unit value`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            val itemReverse = item.let { itemDataModelMapper.mapReverse(it) }
            doNothing().`when`(itemDao).delete(itemReverse)

            // when
            val result = itemDataSource.delete(item)

            // then
            result equalTo Unit
        }
    }
}
