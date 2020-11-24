package com.a6.ensayovlc.vlc;

import org.videolan.libvlc.MediaPlayer;

public interface VlcListener {

  void onComplete();

  void onError();

  void onBuffering(MediaPlayer.Event event);//event.getBuffering(),Hide if 100, else Show
}
