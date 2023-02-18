package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.database.DebtDao
import com.example.debts.database.DebtEntity
import com.example.debts.repositories.NeDebtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeDebtsVM @Inject constructor(private val repository: NeDebtsRepository): ViewModel() {

    var response = MutableLiveData<DebtEntity>()

    fun insertDebt(insertingDebt: DebtEntity) = viewModelScope.launch {
        repository.insertDebt(insertingDebt)
    }

    fun updateDebt(updatingDebt: DebtEntity) = viewModelScope.launch {
        repository.updateDebt(updatingDebt)
    }

    fun getSingleDebt(debtCode: Int) = viewModelScope.launch {
        response.postValue(repository.getSingleDebt(debtCode))
    }
}