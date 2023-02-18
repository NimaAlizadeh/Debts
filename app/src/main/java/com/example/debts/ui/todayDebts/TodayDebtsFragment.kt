package com.example.debts.ui.todayDebts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.databinding.FragmentTodayDebtsBinding
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.DebtsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.utils.CustomDate
import com.example.debts.viewmodel.TodayDebtsVM
import dagger.hilt.android.AndroidEntryPoint
import matteocrippa.it.karamba.day
import matteocrippa.it.karamba.month
import matteocrippa.it.karamba.today
import matteocrippa.it.karamba.year
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TodayDebtsFragment : Fragment() {

    private lateinit var binding: FragmentTodayDebtsBinding

    val viewModel: TodayDebtsVM by viewModels()

    @Inject
    lateinit var adapter: DebtsAdapter

    @Inject
    lateinit var text: String

    @Inject
    lateinit var today: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodayDebtsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appState.postValue(Constants.PAGE_TODAY_DEBTS)

        binding.apply {

            val date = Date().today()
            val cal = CustomDate()
            cal.gregorianToPersian(date.year(), date.month(), date.day())
            today = cal.year.toString() + "/" + cal.month.toString() + "/" + cal.day.toString()
            viewModel.loadTodayDebts(today)


            viewModel.empty.observe(viewLifecycleOwner){
                if(it)
                {
                    todayDebtsEmptyLayout.visibility = View.VISIBLE
                    todayDebtsRecycler.visibility = View.GONE
                }
                else
                {
                    todayDebtsEmptyLayout.visibility = View.GONE
                    todayDebtsRecycler.visibility = View.VISIBLE
                }
            }

            adapter.setOnItemCLickListener { debtEntity, status ->
                when(status){
                    Constants.ON_CLICK_GOTO_DETAIL -> {
                        val direction = TodayDebtsFragmentDirections.actionTodayDebtsFragmentToDetailsFragment()
                        MainActivity.whichDebtCode = debtEntity.debtId
                        findNavController().navigate(direction)
                    }

                    Constants.ON_CLICK_DELETE -> {
                        val alert = AlertDialog.Builder(requireContext())
                        alert.setTitle("حذف")
                            .setMessage("آیا برای حذف این مورد اطمینان دارید؟")
                            .setPositiveButton("بله") { _, _ ->
                                viewModel.deleteDebt(debtEntity)
                                viewModel.loadTodayDebts(text)
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
                                "بدهی خانم/آقای " + debtEntity.fullName + " تا تاریخ " + debtEntity.debtRemainingDate + " به مبلغ " + String.format("%,d",debtEntity.debtRemaining.toLong()) + " می باشد.")
                            type = "text/plain"
                        }
                        startActivity(sendIntent)
                    }
                }
            }

            viewModel.todayDebtsList.observe(viewLifecycleOwner){
                adapter.setData(it)
                todayDebtsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                todayDebtsRecycler.adapter = adapter
            }
        }
    }
}