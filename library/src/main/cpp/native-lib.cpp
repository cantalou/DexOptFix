#include <jni.h>
#include <string>
#include <android/log.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>

#define  LOG_TAG    "DexOptFix"
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)

void signalHandler(int sigNum) {
    LOGW("pid %d hook receive signal %d", getpid(), sigNum);
}

void prepareHandler(void) {
    sighandler_t oldHandler = signal(SIGALRM, signalHandler);
    LOGW("pid %d prepareHandler, old sigalrm handler: %d, new handler: %d", getpid(), (long) oldHandler, (long) signalHandler);
}

void parentHandler(void) {
    sighandler_t oldHandler = signal(SIGALRM, signalHandler);
    LOGW("pid %d parentHandler, old sigalrm handler: %d, new handler: %d", getpid(), (long) oldHandler, (long) signalHandler);
}

void childHandler(void) {
    sighandler_t oldHandler = signal(SIGALRM, signalHandler);
    LOGW("pid %d childHandler,  old sigalrm handler: %d, new handler: %d, clear alarm %ds", getpid(), (long) oldHandler, (long) signalHandler, alarm(0));
}

/**
 * Register a custom method for signal "SIGALRM" and cancel the alarm clock
 */
void hookSignalAlarm() {
    sighandler_t oldHandler = signal(SIGALRM, signalHandler);
    LOGW("pid %d before hook , old sigalrm handler: %d, new handler: %d, clear alarm %ds", getpid(), (long) oldHandler, (long) signalHandler, alarm(0));
    pthread_atfork(prepareHandler, parentHandler, childHandler);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGW("DexOptFix JNI_OnLoad called");
    hookSignalAlarm();
    return JNI_VERSION_1_4;
}

