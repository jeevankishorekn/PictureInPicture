package com.example.pictureinpicture

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pictureinpicture.databinding.ActivityPipBinding

class PIPActivity: AppCompatActivity() {
    private lateinit var pipBinding: ActivityPipBinding

    private val TAG = "PIPActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pipBinding = ActivityPipBinding.inflate(layoutInflater)
        setContentView(pipBinding.root)

        playVideo(intent)
    }

    private fun playVideo(intent: Intent?) {
        val url = intent?.getStringExtra("videoUrl")
        val controller = MediaController(this)
        controller.setAnchorView(pipBinding.videoView)
        Log.d(TAG, "onCreate: $url")
        pipBinding.videoView.apply {
            setMediaController(controller)
            setVideoURI(Uri.parse(url))
            setOnPreparedListener {
                it.start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun enterPIPMode(){

        val ratio = Rational(pipBinding.videoView.width, pipBinding.videoView.height)
        val params = PictureInPictureParams.Builder()
            .setAutoEnterEnabled(true)
            .setAspectRatio(ratio)
            .build()
        enterPictureInPictureMode(params)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        playVideo(intent)
    }

}