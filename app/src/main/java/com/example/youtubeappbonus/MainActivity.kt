package com.example.youtubeappbonus

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AlertDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity : AppCompatActivity() {
    val videos: Array<Array<String>> = arrayOf(
        arrayOf("Numbers Game", "CiFyTc1SwPw"),
        arrayOf("Calculator", "ZbZFMDk69IA"),
        arrayOf("Guess the Phrase", "DU1qMhyKv8g"),
        arrayOf("Username and Password", "G_XYXuC8b9M"),
        arrayOf("GUI Username and Password", "sqJWyPhZkDw"),
        arrayOf("Country Capitals", "yBkRLhoVTmc"),
        arrayOf("Database Module", "E-Kb6FgMbVw"))
    lateinit var youTubePlayer: YouTubePlayerView
    lateinit var player: YouTubePlayer
    var current_Video = 0
    var timeStamp = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternet()

        youTubePlayer = findViewById(R.id.ytPlayer)
        youTubePlayer.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer){
                super.onReady(youTubePlayer)
                player = youTubePlayer
                player.loadVideo(videos[current_Video][1], timeStamp)
                initializeRV()
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayer.enterFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayer.exitFullScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("currentVideo", current_Video)
        outState.putFloat("timeStamp", timeStamp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        current_Video = savedInstanceState.getInt("currentVideo", 0)
        timeStamp = savedInstanceState.getFloat("timeStamp", 0f)
    }

    private fun initializeRV(){

        val gridView: GridView = findViewById(R.id.rvVideos)
        gridView.adapter = VideoGridAdapter(videos, player,this@MainActivity)

    }

    private fun checkInternet(){
        if(!connectedToInternet()){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Internet Connection Not Found")
                .setPositiveButton("RETRY"){_, _ -> checkInternet()}
                .show()
        }
    }

    private fun connectedToInternet(): Boolean{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if(activeNetwork?.isConnectedOrConnecting == true){
            //do something with the network
        }
        else
        {
            println("Not Connected To The Internet")
        }
        return activeNetwork?.isConnectedOrConnecting == true
    }
}