package com.example.debts.repositories

import androidx.lifecycle.LiveData
import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import com.example.debts.database.PaymentEntity
import javax.inject.Inject


class AllDebtsRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao) //****** we will use retrofit later here in getAllDebts function
{
    fun getAllDebts() : LiveData<MutableList<DebtEntity>> {
        return debtDao.getAllDebts()
    }

    suspend fun deleteDebt(deletingDebt: DebtEntity) = debtDao.deleteDebt(deletingDebt)
}