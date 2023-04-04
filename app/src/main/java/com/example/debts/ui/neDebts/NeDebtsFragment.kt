package com.example.debts.ui.neDebts

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.debts.api.ApiService
import com.example.debts.database.DebtEntity
import com.example.debts.databinding.FragmentNeDebtsBinding
import com.example.debts.models.registerBody
import com.example.debts.ui.activities.MainActivity
import com.example.debts.utils.Constants
import com.example.debts.utils.CustomDate
import com.example.debts.viewmodel.NeDebtsVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import matteocrippa.it.karamba.day
import matteocrippa.it.karamba.month
import matteocrippa.it.karamba.today
import matteocrippa.it.karamba.year
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class NeDebtsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNeDebtsBinding

    private val viewModel: NeDebtsVM by viewModels()

    @Inject
    lateinit var str: String

    var timeStamp: Long = 0L

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNeDebtsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        str = ""

        binding.apply {
            timeStamp = 0L

            // close/closing/dismiss the page
            dismissButton.setOnClickListener {
                this@NeDebtsFragment.dismiss()
            }

            neDebtsRemainingDateEdt.setOnClickListener {
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
                            str = persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay.toString()
                            neDebtsRemainingDateEdt.setText(str)
                            timeStamp = persianPickerDate.timestamp
                        }
                        override fun onDismissed() {}
                    })

                picker.show()
            }

            if(MainActivity.neStatus == Constants.ON_CLICK_ADD){

                //this is for focusing on title when it comes to this page
                neDebtsFullNameEdt.requestFocus()
                val keyboard: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.showSoftInput(neDebtsFullNameEdt, InputMethodManager.SHOW_IMPLICIT)

                //set toolbar title
                neDebtsTitle.text = "بدهی جدید"

                //set text to button
                neDebtsButton.text = "ثبت"
                val newDebtEntity = DebtEntity()

                //handling the add button
                neDebtsButton.setOnClickListener {
                    if(neDebtsFullNameEdt.text.toString().isNotEmpty() && neDebtsRemainingDebtEdt.text.toString().isNotEmpty() &&
                        neDebtsGoldPriceEdt.text.toString().isNotEmpty() && neDebtsRemainingDateEdt.text.toString().isNotEmpty()){
                        //filling the information from user
                        newDebtEntity.old_MOId = 0
                        newDebtEntity.fullName = neDebtsFullNameEdt.text.toString()
                        newDebtEntity.phoneNumber = neDebtsPhoneNumberEdt.text.toString()
                        newDebtEntity.goldPrice = neDebtsGoldPriceEdt.text.toString()
                        newDebtEntity.moreDetails = neDebtsMoreDetailsEdt.text.toString()
                        newDebtEntity.debtRemaining = neDebtsRemainingDebtEdt.text.toString()
                        newDebtEntity.debtRemainingDate = str
                        newDebtEntity.userId = Constants.USER_ID
                        newDebtEntity.debtRemainingTimeStamp = timeStamp
                        val today = Date().today()
                        newDebtEntity.updatedAt = today.time
                        newDebtEntity.createdAt = today.time
                        val cal = CustomDate()
                        cal.gregorianToPersian(today.year(), today.month(), today.day())
                        newDebtEntity.buyDate = cal.year.toString() + "/" + cal.month.toString() + "/" + cal.day.toString()


                        viewModel.insertDebt(newDebtEntity)
                        Toast.makeText(requireContext(), "بدهی ثبت شد", Toast.LENGTH_SHORT).show()

                        this@NeDebtsFragment.dismiss()
                    }
                    else{
                        Toast.makeText(requireContext(), "فیلد هارا پر کنید", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if(MainActivity.neStatus == Constants.ON_CLICK_EDIT){
                //set toolbar title
                neDebtsTitle.text = "ویرایش بدهی"

                //set text to button
                neDebtsButton.text = "ویرایش"
                val editingEntity = DebtEntity()

                //get the editable entity from database
                viewModel.getSingleDebt(MainActivity.whichDebtCode)
                viewModel.response.observe(viewLifecycleOwner){
                    neDebtsFullNameEdt.setText(it.fullName)
                    neDebtsPhoneNumberEdt.setText(it.phoneNumber)
                    neDebtsGoldPriceEdt.setText(it.goldPrice)
                    neDebtsMoreDetailsEdt.setText(it.moreDetails)
                    neDebtsRemainingDebtEdt.setText(it.debtRemaining)
                    neDebtsRemainingDateEdt.setText(it.debtRemainingDate)

                    editingEntity.MOId = it.MOId
                    editingEntity.buyDate = it.buyDate
                    editingEntity.debtRemainingTimeStamp = it.debtRemainingTimeStamp
                    editingEntity.createdAt = it.createdAt
                    editingEntity.DBId = it.DBId
                    editingEntity.isDeleted = it.isDeleted
                    editingEntity.old_MOId = it.old_MOId
                }

                neDebtsButton.setOnClickListener {
                    if(neDebtsFullNameEdt.text.toString().isNotEmpty() && neDebtsRemainingDebtEdt.text.toString().isNotEmpty()){

                        editingEntity.fullName = neDebtsFullNameEdt.text.toString()
                        editingEntity.phoneNumber = neDebtsPhoneNumberEdt.text.toString()
                        editingEntity.goldPrice = neDebtsGoldPriceEdt.text.toString()
                        editingEntity.moreDetails = neDebtsMoreDetailsEdt.text.toString()
                        editingEntity.debtRemaining = neDebtsRemainingDebtEdt.text.toString()
                        editingEntity.debtRemainingDate = neDebtsRemainingDateEdt.text.toString()
                        if(timeStamp != 0L)
                            editingEntity.debtRemainingTimeStamp = timeStamp
                        editingEntity.updatedAt = Date().today().time
                        editingEntity.userId = Constants.USER_ID

                        viewModel.updateDebt(editingEntity)

                        Toast.makeText(requireContext(), "بدهی ویرایش شد", Toast.LENGTH_SHORT).show()
                        if(MainActivity.appState.value == Constants.PAGE_SEARCH)
                            MainActivity.reload.postValue(Constants.PAGE_SEARCH)
                        else if(MainActivity.appState.value == Constants.PAGE_TODAY_DEBTS)
                            MainActivity.reload.postValue(Constants.PAGE_TODAY_DEBTS)
                        this@NeDebtsFragment.dismiss()
                    }
                    else{
                        Toast.makeText(requireContext(), "فیلد هارا پر کنید", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}