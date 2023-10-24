#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ecarxspeedometer_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_ecarxspeedometer_service_SpeedometerCDataSource_createServiceBinder(
        JNIEnv *env, jobject thiz) {
    // TODO: implement createServiceBinder()
}