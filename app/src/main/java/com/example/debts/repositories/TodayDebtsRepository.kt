package com.example.debts.repositories

import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import javax.inject.Inject

class TodayDebtsRepository @Inject constructor(private val debtDao: DebtDao)
{
    fun getTodayDebts(date: String): MutableList<DebtEntity>{
        return debtDao.getTodayDebts(date)
    }

    suspend fun deleteDebt(deletingDebt: DebtEntity) = debtDao.deleteDebt(deletingDebt)
}