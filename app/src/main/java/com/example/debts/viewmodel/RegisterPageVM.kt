package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPageVM @Inject constructor() : ViewModel() {
//    val loading = MutableLiveData<Boolean>()
//    val registerUser = MutableLiveData<RegisterResponse>()
//
//    fun registerUser(body: RegisterBody) = viewModelScope.launch {
//        loading.postValue(true)
//
//        val response = registerRepository.registerUser(body)
//
//        if(response.isSuccessful)
//            registerUser.postValue(response.body())
//
//        loading.postValue(false)
//    }
}