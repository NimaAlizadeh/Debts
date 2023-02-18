package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.cm.models.CheckModel
import com.example.debts.cm.models.CheckResponse
import com.example.debts.database.DebtEntity
import com.example.debts.database.DebtsWithPayment
import com.example.debts.database.PaymentEntity
import com.example.debts.repositories.AllDebtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDebtsVM @Inject constructor(private val repository: AllDebtsRepository): ViewModel()
{
    var allDebtsListLiveData = MutableLiveData<MutableList<DebtEntity>>()
    var loading = MutableLiveData<Boolean>()
    var empty = MutableLiveData<Boolean>()
//    var getCheckListLiveData = MutableLiveData<List<CheckModel>>()
//    var sendingCheckListLiveData = MutableLiveData<List<CheckResponse>>()


    fun loadDebtsList() = viewModelScope.launch {
        loading.postValue(true)

        if(repository.getAllDebts().value?.isNotEmpty() == true){
            empty.postValue(false)
        }
        else{
            empty.postValue(true)
        }

        loading.postValue(false)
    }

    fun deleteDebt(deletingDebt: DebtEntity) = viewModelScope.launch {
        repository.deleteDebt(deletingDebt)
    }

//    fun getCheckingList() = viewModelScope.launch {
//        val response = refreshRepository.getCheckingList()
//        if(response.isNotEmpty())
//            getCheckListLiveData.postValue(response)
//    }
//
//    fun sendCheckingList(body: List<CheckModel>) = viewModelScope.launch {
//        val response = refreshRepository.sendCheckingListApi(body)
//        if(response.isSuccessful){
//            sendingCheckListLiveData.postValue(response.body())
//        }
//    }
}