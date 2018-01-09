package com.example.gyan.echo.Utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.example.gyan.echo.Activities.MainActivity
import com.example.gyan.echo.Fragments.SongPlayingFragment
import com.example.gyan.echo.R

/**
 * Created by Gyan on 1/5/2018.
 */
class CaptureBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_NEW_OUTGOING_CALL){
            try{
                MainActivity.Statified.notificationManager?.cancel(1997)
                if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                    SongPlayingFragment.Statified.mediaPlayer?.pause()
                    SongPlayingFragment.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }else{
            val tm: TelephonyManager = context?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            when(tm?.callState){
                TelephonyManager.CALL_STATE_RINGING -> {
                    try{
                        MainActivity.Statified.notificationManager?.cancel(1997)
                        if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                            SongPlayingFragment.Statified.mediaPlayer?.pause()
                            SongPlayingFragment.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else -> {

            }
            }
        }
    }

}