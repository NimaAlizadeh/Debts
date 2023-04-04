package com.example.debts.repositories

import com.example.debts.api.ApiService
import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import javax.inject.Inject

class NeDebtsRepository @Inject constructor(private val debtDao: DebtDao, private val apiService: ApiService)
{
    suspend fun insertDebt(newDebt: DebtEntity) = debtDao.insertDebt(newDebt)
    suspend fun updateDebt(updatingDebt: DebtEntity) = debtDao.updateDebt(updatingDebt)
    suspend fun getSingleDebt(debtCode: Int) : DebtEntity = debtDao.getSingleDebt(debtCode)

//    suspend fun insertDebtToServer(userId: String, body: DebtEntity) = apiService.insertDebt(userId, body)
}