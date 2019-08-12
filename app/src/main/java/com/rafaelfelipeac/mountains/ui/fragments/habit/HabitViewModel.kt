package com.rafaelfelipeac.mountains.ui.fragments.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class HabitViewModel @Inject constructor() : BaseViewModel() {

    private var habit: LiveData<Habit>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        getUser()
    }

    fun init(habitId: Long) {
        habit = habitRepository.getHabit(habitId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Habit
    fun getHabits(): LiveData<Habit>? {
        return habit
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)
    }
}