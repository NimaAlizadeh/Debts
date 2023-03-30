package com.example.debts.repositories

import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import javax.inject.Inject

class TodayDebtsRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao)
{
    fun getTodayDebts(date: String): MutableList<DebtEntity>{
        return debtDao.getTodayDebts(date)
    }

    suspend fun deleteDebt(deletingDebt: DebtEntity) = debtDao.deleteDebt(deletingDebt)

    suspend fun deleteWholePayment(debtId: Int) = paymentDao.deleteWholePayment(debtId)
}