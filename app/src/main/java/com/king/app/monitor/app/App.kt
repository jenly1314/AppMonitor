package com.king.app.monitor.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.king.app.monitor.AppMonitor

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class App : Application() {

    companion object{
        const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        initAppMonitor()
    }

    private fun initAppMonitor(){
        //初始化
        AppMonitor.initialize(this,true)
        //注册监听 App 状态变化（前台，后台）
        AppMonitor.registerAppStatusCallback(object: AppMonitor.OnAppStatusCallback{
            override fun onAppForeground(activity: Activity) {
                //App 切换到前台
                Log.d(TAG,"onAppForeground(Activity = $activity)")
            }

            override fun onAppBackground(activity: Activity) {
                //App 切换到后台
                Log.d(TAG,"onAppBackground(Activity = $activity)")
            }

        })
        //注册监听 Activity 状态变化
        AppMonitor.registerActivityStatusCallback(object: AppMonitor.OnActivityStatusCallback{
            override fun onAliveStatusChanged(
                activity: Activity,
                isAliveState: Boolean,
                aliveActivityCount: Int
            ) {
                //Activity 的存活状态或数量发生变化
                Log.d(TAG,"onAliveStatusChanged(Activity = $activity, isAliveState = $isAliveState, aliveActivityCount = $aliveActivityCount)")
            }

            override fun onActiveStatusChanged(
                activity: Activity,
                isActiveState: Boolean,
                activeActivityCount: Int
            ) {
                //Activity 的活跃状态或数量发生变化
                Log.d(TAG,"onActiveStatusChanged(Activity = $activity, isActiveState = $isActiveState, activeActivityCount = $activeActivityCount)")
            }

        })

        //注册监听屏幕状态变化（开屏、关屏、解锁）
        AppMonitor.registerScreenStatusCallback(object : AppMonitor.OnScreenStatusCallback{
            override fun onScreenStatusChanged(isScreenOn: Boolean) {
                //屏幕状态发生变化（开屏或关屏）
                Log.d(TAG,"onScreenStatusChanged(isScreenOn = $isScreenOn)")
            }

            override fun onUserPresent() {
                //解锁：当设备唤醒后，用户在（解锁键盘消失）时回调
                Log.d(TAG,"onUserPresent()")
            }

        })
    }
}