package com.example.debts.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.debts.utils.Constants

@Entity(tableName = Constants.PAYMENTS_TABLE_NAME)
data class PaymentEntity (
    @PrimaryKey(autoGenerate = true)
    var paymentId: Int = 0,
    var debtCreatorId: Int = 0,
    var date: String = "",
    var amount: String = "",
    var isSynced: Boolean = false
)