package com.example.debts.database

import androidx.room.Embedded
import androidx.room.Relation

data class DebtsWithPayment(
        @Embedded val debt: DebtEntity,
        @Relation(
                parentColumn = "MOId",
                entityColumn = "debtCreatorId"
        )
        var paymentList: List<PaymentEntity>,
)