# AppMonitor

![Image](app/src/main/ic_launcher-playstore.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/AppMonitor/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/app-monitor)](https://repo1.maven.org/maven2/com/github/jenly1314/app-monitor)
[![JitPack](https://jitpack.io/v/jenly1314/AppMonitor.svg)](https://jitpack.io/#jenly1314/AppMonitor)
[![CI](https://travis-ci.com/jenly1314/AppMonitor.svg?branch=master)](https://travis-ci.com/jenly1314/AppMonitor)
[![CircleCI](https://circleci.com/gh/jenly1314/AppMonitor.svg?style=svg)](https://circleci.com/gh/jenly1314/AppMonitor)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)
[![Blog](https://img.shields.io/badge/blog-Jenly-9933CC.svg)](https://jenly1314.github.io/)
[![QQGroup](https://img.shields.io/badge/QQGroup-20867961-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=8fcc6a2f88552ea44b1411582c94fd124f7bb3ec227e2a400dbbfaad3dc2f5ad)

AppMonitor 可以轻松的监听App的前后台状态变化；Activity的活跃状态变化；设备的开关屏状态变化。

## 主要功能介绍
- [x] 支持监听App的状态变化（前后台切换）
- [x] 支持监听Activity的状态变化
- [x] 支持监听设备屏幕的状态变化（开关屏、解锁）


## 引入

### Gradle:

1. 在Project的 **build.gradle** 里面添加远程仓库  
          
```gradle
allprojects {
    repositories {
        //...
        mavenCentral()
    }
}
```

2. 在Module的 **build.gradle** 里面添加引入依赖项
```gradle
//AndroidX 版本
implementation 'com.github.jenly1314:app-monitor:1.0.0'

```

## 示例

```kotlin
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
```

更多使用详情，请查看[Demo](app)中的源码使用示例或直接查看[API帮助文档](https://jitpack.io/com/github/jenly1314/AppMonitor/latest/javadoc/)


## 版本记录

#### v1.0.0：2021-8-31
*  AppMonitor初始版本

## 赞赏
如果你喜欢AppMonitor，或感觉AppMonitor帮助到了你，可以点右上角“Star”支持一下，你的支持就是我的动力，谢谢 :smiley:<p>
你也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:
    <div>
        <img src="https://jenly1314.github.io/image/pay/wxpay.png" width="280" heght="350">
        <img src="https://jenly1314.github.io/image/pay/alipay.png" width="280" heght="350">
        <img src="https://jenly1314.github.io/image/pay/qqpay.png" width="280" heght="350">
        <img src="https://jenly1314.github.io/image/alipay_red_envelopes.jpg" width="233" heght="350">
    </div>

## 关于我
   Name: <a title="关于作者" href="https://about.me/jenly1314" target="_blank">Jenly</a>

   Email: <a title="欢迎邮件与我交流" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314#gmail.com</a> / <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314#vip.qq.com</a>

   CSDN: <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>

   CNBlogs: <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>

   GitHub: <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a>
   
   Gitee: <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>

   加入QQ群: <a title="点击加入QQ群" href="http://shang.qq.com/wpa/qunwpa?idkey=8fcc6a2f88552ea44b1411582c94fd124f7bb3ec227e2a400dbbfaad3dc2f5ad" target="_blank">20867961</a>
   <div>
       <img src="https://jenly1314.github.io/image/jenly666.png">
       <img src="https://jenly1314.github.io/image/qqgourp.png">
   </div>


   
