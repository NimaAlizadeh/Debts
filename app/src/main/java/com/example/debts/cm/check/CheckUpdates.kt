package com.example.debts.cm.check

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.debts.cm.models.CheckModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//class CheckUpdates @Inject constructor(private val repository: CheckRepository)
//{
//
//    var checkingList = MutableLiveData<List<CheckModel>>()
//    lateinit var owner: LifecycleOwner

//    fun sendUpdates ()
//    {
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = repository.getCheckingList()
//            if(response.isNotEmpty())
//                checkingList.postValue(response)
//
//            val observer = Observer<List<CheckModel>>{
//
//            }
//
//            checkingList.observe(owner,observer)
//
//        }
//
//    }
//}