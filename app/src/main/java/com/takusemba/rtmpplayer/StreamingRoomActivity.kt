package com.takusemba.rtmpplayer

import android.app.PendingIntent.getActivity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.takusemba.rtmpplayer.databinding.ActivityStreamingRoomBinding

class StreamingRoomActivity : AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityStreamingRoomBinding

    private var player: SimpleExoPlayer? = null
    private var streamerName: String? = ""
    private var isBuffering: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_streaming_room)

        streamerName = intent.getStringExtra(STREAMER_NAME)
        if (streamerName  != null) {
            supportActionBar?.title = streamerName
            play()
        } else {
            finish()
        }

        binding.retry.setOnClickListener {
            retry()
        }
    }

    private fun play() {
        player = SimpleExoPlayer.Builder(this).build()
        player?.addListener(this)

        val streamingUri = "https://pull.strattonshire.com/live/${streamerName}"
        val uri = Uri.parse(streamingUri)

        binding.playerView.player = player

        val mediaSource = ProgressiveMediaSource
            .Factory(RtmpDataSourceFactory())
            .createMediaSource(MediaItem.fromUri(uri))

        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.playWhenReady = true
    }

    private fun stop() {
        player?.playWhenReady = false
        player?.release()
    }

    private fun retry() {
        stop()
        play()
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)

        isBuffering = false

        when (state) {
            Player.STATE_IDLE -> binding.progressBar.visibility = View.VISIBLE
            Player.STATE_BUFFERING -> {
                binding.progressBar.visibility = View.VISIBLE
                isBuffering = true
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isBuffering)
                        streamEnded()
                }, 5000)
            }
            Player.STATE_READY -> binding.progressBar.visibility = View.GONE
            Player.STATE_ENDED -> streamEnded()
        }
    }

    private fun streamEnded() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Stream ended")
        alertDialog.setMessage("Streamer is offline/has ended the show")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { _, _ -> finish() }
        alertDialog.show()
    }

}