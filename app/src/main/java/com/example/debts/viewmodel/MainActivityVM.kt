package com.example.debts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.debts.database.DebtEntity
import com.example.debts.database.DebtsWithPayment
import com.example.debts.database.PaymentEntity
import com.example.debts.repositories.MainActivityRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityVM @Inject constructor(private val repository: MainActivityRepository): ViewModel() {


}