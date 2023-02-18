package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.database.DebtEntity
import com.example.debts.repositories.TodayDebtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDebtsVM @Inject constructor(private val repository: TodayDebtsRepository) : ViewModel() {
    var empty = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var todayDebtsList = MutableLiveData<List<DebtEntity>>()

    fun loadTodayDebts(date: String) = viewModelScope.launch {
        loading.postValue(false)

        val response = repository.getTodayDebts(date)
        if(response.isNotEmpty()){
            todayDebtsList.postValue(response)
            empty.postValue(false)
        }else{
            empty.postValue(true)
        }

        loading.postValue(true)
    }

    fun deleteDebt(deletingDebt: DebtEntity) = viewModelScope.launch {
        repository.deleteDebt(deletingDebt)
    }
}