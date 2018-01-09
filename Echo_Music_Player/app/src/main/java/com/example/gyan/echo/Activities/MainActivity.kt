package com.example.gyan.echo.Activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.example.gyan.echo.Adapters.NavigationDrawerAdapter
import com.example.gyan.echo.Fragments.MainScreenFragment
import com.example.gyan.echo.Fragments.SongPlayingFragment
import com.example.gyan.echo.R
import android.R.attr.description
import android.graphics.Color
import android.support.v4.app.NotificationCompat


class MainActivity : AppCompatActivity(){


    var navigationDrawerIconList: ArrayList<String> = arrayListOf()
    var trackNotificationBuilder: Notification?= null
    var image_for_navDrawer: IntArray = intArrayOf(R.drawable.navigation_allsongs,R.drawable.navigation_favorites,R.drawable.navigation_settings,R.drawable.navigation_aboutus)

    object Statified{
        var drawerLayout: DrawerLayout? = null
        var notificationManager: NotificationManager ?= null
        var notificationChannel: NotificationChannel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        MainActivity.Statified.drawerLayout = findViewById(R.id.drawer_layout)
        navigationDrawerIconList.addAll(arrayListOf("All Songs","Favorites","Settings","About us"))

        val toggle = ActionBarDrawerToggle(this@MainActivity,MainActivity.Statified.drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragment,"MainScreenFrag")
                .commit()

        var _navAdapter = NavigationDrawerAdapter(navigationDrawerIconList,image_for_navDrawer,this)
        _navAdapter.notifyDataSetChanged()

        var recyclerView = findViewById<RecyclerView>(R.id.nav_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = _navAdapter
        recyclerView.setHasFixedSize(true)

        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Statified.notificationChannel = NotificationChannel("channel1","MusicChannel",NotificationManager.IMPORTANCE_HIGH)
            Statified.notificationChannel?.description = "Now Playing"
            Statified.notificationChannel?.enableLights(true)
            Statified.notificationChannel?.lightColor = Color.RED
            Statified.notificationChannel?.enableVibration(true)
            Statified.notificationChannel?.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            Statified.notificationManager?.createNotificationChannel(Statified.notificationChannel)
        }
        var intent = Intent(this@MainActivity,MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity,System.currentTimeMillis().toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)
        trackNotificationBuilder = NotificationCompat.Builder(this@MainActivity,"channel1")
                .setContentTitle("A Track is Playing In Background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()

    }

    override fun onStop() {
        super.onStop()
        try {
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                Statified.notificationManager?.notify(1997,trackNotificationBuilder)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statified.notificationManager?.cancel(1997)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1997)
            Log.i("this","notificationCancled")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
