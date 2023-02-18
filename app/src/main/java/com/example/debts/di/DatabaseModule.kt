package com.example.debts.di

import android.content.Context
import androidx.room.Room
import com.example.debts.database.DebtEntity
import com.example.debts.database.DebtsDatabase
import com.example.debts.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{
    @Singleton
    @Provides
    fun provideDebtDao(db: DebtsDatabase) = db.debtDao()

    @Singleton
    @Provides
    fun providePaymentDao(db: DebtsDatabase) = db.paymentDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, DebtsDatabase::class.java, Constants.DATA_BASE_NAME)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideNoteEntity(): DebtEntity = DebtEntity()

    @Singleton
    @Provides
    fun provideString(): String = String()
}