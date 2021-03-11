package com.a6.ensayovlc.vlc

import org.videolan.libvlc.MediaPlayer

interface VlcListener {

    fun onComplete()

    fun onError()

    fun onBuffering(event: MediaPlayer.Event)

}