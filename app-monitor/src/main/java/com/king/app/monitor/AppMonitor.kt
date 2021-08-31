package com.king.app.monitor

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.king.app.monitor.receiver.ScreenActionReceiver
import java.util.concurrent.CopyOnWriteArrayList

/**
 * App监听器：主要用于监听App的一些状态变化
 * 如：监听App的状态变化（前后台切换）
 *    监听Activity的状态变化
 *    监听设备屏幕的状态变化（开关屏、解锁）
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
object AppMonitor {

    /**
     * 是否注册屏幕状态事件广播接收器
     */
    private var isRegisterScreenAction = false

    /**
     * 是否注册Activity生命周期回调
     */
    private var isRegisterActivityLifecycle = false

    /**
     * 活跃的 Activity 数量
     */
    private var mActiveActivityCount = 0

    /**
     * 存活的 Activity 数量
     */
    private var mAliveActivityCount = 0

    /**
     * OnAppStatusCallback 集合
     */
    private val mOnAppStatusCallbacks: MutableList<OnAppStatusCallback> by lazy {
        CopyOnWriteArrayList<OnAppStatusCallback>()
    }

    /**
     * OnActivityStatusCallback 集合
     */
    private val mOnActivityStatusCallbacks: MutableList<OnActivityStatusCallback> by lazy {
        CopyOnWriteArrayList<OnActivityStatusCallback>()
    }

    /**
     * OnScreenStatusCallback 集合
     */
    internal val mOnScreenStatusCallbacks: MutableList<OnScreenStatusCallback> by lazy {
        CopyOnWriteArrayList<OnScreenStatusCallback>()
    }

    /**
     * 屏幕状态事件广播接收器
     */
    private val mScreenActionReceiver by lazy {
        ScreenActionReceiver()
    }

    /**
     * 屏幕状态事件广播过滤器
     */
    private val mScreenActionFilter by lazy {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter
    }

    /**
     * Activity 生命周期回调接口
     */
    private val mActivityLifecycleCallbacks by lazy {

        object : Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                //Activity 被创建，存活的 Activity 数量增加
                mAliveActivityCount++

                //通知监听回调接口 Activity 的存活状态或数量发生变化
                mOnActivityStatusCallbacks.forEach {
                    it.onAliveStatusChanged(activity, true, mAliveActivityCount)
                }
            }

            override fun onActivityStarted(activity: Activity) {
                //如果之前活跃的 Activity 数量为0，则表示此时 App 是切换到了前台
                if(mActiveActivityCount == 0){
                    mOnAppStatusCallbacks.forEach {
                        it.onAppForeground(activity)
                    }
                }

                //Activity 触发了生命周期的onStop方法，活跃的 Activity 数量增加
                mActiveActivityCount++

                //通知监听回调接口 Activity 的活跃状态或数量发生变化
                mOnActivityStatusCallbacks.forEach {
                    it.onActiveStatusChanged(activity, true, mActiveActivityCount)
                }

            }

            override fun onActivityResumed(activity: Activity) {
              
            }

            override fun onActivityPaused(activity: Activity) {
                
            }

            override fun onActivityStopped(activity: Activity) {
                //Activity 触发了生命周期的onStop方法，活跃的 Activity 数量减少
                mActiveActivityCount--

                //通知监听回调接口 Activity 的活跃状态或数量发生变化
                mOnActivityStatusCallbacks.forEach {
                    it.onActiveStatusChanged(activity, false, mActiveActivityCount)
                }

                //如果当前活跃的 Activity 数量为0，则表示此时 App 是切换到了后台
                if(mActiveActivityCount == 0){
                    mOnAppStatusCallbacks.forEach {
                        it.onAppBackground(activity)
                    }
                }


            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                
            }

            override fun onActivityDestroyed(activity: Activity) {
                //Activity 被销毁，存活的 Activity 数量减少
                mAliveActivityCount--

                //通知监听回调接口 Activity 的存活状态或数量发生变化
                mOnActivityStatusCallbacks.forEach {
                    it.onAliveStatusChanged(activity, false, mAliveActivityCount)
                }
            }

        }
    }

    /**
     * 初始化：在 [Application] 的 [Application.onCreate] 方法中调用 [initialize] 进行初始化
     * @param app [Application]
     * @param registerScreenAction 是否需要注册屏幕的状态事件接收器，如果需要监听屏幕状态（开屏、关屏、解锁）就传 true，不许要就传 false
     */
    fun initialize(app: Application, registerScreenAction: Boolean = false){

        //注册 [ActivityLifecycleCallbacks]
        if(isRegisterActivityLifecycle){
            app.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        }
        app.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        isRegisterActivityLifecycle = true

        //注册 [ScreenActionReceiver]
        if(isRegisterScreenAction){
            app.unregisterReceiver(mScreenActionReceiver)
        }
        if(registerScreenAction){
            app.registerReceiver(mScreenActionReceiver, mScreenActionFilter)
        }
        isRegisterScreenAction = registerScreenAction
    }

    /**
     * 注册监听 App 状态变化（前台，后台）
     * @param callback
     */
    fun registerAppStatusCallback(callback: OnAppStatusCallback?){
        callback?.let {
            if(!mOnAppStatusCallbacks.contains(it)){
                mOnAppStatusCallbacks.add(it)
            }
        }
    }

    /**
     * 注销监听 App 状态变化（前台，后台）
     */
    fun unregisterAppStatusCallback(callback: OnAppStatusCallback?){
        callback?.let {
            mOnAppStatusCallbacks.remove(it)
        }
    }

    /**
     * 注销所有 App 状态变化监听回调
     */
    fun unregisterAllAppStatusCallbacks(){
        mOnAppStatusCallbacks.clear()
    }

    /**
     * 注册监听 Activity 状态变化
     */
    fun registerActivityStatusCallback(callback: OnActivityStatusCallback?){
        callback?.let {
            if(!mOnActivityStatusCallbacks.contains(it)){
                mOnActivityStatusCallbacks.add(it)
            }
        }
    }

    /**
     * 注销监听 Activity 状态变化
     */
    fun unregisterActivityStatusCallback(callback: OnActivityStatusCallback?){
        callback?.let {
            mOnActivityStatusCallbacks.remove(it)
        }
    }

    /**
     * 注销所有 Activity 状态变化监听回调
     */
    fun unregisterAllActivityStatusCallbacks(){
        mOnActivityStatusCallbacks.clear()
    }

    /**
     * 注册监听屏幕状态变化（开屏、关屏、解锁）
     */
    fun registerScreenStatusCallback(callback: OnScreenStatusCallback?){
        callback?.let {
            if(!mOnScreenStatusCallbacks.contains(it)){
                mOnScreenStatusCallbacks.add(it)
            }
        }
    }


    /**
     * 注销监听屏幕状态变化（开屏、关屏、解锁）
     */
    fun unregisterScreenStatusCallback(callback: OnScreenStatusCallback?){
        callback?.let {
            mOnScreenStatusCallbacks.remove(it)
        }
    }

    /**
     * 注销所有屏幕状态变化监听回调
     */
    fun unregisterAllScreenStatusCallbacks(){
        mOnScreenStatusCallbacks.clear()
    }

    /**
     * 获取活跃的 Activity 数量
     */
    fun getActiveActivityCount() = mActiveActivityCount

    /**
     * 获取存活的 Activity 数量
     */
    fun getAliveActivityCount() = mAliveActivityCount

    /**
     * 判断 App 是否在前台
     */
    fun isAppForeground() = mActiveActivityCount > 0

    /**
     * 判断 App 是否在后台
     */
    fun isAppBackground() = mActiveActivityCount <= 0

    /**
     * App 监听回调接口：主要用来监听 App 的前后台切换状态变化
     */
    interface OnAppStatusCallback {

        /**
         * 当 App 切换到前台时回调此方法
         * @param activity 当前 Activity
         */
        fun onAppForeground(activity: Activity)

        /**
         * 当 App 切换到后台时回调此方法
         * @param activity 当前 Activity
         */
        fun onAppBackground(activity: Activity)


    }

    /**
     * Activity 监听回调接口：主要用来监听 Activity 活跃与存活状态的数量变化
     */
    interface OnActivityStatusCallback {
        /**
         * 当 Activity 存活的数量发生变化时回调此方法
         * @param activity 当前 Activity
         * @param isAliveState 是否是存活状态
         * @param aliveActivityCount 存活的 Activity 数量
         */
        fun onAliveStatusChanged(activity: Activity, isAliveState: Boolean, aliveActivityCount: Int)

        /**
         * 当 Activity 活跃的数量发生变化时回调此方法
         * @param activity 当前 Activity
         * @param isActiveState 是否是活跃状态
         * @param activeActivityCount 活跃的 Activity 数量
         */
        fun onActiveStatusChanged(activity: Activity, isActiveState: Boolean, activeActivityCount: Int)
    }


    /**
     * 屏幕状态监听回调接口：主要用来监听屏幕的状态（开屏或关屏）变化
     */
    interface OnScreenStatusCallback {

        /**
         * 当屏幕状态发生变化（开屏或关屏）时回调此方法
         * @param isScreenOn true 表示开屏，false 表示关屏
         */
        fun onScreenStatusChanged(isScreenOn: Boolean)

        /**
         * 解锁：当设备唤醒后，用户在（解锁键盘消失）时回调此方法
         */
        fun onUserPresent()
    }


}