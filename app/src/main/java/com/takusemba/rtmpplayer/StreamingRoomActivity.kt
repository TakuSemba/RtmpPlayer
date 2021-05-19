package com.takusemba.rtmpplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.takusemba.rtmpplayer.databinding.ActivityStreamingRoomBinding

class StreamingRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStreamingRoomBinding

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_streaming_room)

        play()

        binding.retry.setOnClickListener {
            retry()
        }
    }

    private fun play() {
        player = SimpleExoPlayer.Builder(this).build()

        val streamerName = intent.getStringExtra(STREAMER_NAME)
        val uri = Uri.parse("rtmp://pull.strattonshire.com/live/${streamerName}")

        binding.playerView.player = player

        val mediaSource = ProgressiveMediaSource
            .Factory(
                RtmpDataSourceFactory(),
                DefaultExtractorsFactory()
            )
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

}