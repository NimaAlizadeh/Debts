package com.example.debts.ui.activities

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.debts.R
import com.example.debts.database.DebtsDatabase
import com.example.debts.databinding.ActivityMainBinding
import com.example.debts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var popupMenu: PopupMenu

    @Inject
    lateinit var database: DebtsDatabase

    private lateinit var backup: RoomBackup

    private lateinit var file : File

    internal lateinit var callback: OnRefreshClickListener

    companion object{
        var neStatus: String = ""
        var appState = MutableLiveData<String>()
        var whichDebtCode: Int = 0
    }

    fun setOnRefreshClickListener(callBack: OnRefreshClickListener){
        this.callback = callBack
    }

    interface OnRefreshClickListener {
        fun refreshOnClick()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        appState.postValue(Constants.PAGE_ALL_DEBTS)

        backup = RoomBackup(this)

        binding.apply {
            navController = findNavController(R.id.main_navigation_host)
            mainBottomNavigation.setupWithNavController(navController)


            topButtonMoreMenu.setOnClickListener {
                initPopUpMenu(it.findViewById(R.id.topButton_more_menu))
                menuOnClickListener()
            }

            val liveObserver = Observer<String>{
                if(appState.value == Constants.PAGE_ALL_DEBTS)
                    topButtonMoreMenu.visibility = View.VISIBLE
                else
                    topButtonMoreMenu.visibility = View.INVISIBLE
            }

            appState.observe(this@MainActivity, liveObserver)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    private fun initPopUpMenu(view: View)
    {
        popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.main_menu_more, popupMenu.menu)
        popupMenu.show()
    }

    private fun menuOnClickListener()
    {
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId)
            {
                R.id.getBackup_menu -> {
                    backup.database(database)
                        .enableLogDebug(true)
                        .backupIsEncrypted(true)
                        .customEncryptPassword("theblb")
                        .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_INTERNAL)
                        .apply {
                            onCompleteListener { success, message, exitCode ->
                                Log.d("TAG", "success: $success, message: $message, exitCode: $exitCode")
                                if (success){
                                    Toast.makeText(this@MainActivity, "پشتیبان گیری انجام شد" + "\n" + "برنامه را بسته و دوباره اجرا کنید", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }.backup()
                    Log.d("TAG", "hello")
                    return@setOnMenuItemClickListener true
                }

                R.id.getRestore_menu -> {
                    backup
                        .database(database)
                        .enableLogDebug(true)
                        .backupIsEncrypted(true)
                        .customEncryptPassword("theblb")
                        .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_INTERNAL)
                        .apply {
                            onCompleteListener { success, message, exitCode ->
                                Log.d("TAG", "success: $success, message: $message, exitCode: $exitCode")
                                if (success) {
                                    Toast.makeText(this@MainActivity, "بازیابی اطلاعات انجام شد" + "\n" + "برنامه را بسته و دوباره اجرا کنید", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }
                        .restore()
                    return@setOnMenuItemClickListener true
                }

                R.id.doRefreshWithNet -> {
                    callback.refreshOnClick()
                    return@setOnMenuItemClickListener true
                }

                else -> return@setOnMenuItemClickListener false
            }
        }
    }
}