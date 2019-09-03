package com.cantalou.dexoptfix;

/**
 * If we called DexFile.loadDex() or new DexClassLoader(path), android will fork a sub process to execute dexopt command.
 * In some devices , sub process will receive a signal "SIGALRM" two seconds after process start. Then dexopt will
 * fail with code 0x000e. You app will crash with an exception: "IOException : unable to open DEX file" or "ClassNotFoundException" or "ClassNoDefinedError"
 *
 * @author LinZhiWei
 */
public class DexOptFix {
    public static void fix() {
        System.loadLibrary("dexoptfix");
    }
}
