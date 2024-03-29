package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.database.DebtEntity
import com.example.debts.database.DebtsWithPayment
import com.example.debts.database.PaymentEntity
import com.example.debts.repositories.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsVM @Inject constructor(private val repository: DetailsRepository) : ViewModel() {

    var loading = MutableLiveData<Boolean>()
    var detailsLiveData = MutableLiveData<DebtEntity>()
    var paymentsLiveData = MutableLiveData<List<PaymentEntity>>()

    fun loadDetails(debtCode: Int) = viewModelScope.launch {
        loading.postValue(false)

        val response = repository.getSingleDebt(debtCode)
        if(response.MOId != 0)
            detailsLiveData.postValue(response)

        loading.postValue(true)
    }

    fun deleteDebt(updatingDebt: DebtEntity, debtId: Int) = viewModelScope.launch {
        repository.deleteDebt(updatingDebt)
        repository.deleteWholePayment(debtId)
    }

    fun updateDebt(updatingDebt: DebtEntity) = viewModelScope.launch {
        repository.updateDebt(updatingDebt)
    }

    fun insertPayment(entity: PaymentEntity) = viewModelScope.launch {
        repository.insertPayment(entity)
    }

    fun deletePayment(entity: PaymentEntity) = viewModelScope.launch {
        repository.updatePayment(entity)
    }

    fun getDebtWithPayments(debtId: Int) = viewModelScope.launch {
        val response = repository.getDebtWithPayments(debtId)
        val tempList = ArrayList<PaymentEntity>()
        for(item in response.paymentList){
            if(!item.isDeleted)
                tempList.add(item)
        }
        paymentsLiveData.postValue(tempList)
    }
}