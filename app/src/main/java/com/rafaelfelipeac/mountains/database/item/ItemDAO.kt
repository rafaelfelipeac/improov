package com.rafaelfelipeac.mountains.database.item

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.rafaelfelipeac.mountains.models.Item

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAll(): LiveData<List<Item>>

    @Insert(onConflict = REPLACE)
    fun save(item: Item): Long

    @Delete
    fun delete(item: Item)
}