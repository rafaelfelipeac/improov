package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.WelcomeDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WelcomeDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var welcomeDataSource: WelcomeDataSource

    @Before
    fun setup() {
        welcomeDataSource = WelcomeDataSource(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN getWelcome is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.welcome)
                .willReturn(booleanValue)

            // when
            val result = welcomeDataSource.getWelcome()

            // then
            result equalTo booleanValue
        }
    }

    @Test
    fun `GIVEN a saved new boolean value WHEN getWelcome is called THEN the boolean value must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).welcome = booleanValue
            given(preferences.welcome)
                .willReturn(booleanValue)

            // when
            val resultOfSave = welcomeDataSource.save(booleanValue)
            val returnOfGet = welcomeDataSource.getWelcome()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo booleanValue
        }
    }
}
