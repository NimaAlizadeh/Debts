package com.example.debts.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.debts.R
import com.example.debts.database.DebtEntity
import com.example.debts.databinding.RecyclerItemLayoutBinding
import com.example.debts.utils.Constants
import com.example.debts.utils.CustomDate
import matteocrippa.it.karamba.day
import matteocrippa.it.karamba.month
import matteocrippa.it.karamba.today
import matteocrippa.it.karamba.year
import java.util.*
import javax.inject.Inject


class DebtsAdapter @Inject constructor(): RecyclerView.Adapter<DebtsAdapter.CustomViewHolder>()
{
    private lateinit var binding: RecyclerItemLayoutBinding
    private var dataList = emptyList<DebtEntity>()

    lateinit var context: Context

    private lateinit var popupMenu: PopupMenu

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = RecyclerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(dataList[position])
        holder.setIsRecyclable(false)

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 700
        holder.itemView.startAnimation(anim)
    }

    override fun getItemCount() = dataList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(entity: DebtEntity)
        {
            binding.apply {

                if(Date().today().compareTo(Date(entity.debtRemainingTimeStamp)) == -1)
                {
                    recyclerStatus.text = "مانده به موعد"
                    recyclerStatus.setTextColor(Color.GREEN)
                }
                else
                {
                    val today = Date().today()
                    val persianToday = CustomDate()
                    persianToday.gregorianToPersian(today.year(), today.month(), today.day())
                    val list = entity.debtRemainingDate.split("/")
                    if(list[2] == persianToday.day.toString() && list[1] == persianToday.month.toString() && list[0] == persianToday.year.toString()){
                        recyclerStatus.text = "موعد پرداخت"
                        recyclerStatus.setTextColor(Color.BLUE)
                    }else{
                        recyclerStatus.text = "گذشت از موعد"
                        recyclerStatus.setTextColor(Color.RED)
                    }
                }



                recyclerFullName.text = entity.fullName
                recyclerRemainingDebt.text = String.format("%,d",entity.debtRemaining.toLong())
                recyclerRemainingDate.text = entity.debtRemainingDate


                //on click for opening each item
                recyclerWholeLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(entity, Constants.ON_CLICK_GOTO_DETAIL)
                    }
                }

                recyclerMoreMenu.setOnClickListener {
                    initPopUpMenu(it.findViewById(R.id.recycler_more_menu))
                    menuOnClickListener(entity)
                }

                //long click on recycler items to open the recycler menu
                recyclerWholeLayout.setOnLongClickListener {
                    initPopUpMenu(it.findViewById(R.id.recycler_more_menu))
                    menuOnClickListener(entity)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    //on item select handling
    private var onItemClickListener: ((DebtEntity, String) -> Unit)? = null
    fun setOnItemCLickListener(listener: (DebtEntity, String) -> Unit){
        onItemClickListener = listener
    }


    fun setData(newListData: List<DebtEntity>)
    {
        val notesDiffUtils = NotesDiffUtils(dataList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        dataList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<DebtEntity>, private val newItem: List<DebtEntity>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldItem.size

        override fun getNewListSize() = newItem.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }

    private fun initPopUpMenu(view: View)
    {
        popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.recycler_items_more_menu, popupMenu.menu)
        popupMenu.show()
    }

    private fun menuOnClickListener(entity: DebtEntity)
    {
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId)
            {
                R.id.recycler_menu_detail_delete -> {
                    onItemClickListener?.let {
                        it(entity, Constants.ON_CLICK_DELETE)
                    }
                    return@setOnMenuItemClickListener true
                }

                R.id.recycler_menu_detail_share -> {
                    onItemClickListener?.let {
                        it(entity, Constants.ON_CLICK_SHARE)
                    }
                    return@setOnMenuItemClickListener true
                }

                R.id.recycler_menu_detail_edit -> {
                    onItemClickListener?.let {
                        it(entity, Constants.ON_CLICK_EDIT)
                    }
                    return@setOnMenuItemClickListener true
                }

                else -> return@setOnMenuItemClickListener false
            }
        }
    }
}