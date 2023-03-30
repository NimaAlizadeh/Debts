package com.example.debts.repositories

import com.example.debts.api.ApiService
import com.example.debts.models.user.UserEntity
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService){
    suspend fun newUser(body: UserEntity) = apiService.newUser(body)
}