package com.king.app.monitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.king.app.monitor.AppMonitor

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
internal class ScreenActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            Intent.ACTION_SCREEN_ON -> {//开屏
                AppMonitor.mOnScreenStatusCallbacks.forEach {
                    it.onScreenStatusChanged(true)
                }
            }
            Intent.ACTION_SCREEN_OFF -> {//关屏
                AppMonitor.mOnScreenStatusCallbacks.forEach {
                    it.onScreenStatusChanged(false)
                }
            }
            Intent.ACTION_USER_PRESENT -> {//解锁（解锁键盘消失）
                AppMonitor.mOnScreenStatusCallbacks.forEach {
                    it.onUserPresent()
                }
            }
        }
    }
}