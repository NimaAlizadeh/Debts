package com.example.debts.ui.allDebts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.R
import com.example.debts.database.DebtsDatabase
import com.example.debts.databinding.FragmentAllDebtsBinding
import com.example.debts.repositories.AllDebtsRepository
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.DebtsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.AllDebtsVM
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import javax.inject.Inject

@AndroidEntryPoint
class AllDebtsFragment : Fragment() {
    private lateinit var binding: FragmentAllDebtsBinding

    private val viewModel: AllDebtsVM by viewModels()

    @Inject
    lateinit var adapter: DebtsAdapter

    @Inject
    lateinit var repository: AllDebtsRepository


//    @Inject
//    lateinit var refreshRepository: CheckRepository

//    @Inject
//    lateinit var a : CheckUpdates


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDebtsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appState.postValue(Constants.PAGE_ALL_DEBTS)

        binding.apply {

            //checking updates
//            viewModel.getCheckingList()
//            viewModel.getCheckListLiveData.observe(viewLifecycleOwner){
//                viewModel.sendingCheckListLiveData.postValue(it)
//            }
//            viewModel.sendingCheckListLiveData.observe(viewLifecycleOwner){
//
//            }



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
                        MainActivity.whichDebtCode = debtEntity.debtId
                        findNavController().navigate(direction)
                    }

                    Constants.ON_CLICK_DELETE -> {
                        val alert = AlertDialog.Builder(requireContext())
                        alert.setTitle("حذف")
                            .setMessage("آیا برای حذف این مورد اطمینان دارید؟")
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
                        MainActivity.whichDebtCode = debtEntity.debtId
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
}