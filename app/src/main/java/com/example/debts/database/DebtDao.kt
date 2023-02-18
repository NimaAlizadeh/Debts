package com.example.debts.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.debts.cm.models.CheckModel
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

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME}")
    fun getAllDebts(): LiveData<MutableList<DebtEntity>>

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE debtRemainingDate = :date")
    fun getTodayDebts(date: String): MutableList<DebtEntity>

    @Query(""+ "SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE debtId = :debtCode")
    suspend fun getSingleDebt(debtCode: Int) : DebtEntity

    @Query(""+"SELECT * FROM ${Constants.DEBTS_TABLE_NAME} WHERE fullName LIKE '%' || :fullName || '%'")
    fun getSearchedDebts(fullName: String): MutableList<DebtEntity>

//    @Query(""+"SELECT debtId,lastUpdate FROM ${Constants.DEBTS_TABLE_NAME}")
//    suspend fun getCheckingList(): List<CheckModel>
}