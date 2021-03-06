package com.rafaelfelipeac.improov.features.goal.domain.repository

import com.rafaelfelipeac.improov.features.commons.domain.model.Historic

interface HistoricRepository {

    suspend fun getHistorics(): List<Historic>

    suspend fun getHistoric(historicId: Long): Historic

    suspend fun save(historic: Historic): Long

    suspend fun delete(historic: Historic)
}
