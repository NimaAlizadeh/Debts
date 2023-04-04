package com.example.debts.database

import androidx.room.*
import com.example.debts.utils.Constants

@Dao
interface PaymentDao {
    @Transaction
    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE MOId = :debtId AND isDeleted = 0")
    suspend fun getDebtWithPayments(debtId: Int): DebtsWithPayment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(entity: PaymentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiplePayments(paymentsList: List<PaymentEntity>)

    @Update
    suspend fun updateMultiplePayments(paymentsList: List<PaymentEntity>)

    @Update
    suspend fun updatePayment(entity: PaymentEntity)

    @Query(""+"UPDATE ${Constants.PAYMENTS_TABLE_NAME} SET isDeleted = 1 WHERE debtCreatorId = :debtId")
    suspend fun deleteWholePayment(debtId: Int)

    @Query(""+"SELECT * FROM ${Constants.PAYMENTS_TABLE_NAME}")
    suspend fun getAllPayments(): List<PaymentEntity>

    @Query(""+"DELETE FROM ${Constants.PAYMENTS_TABLE_NAME}")
    suspend fun deletePayments()
}