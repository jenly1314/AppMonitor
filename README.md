# AppMonitor

![Image](app/src/main/ic_launcher-playstore.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/AppMonitor/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/app-monitor)](https://repo1.maven.org/maven2/com/github/jenly1314/app-monitor)
[![JitPack](https://jitpack.io/v/jenly1314/AppMonitor.svg)](https://jitpack.io/#jenly1314/AppMonitor)
[![CI](https://travis-ci.com/jenly1314/AppMonitor.svg?branch=master)](https://travis-ci.com/jenly1314/AppMonitor)
[![CircleCI](https://circleci.com/gh/jenly1314/AppMonitor.svg?style=svg)](https://circleci.com/gh/jenly1314/AppMonitor)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)

AppMonitor 可以轻松的监听App的前后台状态变化；Activity的活跃状态变化；设备的开关屏状态变化。

## 功能介绍
- [x] 支持监听App的状态变化（前后台切换）
- [x] 支持监听Activity的状态变化
- [x] 支持监听设备屏幕的状态变化（开关屏、解锁）


## 引入

### Gradle:
1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle
    implementation 'com.github.jenly1314:app-monitor:1.0.0'
    ```

## 使用

### 示例
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

更多使用详情，请查看[app](app)中的源码使用示例或直接查看[API帮助文档](https://jitpack.io/com/github/jenly1314/AppMonitor/latest/javadoc/)


## 版本记录

#### v1.0.0：2021-8-31
*  AppMonitor初始版本

## 赞赏
如果你喜欢AppMonitor，或感觉AppMonitor帮助到了你，可以点右上角“Star”支持一下，你的支持就是我的动力，谢谢 :smiley:
<p>您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:

<div>
   <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## 关于我

| 我的博客                                                                                | GitHub                                                                                  | Gitee                                                                                  | CSDN                                                                                 | 博客园                                                                            |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| <a title="我的博客" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>  | <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>  | <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>  |

## 联系我

| 微信公众号        | Gmail邮箱                                                                          | QQ邮箱                                                                              | QQ群                                                                                                                       | QQ群                                                                                                                       |
|:-------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="给我发邮件" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314</a> | <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
   <img src="https://jenly1314.github.io/image/page/footer.png">
</div>
   
