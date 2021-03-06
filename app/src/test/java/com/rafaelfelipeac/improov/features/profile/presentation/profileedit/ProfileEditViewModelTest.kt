package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveNameUseCase
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
class ProfileEditViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveNameUseCase = mock(SaveNameUseCase::class.java)
    private var mockGetNameUseCase = mock(GetNameUseCase::class.java)

    private lateinit var profileEditViewModel: ProfileEditViewModel

    @Before
    fun setup() {
        profileEditViewModel = ProfileEditViewModel(
            mockSaveNameUseCase,
            mockGetNameUseCase
        )
    }

    @Test
    fun `GIVEN saveName is successful WHEN saveName is called THEN a Unit is returned`() {
        // given
        val name = "User Name"

        given(runBlocking { mockSaveNameUseCase(name) })
            .willReturn(Unit)

        // when
        profileEditViewModel.saveName(name)

        // then
        runBlocking {
            profileEditViewModel.saved.first() equalTo Unit
        }
    }

    @Test
    fun `GIVEN getName is successful WHEN loadData is called THEN a name is returned`() {
        // given
        val name = "User Name"

        given(runBlocking { mockGetNameUseCase() })
            .willReturn(name)

        // when
        profileEditViewModel.loadData()

        // then
        runBlocking {
            profileEditViewModel.name.first() equalTo name
        }
    }
}
