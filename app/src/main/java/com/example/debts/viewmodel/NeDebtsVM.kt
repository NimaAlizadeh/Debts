package com.example.debts.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.api.ApiService
import com.example.debts.database.DebtEntity
import com.example.debts.models.RegisterResponse
import com.example.debts.models.registerBody
import com.example.debts.repositories.EntityRepository
import com.example.debts.repositories.NeDebtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class NeDebtsVM @Inject constructor(private val repository: NeDebtsRepository): ViewModel() {

    var response = MutableLiveData<DebtEntity>()
    var insertResponse = MutableLiveData<Int>()
    var updateResponse = MutableLiveData<String>()

    fun insertDebt(insertingDebt: DebtEntity) = viewModelScope.launch {
        repository.insertDebt(insertingDebt)
    }

    fun updateDebt(updatingDebt: DebtEntity) = viewModelScope.launch {
        repository.updateDebt(updatingDebt)
    }

    fun getSingleDebt(debtCode: Int) = viewModelScope.launch {
        response.postValue(repository.getSingleDebt(debtCode))
    }

//    fun insertDebtToServer(userId: String, body: DebtEntity) = viewModelScope.launch {
//        Log.d("mashti", "got into method")
//        val response = repository.insertDebtToServer(userId ,body)
//        Log.d("mashti", "after request")
//        if(response.isSuccessful){
//            Log.d("mashti", "is successful")
//            insertResponse.postValue(response.code())
//        }
//    }

//    fun updateDebtToServer(id: Int, body: DebtEntity) = viewModelScope.launch {
//    }
}

// 1680273688435
// 1680273716803 update shod bishtar shod