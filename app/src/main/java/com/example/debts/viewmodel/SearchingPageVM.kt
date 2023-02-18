package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.database.DebtEntity
import com.example.debts.repositories.SearchDebtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingPageVM @Inject constructor(private val repository: SearchDebtsRepository) : ViewModel() {
    var empty = MutableLiveData<Boolean>()
    var searchingList = MutableLiveData<List<DebtEntity>>()
    var loading = MutableLiveData<Boolean>()

    fun loadSearching(fullName: String) = viewModelScope.launch {
        loading.postValue(false)

        val response = repository.getSearchedDebts(fullName)
        if(response.isNotEmpty()){
            searchingList.postValue(response)
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