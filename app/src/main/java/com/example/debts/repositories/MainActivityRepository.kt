package com.example.debts.repositories

import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentDao
import com.example.debts.database.PaymentEntity
import javax.inject.Inject

class MainActivityRepository @Inject constructor(private val debtDao: DebtDao, private val paymentDao: PaymentDao) {


}