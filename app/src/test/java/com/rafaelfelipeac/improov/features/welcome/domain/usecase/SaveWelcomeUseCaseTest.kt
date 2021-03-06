package com.rafaelfelipeac.improov.features.welcome.domain.usecase

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.welcome.domain.repository.WelcomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveWelcomeUseCaseTest {

    @Mock
    internal lateinit var mockWelcomeRepository: WelcomeRepository

    private lateinit var saveWelcomeUseCase: SaveWelcomeUseCase

    @Before
    fun setup() {
        saveWelcomeUseCase = SaveWelcomeUseCase(mockWelcomeRepository)
    }

    @Test
    fun `GIVEN a boolean value WHEN saveWelcomeUseCase is called THEN return just a Unit value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(mockWelcomeRepository.save(booleanValue))
                .willReturn(Unit)

            // when
            val result = saveWelcomeUseCase(booleanValue)

            // then
            result equalTo Unit
        }
    }
}
