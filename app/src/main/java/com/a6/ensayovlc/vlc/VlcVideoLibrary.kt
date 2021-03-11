package com.a6.ensayovlc.vlc

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.util.*

/**
 * Play and stop need be in other thread or app can freeze
 */
class VlcVideoLibrary(
    context: Context?,
    private val vlcListener: VlcListener, //The library will select one of this class for rendering depend of constructor called
    private val surfaceView: SurfaceView?
) : MediaPlayer.EventListener {

    private var width = 0
    private var height = 0
    private val vlcInstance: LibVLC
    private var player: MediaPlayer? = null
    private val textureView: TextureView? = null
    private val surfaceTexture: SurfaceTexture? = null
    private val surface: Surface? = null
    private val surfaceHolder: SurfaceHolder? = null
    private var options: ArrayList<String>? = ArrayList()

    /**
     * This method should be called after constructor and before play methods.
     * @param options seeted to VLC player.
     */
    fun setOptions(options: ArrayList<String>?) {
        this.options = options
    }

    val isPlaying: Boolean
        get() = player != null && player!!.isPlaying

    fun play(endPoint: String?) {
        if (player == null || player!!.isReleased) {
            setMedia(Media(vlcInstance, Uri.parse(endPoint)))
        } else if (!player!!.isPlaying) {
            player!!.play()
        }
    }

    fun stop() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()
            player!!.release()
        }
    }

    fun pause() {
        if (player != null && player!!.isPlaying) {
            player!!.pause()
        }
    }

    private fun setMedia(media: Media) {
        if (options != null) {
            for (s in options!!) {
                media.addOption(s)
            }
        }
        media.setHWDecoderEnabled(true, false)
        player = MediaPlayer(vlcInstance)
        player!!.media = media
        player!!.setEventListener(this)
        val vlcOut = player!!.vlcVout


        if (surfaceView != null) {
            vlcOut.setVideoView(surfaceView)
            width = surfaceView.width
            height = surfaceView.height
        } else if (textureView != null) {
            vlcOut.setVideoView(textureView)
            width = textureView.width
            height = textureView.height
        } else if (surfaceTexture != null) {
            vlcOut.setVideoSurface(surfaceTexture)
        } else if (surface != null) {
            vlcOut.setVideoSurface(surface, surfaceHolder)
        } else {
            throw RuntimeException("You cant set a null render object")
        }
        if (width != 0 && height != 0) vlcOut.setWindowSize(width, height)
        vlcOut.attachViews()
        player!!.setVideoTrackEnabled(true)
        player!!.play()
    }

    override fun onEvent(event: MediaPlayer.Event) {
        when (event.type) {
            MediaPlayer.Event.Playing -> vlcListener.onComplete()
            MediaPlayer.Event.EncounteredError -> vlcListener.onError()
            MediaPlayer.Event.Buffering -> vlcListener.onBuffering(event)
            else -> {
            }
        }
    }

    init {
        vlcInstance = LibVLC(context, options)
        options!!.add(":fullscreen")
        options!!.add(":rtsp-tcp")
    }
}