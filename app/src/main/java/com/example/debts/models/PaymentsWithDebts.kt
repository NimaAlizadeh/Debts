package com.example.debts.models

import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity

data class PaymentsWithDebts(
    var payments: List<PaymentEntity> = emptyList(),
    var debts: List<DebtEntity> = emptyList()
)
