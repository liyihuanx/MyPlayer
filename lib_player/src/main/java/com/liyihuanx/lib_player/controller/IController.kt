package com.liyihuanx.lib_player.controller

import android.view.View
import com.liyihuanx.lib_player.base.IAbsPlayerView

/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
interface IController {

    fun getView(): View?

    fun attach(player: IAbsPlayerView)

    fun detach(){}

}