package com.example.debts.repositories

import com.example.debts.api.ApiService
import com.example.debts.models.entity.CheckModel
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.database.DebtDao
import javax.inject.Inject

class RefreshRepository @Inject constructor(private val apiService: ApiService, private val dao: DebtDao)
{
//    suspend fun getCheckingList() = dao.getCheckingList()
//    suspend fun sendCheckingListApi(userId: String, body: List<CheckModel>) = apiService.sendCheckingUpdates(userId, body)
}