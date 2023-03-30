package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.models.entity.CheckModel
import com.example.debts.models.entity.CheckResponse
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.database.DebtEntity
import com.example.debts.repositories.AllDebtsRepository
import com.example.debts.repositories.RefreshRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDebtsVM @Inject constructor(private val repository: AllDebtsRepository, private val refreshRepository: RefreshRepository): ViewModel()
{
    var allDebtsListLiveData = MutableLiveData<MutableList<DebtEntity>>()
    var loading = MutableLiveData<Boolean>()
    var empty = MutableLiveData<Boolean>()
    var getCheckListLiveData = MutableLiveData<List<CheckModel>>()
    var sendingCheckListLiveData = MutableLiveData<List<CheckResponse>>()
    var getSendingDebtsToNet = MutableLiveData<List<DebtEntity>>()
    var allDebtsForServer = MutableLiveData<List<DebtEntity>>()


    fun deleteDebt(deletingDebt: DebtEntity) = viewModelScope.launch {
        repository.deleteDebt(deletingDebt)
        repository.deleteWholePayment(deletingDebt.MOId)
    }

//    fun getCheckingList() = viewModelScope.launch {
//        val response = refreshRepository.getCheckingList()
//        if(response.isNotEmpty())
//            getCheckListLiveData.postValue(response)
//    }
//
//    fun sendCheckingList(userId: String, body: List<CheckModel>) = viewModelScope.launch {
//        val response = refreshRepository.sendCheckingListApi(userId, body)
//        if(response.isSuccessful){
//            sendingCheckListLiveData.postValue(response.body())
//        }
//    }

    fun getSendingDebtsToNet(wantedIds: List<Int>) = viewModelScope.launch {
        val response = repository.getSendingDebtsToNet(wantedIds)
        if(response.isNotEmpty())
            getSendingDebtsToNet.postValue(response)
    }

    fun getAllDebtsForServer() = viewModelScope.launch {
        val response = repository.getAllDebtsForServer()
        allDebtsForServer.postValue(response)
    }
}