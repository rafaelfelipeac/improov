package com.rafaelfelipeac.improov.features.goal.domain.usecase.goal

import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.repository.GoalRepository
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goal: Goal) {
        goalRepository.delete(goal)
    }
}
