package com.example.debts.ui.searchDebts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.databinding.FragmentSearchBinding
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.DebtsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.SearchingPageVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    val viewModel: SearchingPageVM by viewModels()

    @Inject
    lateinit var adapter: DebtsAdapter

    @Inject
    lateinit var text: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appState.postValue(Constants.PAGE_SEARCH)
        binding.apply {


            searchDebtsEdt.addTextChangedListener {
                text = it.toString()
                if(text.isNotEmpty())
                    viewModel.loadSearching(text)
                else{
                    viewModel.searchingList.postValue(emptyList())
                    viewModel.empty.postValue(true)
                }
            }

            viewModel.empty.observe(viewLifecycleOwner){
                if(it)
                {
                    searchDebtsEmptyLayout.visibility = View.VISIBLE
                    searchDebtsRecycler.visibility = View.GONE
                }
                else
                {
                    searchDebtsEmptyLayout.visibility = View.GONE
                    searchDebtsRecycler.visibility = View.VISIBLE
                }
            }

            adapter.setOnItemCLickListener { debtEntity, status ->
                when(status){
                    Constants.ON_CLICK_GOTO_DETAIL -> {
                        val direction = SearchFragmentDirections.actionSearchFragmentToDetailsFragment()
                        MainActivity.whichDebtCode = debtEntity.debtId
                        findNavController().navigate(direction)
                    }

                    Constants.ON_CLICK_DELETE -> {
                        val alert = AlertDialog.Builder(requireContext())
                        alert.setTitle("حذف")
                            .setMessage("آیا برای حذف این مورد اطمینان دارید؟")
                            .setPositiveButton("بله") { _, _ ->
                                viewModel.deleteDebt(debtEntity)
                                viewModel.loadSearching(text)
                            }
                            .setNegativeButton("خیر", null)
                            .setCancelable(false)
                            .create()
                            .show()
                    }

                    Constants.ON_CLICK_EDIT -> {
                        NeDebtsFragment().show(parentFragmentManager, NeDebtsFragment().tag)
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

            viewModel.searchingList.observe(viewLifecycleOwner){
                adapter.setData(it)
                searchDebtsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                searchDebtsRecycler.adapter = adapter
            }
        }
    }
}