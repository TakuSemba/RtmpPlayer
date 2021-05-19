package com.takusemba.rtmpplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play()

        findViewById<Button>(R.id.retry).setOnClickListener {
            retry()
        }
    }

    private fun play() {
        player = SimpleExoPlayer.Builder(this).build()
        val playerView = findViewById<PlayerView>(R.id.player_view)
        val uri = Uri.parse(BuildConfig.STREAMING_URL)

        playerView.player = player

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
