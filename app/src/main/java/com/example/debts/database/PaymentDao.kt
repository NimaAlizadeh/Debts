package com.example.debts.database

import androidx.room.*
import com.example.debts.utils.Constants

@Dao
interface PaymentDao {
    @Transaction
    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE MOId = :debtId")
    suspend fun getDebtWithPayments(debtId: Int): DebtsWithPayment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(entity: PaymentEntity)

    @Delete
    suspend fun deletePayment(entity: PaymentEntity)

    @Query(""+"DELETE FROM ${Constants.PAYMENTS_TABLE_NAME} WHERE debtCreatorId = :debtId")
    suspend fun deleteWholePayment(debtId: Int)
}