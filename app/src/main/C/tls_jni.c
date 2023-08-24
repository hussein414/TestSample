#include "tls_jni.h"
#include <jni.h>
JavaVM* g_jvm;
JNIEnv* tls_env;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    init_tls_tunnel_lib();
    g_jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT jstring JNICALL
Java_com_example_testsample_vpnclient_tlsTunnel_getVpnAddress(JNIEnv *env, jobject thiz) {
    jstring javaString = (*env)->NewStringUTF(env, getTunnelParams());
    return javaString;
}

JNIEXPORT void JNICALL
Java_com_example_testsample_vpnclient_tlsTunnel_setTunnelFd(JNIEnv *env, jobject thiz, jint fd) {
    setTunneFD(fd);
}

JNIEXPORT jint JNICALL
Java_com_example_testsample_vpnclient_tlsTunnel_tunnelStop(JNIEnv *env, jobject thiz) {
    tls_tunnel_stop();
}

JNIEXPORT jint JNICALL
Java_com_example_testsample_vpnclient_tlsTunnel_tunnelStart(JNIEnv *env, jobject thiz, jstring juuid,
                                                            jstring make_config) {
    const char *config_name = (*env)->GetStringUTFChars(env,make_config, 0);
    const char *uuid = (*env)->GetStringUTFChars(env,juuid, 0);
    tls_tunnel_start(uuid,config_name);
    return 0;
}