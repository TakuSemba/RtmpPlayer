package com.takusemba.rtmpplayer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.takusemba.rtmpplayer.databinding.ActivityMainBinding

const val STREAMER_NAME = "com.takusemba.rtmpplayer.STREAMER_NAME"

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setOnClickListeners()

        binding.streamerNameEditText.addTextChangedListener(this)
        if (binding.streamerNameEditText.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun setOnClickListeners() {
        binding.joinStreamButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.join_stream_button -> joinStream()
        }
    }
    
    private fun joinStream() {
        val streamerName = binding.streamerNameEditText.text.toString()
        val intent = Intent(this, StreamingRoomActivity::class.java).apply {
            putExtra(STREAMER_NAME, streamerName)
        }
        startActivity(intent)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.joinStreamButton.isEnabled = count != 0
    }

    override fun afterTextChanged(s: Editable?) {
    }

}
