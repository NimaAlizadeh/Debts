package com.example.debts.models.entity

import com.example.debts.database.DebtEntity

data class SendRefreshModel(
    var toUpdate: List<DebtEntity> = emptyList(),
    var toFetch: List<Int> = emptyList()
)
