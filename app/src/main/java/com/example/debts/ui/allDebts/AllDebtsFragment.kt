package com.example.debts.ui.allDebts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.R
import com.example.debts.api.ApiService
import com.example.debts.database.DebtEntity
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.databinding.FragmentAllDebtsBinding
import com.example.debts.repositories.AllDebtsRepository
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.DebtsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.AllDebtsVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllDebtsFragment : Fragment() , MainActivity.OnRefreshClickListener{
    private lateinit var binding: FragmentAllDebtsBinding

    private val viewModel: AllDebtsVM by viewModels()

    @Inject
    lateinit var adapter: DebtsAdapter

    @Inject
    lateinit var repository: AllDebtsRepository

    private var sendingDebtsIds = ArrayList<Int>()
    private var gettingNeededDebtsArray = ArrayList<Int>()

    @Inject
    lateinit var apiService: ApiService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDebtsBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).setOnRefreshClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appState.postValue(Constants.PAGE_ALL_DEBTS)

        binding.apply {

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
                                viewModel.deleteDebt(debtEntity)
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


            val refreshAlert = Dialog(requireContext())
            refreshAlert.setCancelable(false)
            refreshAlert.setContentView(R.layout.main_dialog_layout)
            refreshAlert.create()

            //loading on refresh handling
            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    refreshAlert.show()
                }
                else{
                    refreshAlert.dismiss()
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
        viewModel.loading.postValue(true)

        viewModel.getAllDebtsForServer()
        var allDebtsList = emptyList<DebtEntity>()
        viewModel.allDebtsForServer.observe(viewLifecycleOwner){
            allDebtsList = it
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.sync("642169069117096f05229569",allDebtsList)
            if(response.code() == 200){
                Toast.makeText(requireContext(), "khoda kheyret bede", Toast.LENGTH_SHORT).show()
            }
        }

//        viewModel.getCheckingList()
//        viewModel.getCheckListLiveData.observe(viewLifecycleOwner){
//            viewModel.sendCheckingList("642169069117096f05229569" ,it)
//        }
//        viewModel.sendingCheckListLiveData.observe(viewLifecycleOwner){
//            for(item in it){
//                if(item.lastModified == "mobile")
//                    sendingDebtsIds.add(item.id)
//                if(item.lastModified == "db")
//                    gettingNeededDebtsArray.add(item.id)
//            }
//
//            viewModel.getSendingDebtsToNet(sendingDebtsIds)
//        }
//
//        viewModel.getSendingDebtsToNet.observe(viewLifecycleOwner){
//            val sendRefreshModel = SendRefreshModel()
//            sendRefreshModel.toUpdate = it
//            sendRefreshModel.toFetch = gettingNeededDebtsArray
//            viewModel.sendNeededDebtsToNet(sendRefreshModel)
//            sendingDebtsIds.clear()
//            gettingNeededDebtsArray.clear()
//        }



        viewModel.loading.postValue(false)
    }

}