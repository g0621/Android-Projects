package com.example.gyan.echo.Fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.gyan.echo.Adapters.MainScreenadapter
import com.example.gyan.echo.R
import com.example.gyan.echo.Songs
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MainScreenFragment : Fragment() {

    var getSongsList: ArrayList<Songs>? = null
    var nowPlayingButtomBar: RelativeLayout? = null
    var trackPosition: Int = 0
    var playPauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var visibleLayout: RelativeLayout? = null
    var noSong: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var myActivity: Activity? = null
    var _mainScreenAdapter: MainScreenadapter? = null

    object Statified{
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true)
        activity.title = "All Songs"
        visibleLayout = view?.findViewById(R.id.visible_layout)
        playPauseButton = view?.findViewById(R.id.play_pause_button)
        songTitle = view?.findViewById(R.id.songTitleMainScreen)
        nowPlayingButtomBar = view?.findViewById(R.id.hiddenBarMainScreen)
        noSong = view?.findViewById(R.id.noSongs)
        recyclerView = view.findViewById(R.id.content_main)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getSongsList = getSongsFromPhone()
        val pref = activity.getSharedPreferences("action_sort",Context.MODE_PRIVATE)
        val action_sort_asec = pref.getString("action_sort_asec","true")
        val action_sort_recent = pref.getString("action_sort_recent","false")
        if (getSongsList == null){
            visibleLayout?.visibility = View.INVISIBLE
            noSong?.visibility = View.VISIBLE
        }else{
            _mainScreenAdapter = MainScreenadapter(getSongsList as ArrayList<Songs>,myActivity as Context)
            val mLayoutManager = LinearLayoutManager(myActivity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = _mainScreenAdapter
        }
        if (getSongsList != null){
            if (action_sort_asec!!.equals("true",true)){
                Collections.sort(getSongsList,Songs.Statified.nameComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }else if(action_sort_recent!!.equals("true",true)){
                Collections.sort(getSongsList,Songs.Statified.dateComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }
        }
        bottomBarSetup()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main,menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher = item?.itemId
        if (switcher == R.id.action_sort_asec){
            val editor = myActivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_asec","true")
            editor?.putString("action_sort_recent","false")?.apply()
            if (getSongsList != null){
                Collections.sort(getSongsList,Songs.Statified.nameComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false
        }else if (switcher == R.id.action_sort_recent){
            val editor = myActivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_asec","false")
            editor?.putString("action_sort_recent","true")?.apply()
            if (getSongsList != null){
                Collections.sort(getSongsList,Songs.Statified.dateComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    fun getSongsFromPhone(): ArrayList<Songs>{
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver?.query(songUri,null,null,null,null)
        if (songCursor != null && songCursor.moveToFirst()){
            val songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()){
                var currentId = songCursor.getLong(songID)
                var currentTitle = songCursor.getString(songTitle)
                var currentArtist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var curdateIndex = songCursor.getLong(dateIndex)

                arrayList.add(Songs(currentId,currentTitle,currentArtist,currentData,curdateIndex))
            }
        }
        return arrayList
    }

    fun bottomBarSetup(){
        try{
            buttomBarClickHandler()
            songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                SongPlayingFragment.Staticated.onSongComplete()
            })
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                nowPlayingButtomBar?.visibility = View.VISIBLE
            }else{
                nowPlayingButtomBar?.visibility = View.INVISIBLE
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun buttomBarClickHandler(){
        nowPlayingButtomBar?.setOnClickListener({
            MainScreenFragment.Statified.mediaPlayer = SongPlayingFragment.Statified.mediaPlayer
            var songPlayingFrag = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist",SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("songTitle",SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putString("path",SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putInt("songId",SongPlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition", SongPlayingFragment.Statified.currentSongHelper?.currentPosition as Int)
            args.putParcelableArrayList("songData",SongPlayingFragment.Statified.fetchSongs)
            args.putString("MainBottomBar","success")
            songPlayingFrag.arguments = args
            fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFrag)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })
        playPauseButton?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackPosition = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            }else{
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackPosition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }

}// Required empty public constructor
