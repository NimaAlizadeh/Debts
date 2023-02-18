package com.example.debts.ui.activities

import android.os.Bundle
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
import com.example.debts.database.DebtEntity
import com.example.debts.database.DebtsDatabase
import com.example.debts.databinding.ActivityMainBinding
import com.example.debts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.util.*
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


//    private val NOTIFICATION_CHANNEL_ID = "10001"
//    private val default_notification_channel_id = "default"
//    val myCalendar: Calendar = Calendar.getInstance()

    companion object{
        var neStatus: String = ""
        var appState = MutableLiveData<String>()
        var whichDebtCode: Int = 0
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
        popupMenu.menuInflater.inflate(R.menu.backup_menu, popupMenu.menu)
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

                else -> return@setOnMenuItemClickListener false
            }
        }
    }
//
//    private fun scheduleNotification(notification: Notification, delay: Long) {
//        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
//        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, delay] = pendingIntent
//    }
//
//    private fun getNotification(): Notification {
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, default_notification_channel_id)
//        builder.setContentTitle("Scheduled Notification")
//        builder.setContentText("welcome buddy")
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
//        builder.setAutoCancel(true)
//        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
//        return builder.build()
//    }
//
//    fun setDate() {
//        val picker = PersianDatePickerDialog(this)
//            .setInitDate(Date().time)
//            .setPositiveButtonString("باشه")
//            .setNegativeButton("بیخیال")
//            .setTodayButton("امروز")
//            .setTodayButtonVisible(true)
//            .setMinYear(PersianDatePickerDialog.THIS_YEAR)
//            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
//            .setShowInBottomSheet(true)
//            .setActionTextColor(Color.rgb(249,164, 1))
//            .setListener(object : PersianPickerListener {
//                override fun onDateSelected(persianPickerDate: PersianPickerDate) {
//                    val cal = CustomDate()
//                    cal.persianToGregorian(persianPickerDate.persianYear,persianPickerDate.persianMonth,persianPickerDate.persianDay)
//                    myCalendar.set(Calendar.YEAR, cal.year)
//                    myCalendar.set(Calendar.MONTH, cal.month)
//                    myCalendar.set(Calendar.DAY_OF_MONTH, cal.day)
//
//                    scheduleNotification(getNotification(), myCalendar.time.time)
//                }
//                override fun onDismissed() {}
//            })
//
//        picker.show()
//    }
}