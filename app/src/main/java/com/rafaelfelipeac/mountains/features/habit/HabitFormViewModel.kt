package com.rafaelfelipeac.mountains.features.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.core.platform.base.BaseViewModel
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.HabitRepository
import javax.inject.Inject

class HabitFormViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var habit: LiveData<Habit>? = null

    private var habits: LiveData<List<Habit>> = habitRepository.getHabits()

    var habitIdInserted: MutableLiveData<Long> = MutableLiveData()

    fun init(habitId: Long) {
        habit = habitRepository.getHabit(habitId)
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun getHabit(): LiveData<Habit>? {
        return habit
    }

    fun saveHabit(habit: Habit) {
        habitIdInserted.value = habitRepository.save(habit)
    }
}