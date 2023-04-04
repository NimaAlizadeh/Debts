package com.example.debts.repositories

import androidx.lifecycle.LiveData
import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import com.example.debts.database.PaymentEntity
import javax.inject.Inject


class AllDebtsRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao)
{
    fun getAllDebts() : LiveData<MutableList<DebtEntity>> {
        return debtDao.getAllDebts()
    }

    suspend fun insertMultipleDebts(debtsList: List<DebtEntity>) = debtDao.insertMultipleDebts(debtsList)
    suspend fun updateMultipleDebts(debtsList: List<DebtEntity>) = debtDao.updateMultipleDebts(debtsList)

    suspend fun insertMultiplePayments(paymentsList: List<PaymentEntity>) = paymentDao.insertMultiplePayments(paymentsList)
    suspend fun updateMultiplePayments(paymentsList: List<PaymentEntity>) = paymentDao.updateMultiplePayments(paymentsList)

    suspend fun updateDebt(deletingDebt: DebtEntity) = debtDao.updateDebt(deletingDebt)

    suspend fun deleteWholePayment(debtId: Int) = paymentDao.deleteWholePayment(debtId)

    suspend fun getAllDebtsForServer() = debtDao.getAllDebtsForServer()
    suspend fun getAllPaymentsForServer() = paymentDao.getAllPayments()

    suspend fun deleteDebts() = debtDao.deleteDebts()
    suspend fun deletePayments() = paymentDao.deletePayments()
}