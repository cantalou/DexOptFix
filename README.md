# DexOptFix
This library was designed for fixing alarm timeout error when sub process executing dexopt. This may happen when you loading class from special apk/jar file using DexFile.loadDex() or new DexClassLoader() or MultiDex.install().  

# Resolved issue  
In some devices with a system version lower than 5.0 may get a [virus](http://blogs.360.cn/post/analysis_of_fakedebuggerd_d.html). That will register a 2 seconds alarm clock when you fork new process. Then dexopt will fail with code 0x000e if the opt time consumed lager than 2s (It may also be 1s, depends on the version of virus).   
```
D/dalvikvm( 12513): DexOpt: --- BEGIN 'apk.classes2.zip' (bootstrap=0) ---
W/dalvikvm( 12513): DexOpt: --- END 'apk.classes2.zip' --- status=0x000e, process failed
E/dalvikvm( 12513): Unable to extract+optimize DEX from '/data/.../apk.classes2.zip'
```

# How to use
Add library dependency to dependencies{} block in build.gradle file  
```
dependencies{
   compile 'com.cantalou:dexoptfix:1.0.1'
}
```  

Add this code in the method attachBaseContext of Application before calling dex operation(e.g., MultiDex.install).
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