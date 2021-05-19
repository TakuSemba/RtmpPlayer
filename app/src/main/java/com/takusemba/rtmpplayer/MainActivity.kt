package com.takusemba.rtmpplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.takusemba.rtmpplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        play()

        binding.retry.setOnClickListener {
            retry()
        }
    }

    private fun play() {
        player = SimpleExoPlayer.Builder(this).build()
        val uri = Uri.parse(BuildConfig.STREAMING_URL)

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
