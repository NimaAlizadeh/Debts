package com.example.debts.repositories

import com.example.debts.api.ApiService
import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import com.example.debts.database.PaymentEntity
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao, private val apiService: ApiService)
{
    suspend fun getSingleDebt(debtCode: Int) = debtDao.getSingleDebt(debtCode)
    suspend fun deleteDebt(updatingDebt: DebtEntity) = debtDao.updateDebt(updatingDebt)
    suspend fun updateDebt(updatingDebt: DebtEntity) = debtDao.updateDebt(updatingDebt)

    suspend fun insertPayment(entity: PaymentEntity) = paymentDao.insertPayment(entity)
    suspend fun updatePayment(entity: PaymentEntity) = paymentDao.updatePayment(entity)
    suspend fun getDebtWithPayments(debtId: Int) = paymentDao.getDebtWithPayments(debtId)
    suspend fun deleteWholePayment(debtId: Int) = paymentDao.deleteWholePayment(debtId)
}