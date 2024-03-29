package com.example.debts.repositories

import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import javax.inject.Inject

class SearchDebtsRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao) {
    fun getSearchedDebts(fullName: String) = debtDao.getSearchedDebts(fullName)
    suspend fun updateDebt(updatingDebt: DebtEntity) = debtDao.updateDebt(updatingDebt)
    suspend fun deleteWholePayment(debtId: Int) = paymentDao.deleteWholePayment(debtId)
}