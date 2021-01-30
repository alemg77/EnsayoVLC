package com.a6.ensayovlc

import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.a6.ensayovlc.vlc.VlcListener
import com.a6.ensayovlc.vlc.VlcVideoLibrary
import org.videolan.libvlc.MediaPlayer
import java.util.*

const val URL_RTSP = "rtsp://mirgor.k8s.cablevision-labs.com.ar:8554/"

class MainActivity : AppCompatActivity(), VlcListener, View.OnClickListener {

    private var vlcVideoLibrary: VlcVideoLibrary? = null
    private var bStartStop: Button? = null
    private val options = arrayOf(":fullscreen")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView
        bStartStop = findViewById<View>(R.id.b_start_stop) as Button
        bStartStop!!.setOnClickListener(this)
        vlcVideoLibrary = VlcVideoLibrary(this, this, surfaceView)
        vlcVideoLibrary!!.setOptions(listOf(*options))
    }

    override fun onComplete() {
        Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show()
    }

    override fun onError() {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show()
        vlcVideoLibrary!!.stop()
        bStartStop!!.text = "start_player"
    }

    override fun onBuffering(event: MediaPlayer.Event) {}

    override fun onClick(view: View) {
        if (!vlcVideoLibrary!!.isPlaying) {
            vlcVideoLibrary!!.play(URL_RTSP)
            bStartStop!!.visibility = View.GONE
        } else {
            vlcVideoLibrary!!.stop()
            bStartStop!!.text = "start_player"
        }
    }
}
