package com.example.debts.ui.details

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debts.R
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity
import com.example.debts.databinding.FragmentDetailsBinding
import com.example.debts.ui.activities.MainActivity
import com.example.debts.ui.adapters.PaymentsAdapter
import com.example.debts.ui.neDebts.NeDebtsFragment
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.DetailsVM
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import matteocrippa.it.karamba.today
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    val viewModel: DetailsVM by viewModels()

    @Inject
    lateinit var debtEntity: DebtEntity

    @Inject
    lateinit var adapter: PaymentsAdapter

    @Inject
    lateinit var date: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appState.postValue(Constants.PAGE_DETAILS)

        binding.apply {
            detailsToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }


            viewModel.loadDetails(MainActivity.whichDebtCode)
            viewModel.detailsLiveData.observe(viewLifecycleOwner){ entity ->
                debtEntity = entity
                fillText(detailsFullName, entity.fullName)
                fillText(detailsPhoneNumber, entity.phoneNumber)
                fillText(detailsRemainingDebtDate, entity.debtRemainingDate)
                fillText(detailsBuyDate, entity.buyDate)
                fillText(detailsMoreDetails, entity.moreDetails)
                fillText(detailsGoldPrice, String.format("%,d",entity.goldPrice.toLong()))
                fillText(detailsRemainingDebt, String.format("%,d",entity.debtRemaining.toLong()))




                if(detailsPhoneNumber.text != "چیزی برای نمایش نیست"){
                    detailsPhoneNumber.setOnClickListener {
                        val callIntent = Intent(Intent.ACTION_DIAL)
                        callIntent.data = Uri.parse("tel:" + entity.phoneNumber)
                        startActivity(callIntent)
                    }
                }

                viewModel.getDebtWithPayments(entity.MOId)
            }

            viewModel.paymentsLiveData.observe(viewLifecycleOwner){
                adapter.differ.submitList(it.paymentList)
                detailsPaymentRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                detailsPaymentRecycler.adapter = adapter
            }

            Toast.makeText(requireContext(), debtEntity.DBId, Toast.LENGTH_SHORT).show()

            detailsPaymentDateEdt.setOnClickListener {
                val picker = PersianDatePickerDialog(requireContext())
                    .setInitDate(Date().time)
                    .setPositiveButtonString("باشه")
                    .setNegativeButton("بیخیال")

                    .setTodayButton("امروز")
                    .setTodayButtonVisible(true)
                    .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                    .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                    .setShowInBottomSheet(true)
                    .setActionTextColor(Color.rgb(249,164, 1))
                    .setListener(object : PersianPickerListener {
                        override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                            date = persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay.toString()
                            detailsPaymentDateEdt.setText(date)
                        }
                        override fun onDismissed() {}
                    })

                picker.show()
            }

            //insert or set payment when clicking button
            setPaymentButton.setOnClickListener {
                if(detailsPaymentDateEdt.text!!.isNotEmpty() && detailsPaymentAmountEdt.text!!.isNotEmpty()){

                    val remaining = debtEntity.debtRemaining.toLong()
                    val amount = detailsPaymentAmountEdt.text.toString()
                    if((remaining - amount.toLong()) >= 0){
                        val insertingEntity = PaymentEntity(debtCreatorId = debtEntity.MOId, date = date, amount = amount)
                        viewModel.insertPayment(insertingEntity)
                        detailsPaymentDateEdt.text?.clear()
                        detailsPaymentAmountEdt.text?.clear()
                        detailsPaymentAmountEdt.clearFocus()
                        val doMinus = remaining - amount.toLong()
                        detailsRemainingDebt.text = String.format("%,d",doMinus)
                        val updatingDebt = debtEntity
                        updatingDebt.debtRemaining = doMinus.toString()
                        updatingDebt.lastModified = Date().today().time
                        viewModel.updateDebt(updatingDebt)
                        viewModel.getDebtWithPayments(debtEntity.MOId)
                        Toast.makeText(requireContext(), "پرداخت ثبت شد", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "مبلغ پرداختی بیشتر از مانده بدهی است", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(requireContext(), "فیلد هارا پر کنید", Toast.LENGTH_SHORT).show()
                }
            }

            //delete on click listener handling
            adapter.setOnItemCLickListener { paymentEntity, s ->
                if(s == "Delete Payment"){
                    val alert = AlertDialog.Builder(requireContext())
                    alert.setTitle("حذف پرداختی")
                        .setMessage("آیا برای حذف این پرداخت اطمینان دارید؟")
                        .setPositiveButton("بله") { _, _ ->
                            val updatingDebt = debtEntity
                            val remain = updatingDebt.debtRemaining.toLong() + paymentEntity.amount.toLong()
                            updatingDebt.debtRemaining = remain.toString()
                            updatingDebt.lastModified = Date().today().time
                            viewModel.updateDebt(updatingDebt)
                            viewModel.deletePayment(paymentEntity)
                            detailsRemainingDebt.text = String.format("%,d",remain)
                            viewModel.getDebtWithPayments(debtEntity.MOId)
                        }
                        .setNegativeButton("خیر", null)
                        .setCancelable(false)
                        .create()
                        .show()
                }
            }


            detailsToolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.recycler_menu_detail_delete -> {
                        val alert = AlertDialog.Builder(requireContext())
                        alert.setTitle("حذف بدهی")
                            .setMessage("آیا برای حذف این بدهی اطمینان دارید؟")
                            .setPositiveButton("بله") { _, _ ->
                                viewModel.deleteDebt(debtEntity)
                                findNavController().popBackStack()
                            }
                            .setNegativeButton("خیر", null)
                            .setCancelable(false)
                            .create()
                            .show()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.recycler_menu_detail_share -> {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "بدهی خانم/آقای " + debtEntity.fullName + " تا تاریخ " + debtEntity.debtRemainingDate + " به مبلغ " + String.format("%,d",debtEntity.debtRemaining.toLong()) + " ریال می باشد.")
                            type = "text/plain"
                        }
                        startActivity(sendIntent)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.recycler_menu_detail_edit -> {
                        findNavController().popBackStack()
                        NeDebtsFragment().show(parentFragmentManager, NeDebtsFragment().tag)
                        MainActivity.neStatus = Constants.ON_CLICK_EDIT
                        MainActivity.whichDebtCode = debtEntity.MOId

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
        }
    }

    private fun fillText(view: TextView, text: String){
        if(text.isNotEmpty()){
            view.text = text
            view.setTextColor(Color.rgb(0,0,0))
        }
        else{
            view.text = "چیزی برای نمایش نیست"
            view.setTextColor(Color.rgb(213,213,213))
        }
    }
}