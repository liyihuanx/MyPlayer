package com.liyihuanx.lib_player.controller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.base.AbsController
import com.liyihuanx.lib_player.status.VideoStatus
import com.liyihuanx.lib_player.utils.PlayerUtil
import java.util.*

/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
class DefaultController(context: Context) : AbsController(context), View.OnClickListener {

    // 已播放时长
    private var tvHasPlayTime: TextView? = null

    // 总时长
    private var tvTotalPlayTime: TextView? = null

    // 加载动画
    private var loading: LinearLayout? = null

    // 播放键
    private var ivCenterStart: ImageView? = null

    // 根布局
    private var rlRoot: RelativeLayout? = null

    override fun getCoverLayout(): Int {
        return R.layout.layout_default_controller
    }

    override fun initView(rootView: View) {
        tvHasPlayTime = rootView.findViewById<TextView>(R.id.tvHasPlayTime)
        tvTotalPlayTime = rootView.findViewById<TextView>(R.id.tvTotalPlayTime)
        loading = rootView.findViewById<LinearLayout>(R.id.loading)

        ivCenterStart = rootView.findViewById<ImageView>(R.id.ivCenterStart)

        rlRoot = rootView.findViewById<RelativeLayout>(R.id.rlRoot)
        rlRoot?.setOnClickListener(this)
    }

    override fun onStatusChange(status: Int) {
        Log.d("QWER", "onStatusChange: $status")
        when (status) {
            VideoStatus.STATE_PRELOADED_WAITING, VideoStatus.STATE_PREPARED -> {
                tvTotalPlayTime?.text = PlayerUtil.formatTime(mPlayer.getDuration())
                loading?.visibility = View.GONE
            }

            VideoStatus.STATE_PREPARING, VideoStatus.STATE_PRELOADING -> {
                loading?.visibility = View.VISIBLE
            }

            VideoStatus.STATE_PLAYING -> {
                loading?.visibility = View.GONE
                ivCenterStart?.visibility = View.GONE
            }

            VideoStatus.STATE_PAUSED -> {
                ivCenterStart?.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0) {
            rlRoot -> {
                switchPlay(mPlayer.getCurrentStatus())
            }
        }
    }

    // 切换播放，暂停
    private fun switchPlay(status: Int?) {
        when(status) {
            // 1.还没开始初始化
            // 当需要手动调用openMedia时,isAutoPlay 就必须是TRUE才行
            VideoStatus.STATE_IDLE -> {
                mPlayer.openMedia()
            }
            // 2.准备好播放了
            VideoStatus.STATE_PRELOADED_WAITING, VideoStatus.STATE_PREPARED -> {
                mPlayer.startPlay()
            }
            // 3.暂停了
            VideoStatus.STATE_PAUSED -> {
                mPlayer.onResume()
            }
            // 4.播放中
            VideoStatus.STATE_PLAYING -> {
                mPlayer.onPause()
            }
        }

    }
}