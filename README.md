# DexOptFix
This library was designed for fixing alarm timeout error when sub process executing dexopt. This may happen when you loading class from special apk/jar file using DexFile.loadDex() or new DexClassLoader() or MultiDex.install(). 

# Resolved issue  
In some devices with a system version lower than 5.0 may get a [virus](http://blogs.360.cn/post/analysis_of_fakedebuggerd_d.html). That will register a 2 seconds alarm clock when you fork new process. Then dexopt will fail with code 0x000e if the opt time consumed lager than 2s (It may also be 1s, depends on the version of virus).   
```
D/dalvikvm( 12513): DexOpt: --- BEGIN 'apk.classes2.zip' (bootstrap=0) ---
W/dalvikvm( 12513): DexOpt: --- END 'apk.classes2.zip' --- status=0x000e, process failed
E/dalvikvm( 12513): Unable to extract+optimize DEX from '/data/.../apk.classes2.zip'
```
After dexopt failure, you app will crash with dramatic exception: ``` java.lang.ClassNotFoundException``` or ```java.lang.NoClassDefFoundError```. You can get more from [this link](https://github.com/Tencent/tinker/issues/925#issuecomment-483927335).  

<br></br>  
In this library, we register process fork handler, clear the alarm clock and reset signal handler in the ```childHandler```.
```
pthread_atfork(prepareHandler, parentHandler, childHandler)
```   
Normal device`s log
> W/DexOptFix: pid 2363 before hook ,   old sigalrm handler: 0, new handler: -1642465520, clear alarm 0  
  W/DexOptFix: pid 2363 prepareHandler, old sigalrm handler: -1642465520, new handler: -1642465520  
  W/DexOptFix: pid 2363 parentHandler,  old sigalrm handler: -1642465520, new handler: -1642465520  
  W/DexOptFix: pid 2377 childHandler,   **old sigalrm handler: -1642465520**, new handler: -1642465520, **clear alarm 0s**

Error device`s log
> W/DexOptFix: pid 13581 before hook ,   old sigalrm handler: 0, new handler: 1955399981, clear alarm 0  
  W/DexOptFix: pid 13581 prepareHandler, old sigalrm handler: 1955399981, new handler: 1955399981  
  W/DexOptFix: pid 13581 parentHandler,  old sigalrm handler: 1955399981, new handler: 1955399981  
  W/DexOptFix: pid 14145 childHandler,   **old sigalrm handler: 1075695361**, new handler: 1955399981, **clear alarm 2s**    

We can find that, new sub process in error device had been replaced with a different signal handler and a 2s alarm clock by the virus.

# How to use
Add library dependency to dependencies{} block in build.gradle file  
```
dependencies{
   compile 'com.cantalou:dexoptfix:1.0.1'
}
```  

Add this code in the method ```attachBaseContext``` of Application before calling dex operation(e.g., MultiDex.install).
```
override fun attachBaseContext(base: Context?) {
   super.attachBaseContext(base)
   DexOptFix.fix(this)
   MultiDex.install(this)
}
```  

# Test cover
Android version : from 4.0 - 4.4

# Thanks
[douniwan5788](https://github.com/douniwan5788)