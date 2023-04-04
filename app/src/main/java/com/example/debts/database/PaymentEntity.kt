package com.example.debts.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.debts.utils.Constants

@Entity(tableName = Constants.PAYMENTS_TABLE_NAME)
data class PaymentEntity (
    @PrimaryKey(autoGenerate = true)
    var paymentId: Int = 0,
    var debtCreatorId: Int = 0,
    var debtDBId: String = "",
    var userId: String = Constants.USER_ID,
    var DBId: String = "",
    var date: String = "",
    var amount: String = "",
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false,
    var updatedAt: Long = 0L
)