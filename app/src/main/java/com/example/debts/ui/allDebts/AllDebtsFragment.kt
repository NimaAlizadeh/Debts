package com.example.debts.ui.allDebts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.R
import com.example.debts.api.ApiService
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.databinding.FragmentAllDebtsBinding
import com.example.debts.models.PaymentsWithDebts
import com.example.debts.repositories.AllDebtsRepository
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.DebtsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.AllDebtsVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import matteocrippa.it.karamba.today
import retrofit2.Response
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AllDebtsFragment : Fragment() , MainActivity.OnRefreshClickListener{
    private lateinit var binding: FragmentAllDebtsBinding

    private val viewModel: AllDebtsVM by viewModels()

    @Inject
    lateinit var adapter: DebtsAdapter

    @Inject
    lateinit var repository: AllDebtsRepository

    @Inject
    lateinit var apiService: ApiService

    private var allDebtsList = emptyList<DebtEntity>()

    private lateinit var refreshAlert: Dialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDebtsBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).setOnRefreshClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appState.postValue(Constants.PAGE_ALL_DEBTS)
        viewModel.getAllPaymentsForServer()

        binding.apply {
            //alert dialog for sync project
            refreshAlert = Dialog(requireContext())
            refreshAlert.setCancelable(false)
            refreshAlert.setContentView(R.layout.main_dialog_layout)
            refreshAlert.create()

            repository.getAllDebts().observe(viewLifecycleOwner){
                viewModel.allDebtsListLiveData.postValue(it)
                if(it.isNotEmpty())
                    viewModel.empty.postValue(false)
                else
                    viewModel.empty.postValue(true)
                adapter.setData(it)
                allDebtsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                allDebtsRecycler.adapter = adapter
            }

            viewModel.allDebtsListLiveData.observe(viewLifecycleOwner){
                allDebtsList = it
                viewModel.getAllDebtsForServer()
                viewModel.getAllPaymentsForServer()
            }

            //adapter's on click listeners
            adapter.setOnItemCLickListener { debtEntity, status ->
                when(status){
                    Constants.ON_CLICK_GOTO_DETAIL -> {
                        val direction = AllDebtsFragmentDirections.actionAllDebtsFragmentToDetailsFragment()
                        MainActivity.whichDebtCode = debtEntity.MOId
                        findNavController().navigate(direction)
                    }

                    Constants.ON_CLICK_DELETE -> {
                        val alert = AlertDialog.Builder(requireContext())
                        alert.setTitle("حذف بدهی")
                            .setMessage("آیا برای حذف این بدهی اطمینان دارید؟")
                            .setPositiveButton("بله") { _, _ ->
                                val entity = debtEntity
                                entity.isDeleted = true
                                entity.updatedAt = Date().today().time
                                viewModel.deleteDebt(entity)
                            }
                            .setNegativeButton("خیر", null)
                            .setCancelable(false)
                            .create()
                            .show()
                    }

                    Constants.ON_CLICK_EDIT -> {
                        NeDebtsFragment().show(parentFragmentManager,NeDebtsFragment().tag)
                        MainActivity.neStatus = Constants.ON_CLICK_EDIT
                        MainActivity.whichDebtCode = debtEntity.MOId
                    }

                    Constants.ON_CLICK_SHARE -> {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "بدهی خانم/آقای " + debtEntity.fullName + " تا تاریخ " + debtEntity.debtRemainingDate + " به مبلغ " + String.format("%,d",debtEntity.debtRemaining.toLong()) + " ریال می باشد.")
                            type = "text/plain"
                        }
                        startActivity(sendIntent)
                    }
                }
            }

            //handling empty layout
            viewModel.empty.observe(viewLifecycleOwner){
                if(it)
                {
                    allDebtsEmptyLayout.visibility = View.VISIBLE
                    allDebtsRecycler.visibility = View.GONE
                }
                else
                {
                    allDebtsEmptyLayout.visibility = View.GONE
                    allDebtsRecycler.visibility = View.VISIBLE
                }
            }


            //handling the toolbar menu
            allDebtsToolbar.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.toolbar_menu_addDebts -> {
                        NeDebtsFragment().show(parentFragmentManager,NeDebtsFragment().tag)
                        MainActivity.neStatus = Constants.ON_CLICK_ADD
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }

        }
    }

    override fun refreshOnClick() {
        refreshAlert.show()
        var responseOk: Boolean
        val paymentsWithDebts = PaymentsWithDebts()
        var debtsResponse: Response<List<DebtEntity>>? = null
        var paymentsResponse: Response<List<PaymentEntity>>? = null

        lifecycle.coroutineScope.launchWhenCreated {
            try {
                if(viewModel.allDebtsForServer.value != null){
                    val response = apiService.syncDebts(Constants.USER_ID ,viewModel.allDebtsForServer.value!!)
                    Log.d("mashti", "payments = " + response.errorBody())
                    if(response.isSuccessful && response.code() == 200){
                        Log.d("mashti", "got into debts")
                        debtsResponse = response
                        paymentsWithDebts.debts = response.body()!!
                    }
                }
                if(viewModel.allPaymentsForServer.value != null){
                    paymentsWithDebts.payments = viewModel.allPaymentsForServer.value!!
                    val response1 = apiService.syncPayments(Constants.USER_ID, paymentsWithDebts)
                    Log.d("mashti", "payments = " + response1.errorBody())
                    if(response1.isSuccessful && response1.code() == 200){
                        Log.d("mashti", "got into debts")
                        paymentsResponse = response1
                    }
                }

                responseOk = if(debtsResponse != null && paymentsResponse != null){
                    viewModel.deleteDebts()
                    delay(2000)
                    viewModel.insertMultipleDebts(debtsResponse!!.body()!!)
                    viewModel.deletePayments()
                    delay(2000)
                    viewModel.insertMultiplePayments(paymentsResponse!!.body()!!)
                    true
                }else{ false }

                withContext(Dispatchers.Main){
                    refreshAlert.dismiss()
                    if(responseOk)
                        Toast.makeText(requireContext(), "همگام سازی با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(requireContext(), "همگام سازی به مشکل خورد", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    refreshAlert.dismiss()
                    Toast.makeText(requireContext(), "برنامه در برقراری با اینترنت به مشکل خورد", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}