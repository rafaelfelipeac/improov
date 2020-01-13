package com.rafaelfelipeac.improov.features.commons

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
data class Goal (
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var value: Float = 0F,
    var type: GoalType = GoalType.GOAL_NONE,
    var done: Boolean = false,
    var divideAndConquer: Boolean = false,
    var singleValue: Float = 0F,
    var bronzeValue: Float = 0F,
    var silverValue: Float = 0F,
    var goldValue: Float = 0F,
    var incrementValue: Float = 0F,
    var decrementValue: Float = 0F,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var archiveDate: Date? = null,
    var date: Date? = null
) : Serializable, GoalHabit()

enum class GoalType { GOAL_LIST, GOAL_COUNTER, GOAL_FINAL, GOAL_NONE }
