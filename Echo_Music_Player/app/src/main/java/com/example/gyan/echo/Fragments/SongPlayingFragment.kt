package com.example.gyan.echo.Fragments


import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.example.gyan.echo.CurrentSongHelper
import com.example.gyan.echo.EchoDatabase
import com.example.gyan.echo.R
import com.example.gyan.echo.Songs
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class SongPlayingFragment : Fragment() {

    object Statified{
        var myActivity: Activity? = null
        var mediaPlayer: MediaPlayer? = null
        var startTimeText: TextView? = null
        var endTimeText: TextView? = null
        var playPauseImageButton: ImageButton? = null
        var previousImageButton: ImageButton? = null
        var nextImageButton: ImageButton? = null
        var loopImageButton: ImageButton? = null
        var favImagButton: ImageButton? = null
        var seekBar: SeekBar? = null
        var songArtistView: TextView? = null
        var songTitleView: TextView? = null
        var shuffleImageButton: ImageButton? = null
        var currentSongHelper: CurrentSongHelper? = null
        var currentPosition: Int = 0
        var fetchSongs: ArrayList<Songs>? = null
        var audioVisualization: AudioVisualization? = null
        var glView: GLAudioVisualizationView? = null
        var favoriteContent: EchoDatabase? = null
        var mSensorManager: SensorManager?= null
        var mSensorListner: SensorEventListener? = null

        var updateSongTimer = object : Runnable{
            override fun run() {
                val getCurrent = mediaPlayer?.currentPosition

                val min = TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long)
                val sec = TimeUnit.MILLISECONDS.toSeconds(getCurrent?.toLong() as Long)  - TimeUnit.MINUTES.toSeconds(min)
                startTimeText?.text = String.format("%d:%d",min,sec)

                seekBar?.progress = getCurrent.toInt()
                Handler().postDelayed(this,1000)
            }

        }
    }
    object Staticated{
        var MY_PREF_SHUFFLE = "Shuffle feature"
        var MY_PREF_LOOP = "Loop feature"

        fun onSongComplete(){
            if (Statified.currentSongHelper?.isShuffle as Boolean){
                playNext("PlayNextLikeNormalShuffle")
                Statified.currentSongHelper?.isPlaying = true
            }else {
                Statified.currentSongHelper?.isPlaying = true
                if (Statified.currentSongHelper?.isLoop as Boolean){
                    fetchPlay(Statified.currentPosition)
                }else{
                    playNext("PlayNextNormal")
                }
            }
        }
        fun updateTextView(songTitle: String,songArtist: String){
            var artist1 = songArtist
            var title1 = songTitle
            if (songTitle.equals("<unknown>",true)) {
                title1 = "Unkonow"
            }
            if (songArtist.equals("<unknown>",true)){
                artist1 = "Unknown"
            }
            Statified.songTitleView?.text = title1
            Statified.songArtistView?.text = artist1
        }

        fun processInformation(mediaPlayer: MediaPlayer){
            val finalTime = mediaPlayer.duration
            val startTime = mediaPlayer.currentPosition

            Statified.seekBar?.max = finalTime
            Statified.seekBar?.progress = startTime

            val min = TimeUnit.MILLISECONDS.toMinutes(startTime?.toLong())
            val sec = TimeUnit.MILLISECONDS.toSeconds(startTime?.toLong())  - TimeUnit.MINUTES.toSeconds(min)
            Statified.startTimeText?.text = String.format("%d:%d",min,sec)

            val min1 = TimeUnit.MILLISECONDS.toMinutes(finalTime?.toLong())
            val sec1 = TimeUnit.MILLISECONDS.toSeconds(finalTime?.toLong())  - TimeUnit.MINUTES.toSeconds(min1)
            Statified.endTimeText?.text = String.format("%d:%d",min1,sec1)
            Handler().postDelayed(Statified.updateSongTimer,1000)
        }
        fun playNext(check: String){
            if (check.equals("PlayNextNormal",true)){
                Statified.currentPosition += 1
            }else if(check.equals("PlayNextLikeNormalShuffle",true)){
                var randomObj = Random()
                var randomPosition = randomObj.nextInt(Statified.fetchSongs?.size?.plus(1) as Int)
                Statified.currentPosition = randomPosition
            }
            if (Statified.currentPosition == Statified.fetchSongs?.size){
                Statified.currentPosition = 0
            }
            Statified.currentSongHelper?.isLoop = false
            if (Statified.currentSongHelper?.isPlaying as Boolean) {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            } else {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }
            fetchPlay(Statified.currentPosition)
        }

        fun playPrevious() {
            Statified.currentPosition -= 1
            if (Statified.currentPosition == -1) Statified.currentPosition = (Statified.fetchSongs?.size?.minus(1) as Int)
            if (Statified.currentSongHelper?.isPlaying as Boolean) {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            } else {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }
            Statified.currentSongHelper?.isLoop = false
            fetchPlay(Statified.currentPosition)
        }

        fun fetchPlay(currentPosition: Int){
            var nextSong = Statified.fetchSongs?.get(currentPosition)
            Statified.currentSongHelper?.songPath = nextSong?.songData
            Statified.currentSongHelper?.songTitle = nextSong?.songTitle
            Statified.currentSongHelper?.songArtist = nextSong?.Artist
            Statified.currentSongHelper?.songId = nextSong?.songID as Long
            Statified.currentSongHelper?.currentPosition = currentPosition
            updateTextView(Statified.currentSongHelper?.songTitle as String,Statified.currentSongHelper?.songArtist as String)
            Statified.mediaPlayer?.reset()
            try {
                Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(Statified.currentSongHelper?.songPath))
                Statified.mediaPlayer?.prepare()
                Statified.mediaPlayer?.start()
                processInformation(Statified.mediaPlayer as MediaPlayer)
            }catch (e: Exception){
                e.printStackTrace()
            }
            if (Statified.favoriteContent?.checkifExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean){
                Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_on))
            } else {
                Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_off))
            }
        }
    }


    var mAcceleration: Float = 0f
    var mAccelerationCurrent: Float = 0f
    var mAccelerationLast: Float = 0f

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_song_playing, container, false)
        setHasOptionsMenu(true)
        activity.title = "Now Playing"
        Statified.seekBar = view?.findViewById(R.id.seekBar)
        Statified.startTimeText = view?.findViewById(R.id.startTime)
        Statified.endTimeText = view?.findViewById(R.id.endTime)
        Statified.playPauseImageButton = view?.findViewById(R.id.PlayPauseButton)
        Statified.nextImageButton = view?.findViewById(R.id.NextButton)
        Statified.previousImageButton = view?.findViewById(R.id.PreviousButton)
        Statified.loopImageButton = view?.findViewById(R.id.LoopButton)
        Statified.shuffleImageButton = view?.findViewById(R.id.ShuffleButton)
        Statified.songArtistView = view?.findViewById(R.id.song_artist)
        Statified.songTitleView = view?.findViewById(R.id.song_title)
        Statified.favImagButton = view?.findViewById(R.id.favouriteIcon)
        Statified.glView = view?.findViewById(R.id.visualizer_view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Statified.audioVisualization = Statified.glView as AudioVisualization
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Statified.myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        Statified.myActivity = activity
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.song_playing_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        val item: MenuItem? = menu?.findItem(R.id.action_redirect)
        item?.isVisible = true
        val item2: MenuItem? = menu?.findItem(R.id.action_sort)
        item2?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_redirect -> {
                Statified.myActivity?.onBackPressed()
                return false
            }
        }
        return false
    }


    override fun onPause() {
        Statified.audioVisualization?.onPause()
        super.onPause()
        Statified.mSensorManager?.unregisterListener(Statified.mSensorListner)
    }

    //Check Here
    override fun onDestroyView() {
        Statified.audioVisualization?.release()
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Statified.mSensorManager = Statified.myActivity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAcceleration = 0.0f
        mAccelerationCurrent = SensorManager.GRAVITY_EARTH
        mAccelerationLast = SensorManager.GRAVITY_EARTH
        bindShakeListener()
    }

    override fun onResume() {
        super.onResume()
        Statified.audioVisualization?.onResume()
        Statified.mSensorManager?.registerListener(Statified.mSensorListner,Statified.mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Statified.currentSongHelper = CurrentSongHelper()
        Statified.currentSongHelper?.isPlaying = true
        Statified.currentSongHelper?.isLoop = false
        Statified.currentSongHelper?.isShuffle = false
        Statified.favoriteContent = EchoDatabase(Statified.myActivity)

        var path: String? = null
        var songTitle: String? = null
        var songArtist: String?= null
        var songId: Long = 0
        try {
            path = arguments.getString("path")
            songTitle = arguments.getString("songTitle")
            songArtist = arguments.getString("songArtist")
            songId = arguments.getInt("songId").toLong()
            Statified.currentPosition = arguments.getInt("songPosition")
            Statified.fetchSongs = arguments.getParcelableArrayList("songData")
            Statified.currentSongHelper?.songPath = path
            Statified.currentSongHelper?.songTitle = songTitle
            Statified.currentSongHelper?.songArtist = songArtist
            Statified.currentSongHelper?.songId = songId
            Statified.currentSongHelper?.currentPosition = Statified.currentPosition

            Staticated.updateTextView(songTitle,songArtist)

        }catch (e: Exception){
            e.printStackTrace()
        }

        var fromFavButtom = arguments.get("FavBottomBar") as? String
        var fromMainButtom = arguments.get("MainBottomBar") as? String
        if (fromFavButtom != null){
            Statified.mediaPlayer = FavoriteFragmnet.Statified.mediaPlayer
        }else if(fromMainButtom != null){
            Statified.mediaPlayer = MainScreenFragment.Statified.mediaPlayer
        } else {
            Statified.mediaPlayer = MediaPlayer()
            Statified.mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(path))
                Statified.mediaPlayer?.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Statified.mediaPlayer?.start()
        }
        Staticated.processInformation(Statified.mediaPlayer as MediaPlayer)
        if (Statified.currentSongHelper?.isPlaying as Boolean){
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
        }
        Statified.mediaPlayer?.setOnCompletionListener {
            Staticated.onSongComplete()
        }
        clickHandler()
        var VisualizationHandler = DbmHandler.Factory.newVisualizerHandler(Statified.myActivity as Context,0)
        Statified.audioVisualization?.linkTo(VisualizationHandler)

        var isShuffleAllowed = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_SHUFFLE,Context.MODE_PRIVATE)?.getBoolean("feature",false)
        var isLoopAllowed = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_LOOP,Context.MODE_PRIVATE)?.getBoolean("feature",false)

        if (isShuffleAllowed as Boolean){
            Statified.currentSongHelper?.isShuffle = true
            Statified.currentSongHelper?.isLoop = false
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
        }else{
            Statified.currentSongHelper?.isShuffle = false
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
        }
        if (isLoopAllowed as Boolean){
            Statified.currentSongHelper?.isShuffle = false
            Statified.currentSongHelper?.isLoop = true
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_icon)
        }else{
            Statified.currentSongHelper?.isLoop = false
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
        }

        if (Statified.favoriteContent?.checkifExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean){
            Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_on))
        } else {
            Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_off))
        }
    }

    fun clickHandler(){
        Statified.seekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (Statified.mediaPlayer?.isPlaying as Boolean && fromUser) Statified.mediaPlayer?.seekTo(progress)
            }
        })
        Statified.favImagButton?.setOnClickListener({
            if (Statified.favoriteContent?.checkifExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean){
                Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_off))
                Statified.favoriteContent?.deleteFavourites(Statified.currentSongHelper?.songId?.toInt() as Int)
                Toast.makeText(Statified.myActivity,"Removed from favorites",Toast.LENGTH_SHORT).show()
            } else {
                Statified.favImagButton?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity,R.drawable.favorite_on))
                Statified.favoriteContent?.storeAsFaviorite(Statified.currentSongHelper?.songId?.toInt(),Statified.currentSongHelper?.songArtist,Statified.currentSongHelper?.songTitle,Statified.currentSongHelper?.songPath)
                Toast.makeText(Statified.myActivity,"Added to favorites",Toast.LENGTH_SHORT).show()
            }
        })
        Statified.shuffleImageButton?.setOnClickListener({
            var editorShuffle = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_SHUFFLE,Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_LOOP,Context.MODE_PRIVATE)?.edit()
            if (Statified.currentSongHelper?.isShuffle as Boolean){
                Statified.currentSongHelper?.isShuffle = false
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                editorShuffle?.putBoolean("feature",false)?.apply()

            }else {
                Statified.currentSongHelper?.isShuffle = true
                Statified.currentSongHelper?.isLoop = false
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
                editorShuffle?.putBoolean("feature",true)?.apply()
                editorLoop?.putBoolean("feature",false)?.apply()
            }
        })
        Statified.nextImageButton?.setOnClickListener({
            Statified.currentSongHelper?.isPlaying = true
            if (Statified.currentSongHelper?.isShuffle as Boolean){
                Staticated.playNext("PlayNextLikeNormalShuffle")
            }else{
                Staticated.playNext("PlayNextNormal")
            }
        })
       Statified.previousImageButton?.setOnClickListener({
            Statified.currentSongHelper?.isPlaying = true
            if (Statified.currentSongHelper?.isLoop as Boolean){
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
            }
            Staticated.playPrevious()
        })
        Statified.loopImageButton?.setOnClickListener({
            var editorShuffle = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_SHUFFLE,Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myActivity?.getSharedPreferences(Staticated.MY_PREF_LOOP,Context.MODE_PRIVATE)?.edit()
            if (Statified.currentSongHelper?.isLoop as Boolean){
                Statified.currentSongHelper?.isLoop = false
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
                editorLoop?.putBoolean("feature",false)?.apply()
            } else {
                Statified.currentSongHelper?.isLoop = true
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_icon)
                Statified.currentSongHelper?.isShuffle = false
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                editorLoop?.putBoolean("feature",true)?.apply()
                editorShuffle?.putBoolean("feature",false)?.apply()
            }
        })
        Statified.playPauseImageButton?.setOnClickListener({
            if (Statified.mediaPlayer?.isPlaying as Boolean){
                Statified.mediaPlayer?.pause()
                Statified.currentSongHelper?.isPlaying = false
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }else{
                Statified.mediaPlayer?.start()
                Statified.currentSongHelper?.isPlaying = true
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }
    fun bindShakeListener(){
        Statified.mSensorListner = object : SensorEventListener{
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                mAccelerationLast = mAccelerationCurrent
                mAccelerationCurrent = Math.sqrt(((x*x + y*y + z*z).toDouble())).toFloat()
                val delta = mAccelerationCurrent - mAccelerationLast
                mAcceleration = mAcceleration * 0.9f + delta

                if (mAcceleration > 12){
                    val pref = Statified.myActivity?.getSharedPreferences(SettingsFragment.Statified.MY_PREF_NAME,Context.MODE_PRIVATE)
                    val isAllowed = pref?.getBoolean("feature",false)
                    if (isAllowed as Boolean) Staticated.playNext("PlayNextNormal")
                }
            }
        }
    }

}// Required empty public constructor
