package com.example.debts.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.debts.utils.Constants

@Entity(tableName = Constants.DEBTS_TABLE_NAME)
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    var MOId:Int = 0,
    var DBId: String = "",
    var fullName: String = "",
    var phoneNumber: String = "",
    var moreDetails: String = "",
    var isSynced: Boolean = false,
    var debtRemaining: String = "",
    var debtRemainingDate: String = "",
    var debtRemainingTimeStamp: Long = 0L,
    var buyDate: String = "",
    var goldPrice: String = "",
    var lastModified: Long = 0L,
    var isDeleted: Boolean = false,
)