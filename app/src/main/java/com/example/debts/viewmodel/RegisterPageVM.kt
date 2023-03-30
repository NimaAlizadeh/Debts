package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.models.user.UserEntity
import com.example.debts.repositories.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPageVM @Inject constructor(private val repository: RegisterRepository) : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val registerUser = MutableLiveData<String>()

    fun registerUser(body: UserEntity) = viewModelScope.launch {
        loading.postValue(true)

        val response = repository.newUser(body)

        if(response.isSuccessful)
            registerUser.postValue(response.body())

        loading.postValue(false)
    }
}