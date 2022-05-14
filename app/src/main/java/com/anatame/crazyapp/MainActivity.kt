package com.anatame.crazyapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anatame.crazyapp.databinding.ActivityMainBinding
import com.anatame.crazyapp.services.ForegroundService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetMessage.setOnClickListener {
            val message = binding.etSetMessage.text.toString()
            CommonData.message = message
            binding.tvCurrentText.text = message
        }

        checkOverlayPermission()
        startService()

    }

    fun closeEmoteMenu(){
        println("MessageFromActivity: close emote menu")
    }

    // method for starting the service
    fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if (Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this, ForegroundService::class.java))
                } else {
                    startService(Intent(this, ForegroundService::class.java))
                }
            }
        } else {
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    // method to ask user to grant the Overlay permission
    fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(myIntent)
            }
        }
    }

    // check for permission again when user grants it from
    // the device settings, and start the service
    override fun onResume() {
        super.onResume()
        startService()
    }
}
