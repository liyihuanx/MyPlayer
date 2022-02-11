package com.liyihuanx.lib_player.controller

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.base.AbsController
import com.liyihuanx.lib_player.status.VideoStatus
import com.liyihuanx.lib_player.utils.PlayerUtil
import java.util.*
import android.os.Looper


/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
class DefaultController(context: Context) : AbsController(context), View.OnClickListener {

    // 已播放时长
    private lateinit var tvHasPlayTime: TextView

    // 总时长
    private lateinit var tvTotalPlayTime: TextView

    // 加载动画
    private lateinit var loading: LinearLayout

    // 播放键
    private lateinit var ivCenterStart: ImageView

    // 根布局
    private lateinit var rlRoot: RelativeLayout

    // 进度条
    private lateinit var seekBar: SeekBar


    private val mainThread = Handler(Looper.getMainLooper())
    private var mUpdateProgressTimer: Timer? = null
    private var mUpdateProgressTimerTask: TimerTask? = null


    override fun getCoverLayout(): Int {
        return R.layout.layout_default_controller
    }

    override fun initView(rootView: View) {
        tvHasPlayTime = rootView.findViewById<TextView>(R.id.tvHasPlayTime)
        tvTotalPlayTime = rootView.findViewById<TextView>(R.id.tvTotalPlayTime)
        loading = rootView.findViewById<LinearLayout>(R.id.loading)

        ivCenterStart = rootView.findViewById<ImageView>(R.id.ivCenterStart)

        seekBar = rootView.findViewById<SeekBar>(R.id.seekBar)

        rlRoot = rootView.findViewById<RelativeLayout>(R.id.rlRoot)
        rlRoot.setOnClickListener(this)
    }

    override fun onStatusChange(status: Int) {
        Log.d("QWER", "onStatusChange: $status")
        when (status) {
            VideoStatus.STATE_PRELOADED_WAITING, VideoStatus.STATE_PREPARED -> {
                tvTotalPlayTime.text = PlayerUtil.formatTime(mPlayer.getDuration())
                loading.visibility = View.GONE
            }

            VideoStatus.STATE_PREPARING, VideoStatus.STATE_PRELOADING -> {
                loading.visibility = View.VISIBLE
            }

            VideoStatus.STATE_PLAYING -> {
                loading.visibility = View.GONE
                ivCenterStart.visibility = View.GONE
                startUpdate()
            }

            VideoStatus.STATE_PAUSED -> {
                loading.visibility = View.GONE
                ivCenterStart.visibility = View.VISIBLE
                cancelUpdate()
            }

            VideoStatus.STATE_BUFFERING_PLAYING -> {
                loading.visibility = View.VISIBLE
            }

            VideoStatus.STATE_BUFFERING_PAUSED -> {
                loading.visibility = View.VISIBLE
                cancelUpdate()
            }

            VideoStatus.STATE_COMPLETED -> {
                cancelUpdate()
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


    private fun startUpdate() {
        cancelUpdate()

        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = Timer()
        }

        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = object : TimerTask() {
                override fun run() {
                    mainThread.post {
                        updatePlayingInfo()
                    }
                }
            }
        }
        mUpdateProgressTimer?.schedule(mUpdateProgressTimerTask, 0, 1000)
    }

    private fun cancelUpdate() {
        mUpdateProgressTimer?.cancel()
        mUpdateProgressTimer = null
        mUpdateProgressTimerTask?.cancel()
        mUpdateProgressTimerTask = null
    }

    /**
     * 视频播放开始，需要做一些操作
     */
    private fun updatePlayingInfo() {
        val position = mPlayer.getCurrentDuration()
        val duration =  mPlayer.getDuration()
        tvHasPlayTime.text = PlayerUtil.formatTime(mPlayer.getCurrentDuration())

        seekBar.secondaryProgress = mPlayer.getCurrentBufferPercentage()

        val progress = (100f * position / duration).toInt()
        seekBar.progress = progress

    }

    // 切换播放，暂停
    private fun switchPlay(status: Int?) {
        when (status) {
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