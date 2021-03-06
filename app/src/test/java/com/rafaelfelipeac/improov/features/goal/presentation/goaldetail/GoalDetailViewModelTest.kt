package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.SaveHistoricUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.SaveItemUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoalDetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveGoalUseCase = mock(SaveGoalUseCase::class.java)
    private var mockGetGoalUseCase = mock(GetGoalUseCase::class.java)
    private var mockSaveItemUseCase = mock(SaveItemUseCase::class.java)
    private var mockGetItemListUseCase = mock(GetItemListUseCase::class.java)
    private var mockSaveHistoricUseCase = mock(SaveHistoricUseCase::class.java)
    private var mockGetHistoricListUseCase = mock(GetHistoricListUseCase::class.java)

    private lateinit var goalDetailViewModel: GoalDetailViewModel

    @Before
    fun setup() {
        goalDetailViewModel = GoalDetailViewModel(
            mockSaveGoalUseCase,
            mockGetGoalUseCase,
            mockSaveItemUseCase,
            mockGetItemListUseCase,
            mockSaveHistoricUseCase,
            mockGetHistoricListUseCase
        )
    }

    @Test
    fun `GIVEN saveGoal is successful WHEN saveGoal is called THEN goalId is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase(goal) })
            .willReturn(goalId)

        // when
        goalDetailViewModel.saveGoal(goal)

        // then
        runBlocking {
            goalDetailViewModel.savedGoal.first() equalTo goalId
        }
    }

    @Test
    fun `GIVEN getGoal is successful WHEN loadData is called THEN return the specific goal`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockGetGoalUseCase(goalId) })
            .willReturn(goal)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        runBlocking {
            goalDetailViewModel.goal.first() equalTo goal
        }
    }

    @Test
    fun `GIVEN saveItem is successful WHEN saveItem is called THEN itemId is returned`() {
        // given
        val itemId = 1L
        val item = createItem(itemId)

        given(runBlocking { mockSaveItemUseCase(item) })
            .willReturn(itemId)

        // when
        goalDetailViewModel.saveItem(item)

        // then
        runBlocking {
            goalDetailViewModel.savedItem.first() equalTo itemId
        }
    }

    @Test
    fun `GIVEN getItemList is successful WHEN loadData is called THEN return a list of items`() {
        // given
        val goalId = 1L
        val items = listOf(
            createItem(itemId = 1, goalId = goalId),
            createItem(itemId = 2, goalId = goalId),
            createItem(itemId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetItemListUseCase(goalId) })
            .willReturn(items)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        runBlocking {
            goalDetailViewModel.items.first() equalTo items
        }
    }

    @Test
    fun `GIVEN saveHistoric is successful WHEN saveHistoric is called THEN historicId is returned`() {
        // given
        val historicId = 1L
        val historic = createHistoric(historicId)

        given(runBlocking { mockSaveHistoricUseCase(historic) })
            .willReturn(historicId)

        // when
        goalDetailViewModel.saveHistoric(historic)

        // then
        runBlocking {
            goalDetailViewModel.savedHistoric.first() equalTo historicId
        }
    }

    @Test
    fun `GIVEN getHistoricList is successful WHEN loadData is called THEN return a list of historics`() {
        // given
        val goalId = 1L
        val historics = listOf(
            createHistoric(historicId = 1, goalId = goalId),
            createHistoric(historicId = 2, goalId = goalId),
            createHistoric(historicId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetHistoricListUseCase(goalId) })
            .willReturn(historics)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        runBlocking {
            goalDetailViewModel.historics.first() equalTo historics
        }
    }
}
