package com.rafaelfelipeac.readmore.ui.fragments.goalform

import com.rafaelfelipeac.readmore.app.App
import com.rafaelfelipeac.readmore.database.goal.GoalDAO
import com.rafaelfelipeac.readmore.database.goal.GoalRepository
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseViewModel

class GoalFormViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!

    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun saveGoal(goal: Goal) {
        goalRepository.insert(goal)
    }
}