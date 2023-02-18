package com.example.debts.repositories

import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import javax.inject.Inject

class SearchDebtsRepository @Inject constructor(private val debtDao: DebtDao) {
    fun getSearchedDebts(fullName: String) = debtDao.getSearchedDebts(fullName)
    suspend fun deleteDebt(deletingDebt: DebtEntity) = debtDao.deleteDebt(deletingDebt)
}