package com.liyihuanx.lib_player.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.controller.IController
import com.liyihuanx.lib_player.status.IVideoStatusListener

/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
abstract class AbsController(private val context: Context) : IController, IVideoStatusListener {

    abstract fun getCoverLayout(): Int?

    abstract fun initView(rootView: View)

    private var rootView: View? = null

    // 视频播放器
    var mPlayer: IAbsPlayerView? = null


    override fun getView(): View? {
        if (getCoverLayout() != null) {
            rootView = LayoutInflater.from(context)
                .inflate(R.layout.layout_default_controller, null, false)
            initView(rootView!!)
        }
        return rootView
    }

    override fun attach(player: IAbsPlayerView) {
        this.mPlayer = player
    }

}