package com.example.gyan.echo.Fragments


import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.gyan.echo.Adapters.FavoriteAdapter
import com.example.gyan.echo.EchoDatabase
import com.example.gyan.echo.R
import com.example.gyan.echo.Songs


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragmnet : Fragment() {

    var myActivity: Activity? = null
    var noFavoriate: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var playPauseButtom: ImageButton? = null
    var songTitle: TextView? = null
    var recyclerView: RecyclerView?= null
    var trackPosition: Int = 0
    var favoriteContent: EchoDatabase?= null

    var refreshList: ArrayList<Songs>?= null
    var getListFromDataBase: ArrayList<Songs>? = null

    object Statified{
        var mediaPlayer: MediaPlayer? = null
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_favorite_fragmnet, container, false)
        activity.title = "Favorites"
        noFavoriate = view?.findViewById(R.id.noFavorites)
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenBarFavScreen)
        songTitle = view?.findViewById(R.id.songTitleFavScreen)
        playPauseButtom = view?.findViewById(R.id.play_pause_button_fav)
        recyclerView = view?.findViewById(R.id.favoriteRecycler)
        return  view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteContent = EchoDatabase((myActivity))
        displayFavBYSearch()
        bottomBarSetup()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
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
                nowPlayingBottomBar?.visibility = View.VISIBLE
            }else{
                nowPlayingBottomBar?.visibility = View.INVISIBLE
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun buttomBarClickHandler(){
        nowPlayingBottomBar?.setOnClickListener({
            Statified.mediaPlayer = SongPlayingFragment.Statified.mediaPlayer
            var songPlayingFrag = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist",SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("songTitle",SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putString("path",SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putInt("songId",SongPlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition", SongPlayingFragment.Statified.currentSongHelper?.currentPosition as Int)
            args.putParcelableArrayList("songData",SongPlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar","success")
            songPlayingFrag.arguments = args
            fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFrag)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })
        playPauseButtom?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackPosition = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playPauseButtom?.setBackgroundResource(R.drawable.play_icon)
            }else{
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackPosition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playPauseButtom?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }

    fun displayFavBYSearch(){
        if (favoriteContent?.checkSize() as Int > 0){
            refreshList = ArrayList<Songs>()
            getListFromDataBase = favoriteContent?.queryDBList()
            var fetchListdevice = getSongsFromPhone()
            if (fetchListdevice != null){
                for (i in 0..fetchListdevice?.size - 1){
                    for (j in 0..getListFromDataBase?.size as Int -1){
                        if (getListFromDataBase?.get(j)?.songID === fetchListdevice?.get(i)?.songID){
                            refreshList?.add((getListFromDataBase as ArrayList<Songs>)[j])
                        }
                    }
                }
            }
            if (refreshList == null){
                recyclerView?.visibility = View.INVISIBLE
                noFavoriate?.visibility = View.VISIBLE
            }else{
                var favoriteAdapter = FavoriteAdapter(refreshList as ArrayList<Songs>,myActivity as Context)
                val mLayoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = mLayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
        } else {
            recyclerView?.visibility = View.INVISIBLE
            noFavoriate?.visibility = View.VISIBLE
        }
    }
}// Required empty public constructor
