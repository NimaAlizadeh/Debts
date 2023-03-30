package com.example.debts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.debts.models.entity.CheckModel
import com.example.debts.utils.Constants

@Dao
interface DebtDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(newDebt: DebtEntity)

    @Delete
    suspend fun deleteDebt(deletingDebt: DebtEntity)

    @Update
    suspend fun updateDebt(updatingDebt: DebtEntity)

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE isDeleted = 0")
    fun getAllDebts(): LiveData<MutableList<DebtEntity>>

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE debtRemainingDate = :date AND isDeleted = 0")
    fun getTodayDebts(date: String): MutableList<DebtEntity>

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE MOId = :debtCode AND isDeleted = 0")
    suspend fun getSingleDebt(debtCode: Int) : DebtEntity

    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE fullName LIKE '%' || :fullName || '%' AND isDeleted = 0")
    fun getSearchedDebts(fullName: String): MutableList<DebtEntity>

    //..........................................................................................

    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME}")
    suspend fun getAllDebtsForServer(): List<DebtEntity>

    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE MOId IN (:wantedIds)")
    suspend fun getSendingDebtsToNet(wantedIds: List<Int>): MutableList<DebtEntity>
}