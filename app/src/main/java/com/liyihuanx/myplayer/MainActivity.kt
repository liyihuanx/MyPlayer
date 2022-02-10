package com.liyihuanx.myplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.children
import com.liyihuanx.lib_player.controller.DefaultController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ijkPlay.controller = DefaultController(this)
        ijkPlay.setPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",false)

        btnPlay.setOnClickListener {
            ijkPlay.startPlay()
//            ijkPlay.playVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
//            ijkPlay.playVideoPath("https://media.w3.org/2010/05/sintel/trailer.mp4")
//            ijkPlay.playVideoPath("http://v-cdn.zjol.com.cn/280443.mp4")
        }
    }
}