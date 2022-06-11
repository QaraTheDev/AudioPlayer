package com.example.audioplayer


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var songPosition: Int = 0
    lateinit var runnable: Runnable
    private var handler = Handler()
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var musicPlayer = MediaPlayer.create(this, R.raw.music1)


        totalTime = musicPlayer.duration

        seekBar.progress = 0

        seekBar.max = musicPlayer.duration


        val volumeControl = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        volumeBar.max = volumeControl.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volumeBar.progress = volumeControl.getStreamVolume(AudioManager.STREAM_MUSIC)

        next_btn.setOnClickListener {
            songPosition++
        }

        back_btn.setOnClickListener {
            musicPlayer.stop()
            musicPlayer.prepare()
            musicPlayer.start()
        }

        repeat.setOnClickListener {
            musicPlayer.setLooping(true)
        }
        shuffle.setOnClickListener {

        }

        play_btn.setOnClickListener {
            if(!musicPlayer.isPlaying){
                musicPlayer.start()

                play_btn.setImageResource(R.drawable.ic_baseline_pause_24)

            }else{
                musicPlayer.pause()

                play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            }
        }


        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed){
                    musicPlayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })


        volumeBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                volumeControl.setStreamVolume(AudioManager.STREAM_MUSIC, pos, 0)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        runnable = Runnable {

            seekBar.progress = musicPlayer.currentPosition

            handler.postDelayed(runnable, 1000)

            timeStart.text = TimeLabel(musicPlayer.currentPosition)

            timeEnd.text = TimeLabel(totalTime)

        }

        handler.postDelayed(runnable, 1000)

        musicPlayer.setOnCompletionListener{
            play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekBar.progress = 0
        }
    }

    fun TimeLabel(time: Int): String{
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

}

