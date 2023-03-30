package com.example.debts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.debts.R
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity
import com.example.debts.databinding.RecyclerPaymentsItemLayoutBinding
import com.example.debts.utils.Constants
import javax.inject.Inject

class PaymentsAdapter @Inject constructor() : RecyclerView.Adapter<PaymentsAdapter.CustomViewHolder>(){
    lateinit var binding: RecyclerPaymentsItemLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = RecyclerPaymentsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun setData(tempItem: PaymentEntity)
        {
            binding.apply {
                recyclerPaymentItemAmount.text = String.format("%,d",tempItem.amount.toLong())
                recyclerPaymentItemDate.text = tempItem.date

                //on delete click listener
                recyclerPaymentItemDelete.setOnClickListener {
                    onItemClickListener?.let {
                        it(tempItem, "Delete Payment")
                    }
                }

            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<PaymentEntity>(){

        override fun areItemsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //on item select handling
    private var onItemClickListener: ((PaymentEntity, String) -> Unit)? = null
    fun setOnItemCLickListener(listener: (PaymentEntity, String) -> Unit){
        onItemClickListener = listener
    }
}