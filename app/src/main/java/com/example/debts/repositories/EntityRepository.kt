package com.example.debts.repositories

import android.util.Log
import com.example.debts.api.ApiService
import com.example.debts.database.DebtEntity
import com.example.debts.models.registerBody
import javax.inject.Inject

class EntityRepository @Inject constructor(private val apiService: ApiService){
//    suspend fun updateDebt(id: Int, body: DebtEntity) = apiService.updateDebt(id, body)
}