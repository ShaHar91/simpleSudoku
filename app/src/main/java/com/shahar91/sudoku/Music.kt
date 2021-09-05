package com.shahar91.sudoku

import android.content.Context
import android.media.MediaPlayer

object Music {
    private var mp: MediaPlayer? = null

    /** stop old song and start new one  */
    fun play(context: Context?, resource: Int) {
        // stop(context);
        if (Prefs.getMusic(context)) {
            if (resource == R.raw.main) {
                if (mp != null) {
                    mp?.start()
                } else {
                    mp = createPlayer(context, resource)
                }
            } else {
                stop()
                mp = createPlayer(context, resource)
            }
        }
    }

    private fun createPlayer(context: Context?, resource: Int): MediaPlayer {
        return MediaPlayer.create(context, resource).apply {
            isLooping = true
            try {
                prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            start()
        }
    }

    /** stop the music  */
    fun pause() {
        mp?.pause()
    }

    fun start() {
        mp?.start()
    }

    /** stop the music  */
    fun stop() {
        mp?.let {
            it.stop()
            it.release()
        }
        mp = null
    }
}
