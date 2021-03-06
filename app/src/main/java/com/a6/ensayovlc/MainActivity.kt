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
import kotlin.collections.ArrayList

//const val URL_RTSP = "rtsp://mirgor.k8s.cablevision-labs.com.ar:8554/"
//const val URL_RTSP = "rtsp://200.32.54.152:50105/cam"
const val URL_RTSP = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"

class MainActivity : AppCompatActivity(), VlcListener, View.OnClickListener {

    private var vlcVideoLibrary: VlcVideoLibrary? = null
    private var bStartStop: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView
        bStartStop = findViewById<View>(R.id.b_start_stop) as Button
        bStartStop!!.setOnClickListener(this)
        vlcVideoLibrary = VlcVideoLibrary(this, this, surfaceView)
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
