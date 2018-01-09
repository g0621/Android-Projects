package com.example.gyan.echo.Adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.gyan.echo.Activities.MainActivity
import com.example.gyan.echo.Fragments.MainScreenFragment
import com.example.gyan.echo.Fragments.SongPlayingFragment
import com.example.gyan.echo.R
import com.example.gyan.echo.Songs
import kotlinx.android.synthetic.main.row_custom_mainscreen_adapter.view.*

/**
 * Created by Gyan on 1/2/2018.
 */
class MainScreenadapter(_songDetails: ArrayList<Songs>,_context: Context): RecyclerView.Adapter<MainScreenadapter.MyViewHolder>(){
    override fun getItemCount(): Int {
        if (songsDetails == null) return 0
        else return (songsDetails as ArrayList<Songs>).size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val songObject = songsDetails?.get(position)
        holder?.trackTitle?.text = songObject?.songTitle
        holder?.trackArtist?.text = songObject?.Artist
        holder?.contentHolder?.setOnClickListener({
            var songPlayingFrag = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist",songObject?.Artist)
            args.putString("songTitle",songObject?.songTitle)
            args.putString("path",songObject?.songData)
            args.putInt("songId",songObject?.songID?.toInt() as Int)
            args.putInt("songPosition",position)
            args.putParcelableArrayList("songData",songsDetails)
            songPlayingFrag.arguments = args
            (mContext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFrag)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreen_adapter,parent,false)
        return MyViewHolder(itemView)
    }

    var songsDetails: ArrayList<Songs>? = null
    var mContext: Context? = null

    init {
        this.songsDetails = _songDetails
        this.mContext = _context
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var trackTitle: TextView? = null
        var trackArtist: TextView? = null
        var contentHolder: RelativeLayout? = null

        init {
            trackTitle = view.findViewById<TextView>(R.id.TrackTitle)
            trackArtist = view.findViewById<TextView>(R.id.TraceArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
        }
    }
}