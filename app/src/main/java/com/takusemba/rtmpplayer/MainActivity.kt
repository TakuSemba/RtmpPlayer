package com.takusemba.rtmpplayer

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

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
        player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this), DefaultTrackSelector(), DefaultLoadControl())
        val playerView = findViewById<SimpleExoPlayerView>(R.id.player_view)
        val uri = Uri.parse(BuildConfig.STREAMING_URL)

        playerView.player = player

        val mediaSource = ExtractorMediaSource(uri, RtmpDataSourceFactory(), DefaultExtractorsFactory(), null, null)

        player?.prepare(mediaSource)
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
