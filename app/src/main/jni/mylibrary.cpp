//
// Created by longbeach-user on 16/10/11.
//
#include "com_example_mvpdemo_utils_NativeHelper.h"

JNIEXPORT jstring JNICALL Java_com_example_mvpdemo_utils_NativeHelper_getString
        (JNIEnv *env, jobject obj) {
    return (*env).NewStringUTF("uhh.......");
}

