package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.models.entity.CheckModel
import com.example.debts.models.entity.CheckResponse
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity
import com.example.debts.repositories.AllDebtsRepository
import com.example.debts.repositories.RefreshRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDebtsVM @Inject constructor(private val repository: AllDebtsRepository, private val refreshRepository: RefreshRepository): ViewModel()
{
    var allDebtsListLiveData = MutableLiveData<MutableList<DebtEntity>>()
    var empty = MutableLiveData<Boolean>()
    var allDebtsForServer = MutableLiveData<List<DebtEntity>>()
    var allPaymentsForServer = MutableLiveData<List<PaymentEntity>>()


    fun deleteDebt(updatingDebt: DebtEntity) = viewModelScope.launch {
        repository.updateDebt(updatingDebt)
        repository.deleteWholePayment(updatingDebt.MOId)
    }

    fun getAllDebtsForServer() = viewModelScope.launch {
        val response = repository.getAllDebtsForServer()
        allDebtsForServer.postValue(response)
    }

    fun getAllPaymentsForServer() = viewModelScope.launch {
        val response = repository.getAllPaymentsForServer()
        allPaymentsForServer.postValue(response)
    }

    fun insertMultipleDebts(debtsList: List<DebtEntity>) = viewModelScope.launch {
        repository.insertMultipleDebts(debtsList)
    }

    fun updateMultipleDebts(debtsList: List<DebtEntity>) = viewModelScope.launch {
        repository.updateMultipleDebts(debtsList)
    }

    fun insertMultiplePayments(paymentsList: List<PaymentEntity>) = viewModelScope.launch {
        repository.insertMultiplePayments(paymentsList)
    }

    fun updateMultiplePayments(paymentsList: List<PaymentEntity>) = viewModelScope.launch {
        repository.updateMultiplePayments(paymentsList)
    }

    fun deleteDebts() = viewModelScope.launch {
        repository.deleteDebts()
    }

    fun deletePayments() = viewModelScope.launch {
        repository.deletePayments()
    }
}