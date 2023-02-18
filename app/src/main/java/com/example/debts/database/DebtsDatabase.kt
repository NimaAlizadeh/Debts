package com.example.debts.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DebtEntity::class, PaymentEntity::class], version = 1)
abstract class DebtsDatabase:RoomDatabase()
{
    abstract fun debtDao(): DebtDao
    abstract fun paymentDao(): PaymentDao
}