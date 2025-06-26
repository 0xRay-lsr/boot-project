//
// Created by 0xRay on 2025/4/25.
#include <jni.h>
#include <iostream>
#include <string>
// 引用本地h文件使用 "" 并非 <>
#include "cn_aps_jni_NativeDB.h"

JNIEXPORT jboolean JNICALL Java_cn_aps_jni_NativeDB_connect
(JNIEnv *env, jobject obj, jstring url) {
    const char *url_c = env->GetStringUTFChars(url, NULL);
    std::cout << "[C++] connectiong to db : " << url_c << std::endl;
    //模拟连接
    bool success = true;
    env->ReleaseStringUTFChars(url, url_c);
    return success ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL Java_cn_aps_jni_NativeDB_query
(JNIEnv *env, jobject obj, jstring sql) {
    const char *cSql = env->GetStringUTFChars(sql, NULL);
    std::cout << "[C++] executing SQL:" << cSql << std::endl;
    //间数据返回(模拟json)
    std::string result = R"([{"id":1,"name":"lishirui"},{"id":2,"name":"zhangsan"}])";
    env->ReleaseStringUTFChars(sql, cSql);
    return env->NewStringUTF(result.c_str());
}
