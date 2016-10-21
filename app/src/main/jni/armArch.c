#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <cpu-features.h>
#include <inttypes.h>
#include<Android/log.h>

#define TAG		"armArch_test"
#define logI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)

jstring
Java_com_github_hiteshsondhi88_libffmpeg_ArmArchHelper_cpuArchFromJNI(JNIEnv* env, jobject obj)
{
	// Maximum we need to store here is ARM v7-neon
	// Which is 11 char long, so initializing a character array of length 11
	char arch_info[13] = "";

//	logI("ANDROID_CPU_FAMILY_ARM = %d",ANDROID_CPU_FAMILY_ARM);
//	logI("ANDROID_CPU_ARM_FEATURE_ARMv7 = %d",ANDROID_CPU_ARM_FEATURE_ARMv7);
//	logI("ANDROID_CPU_ARM_FEATURE_NEON = %d",ANDROID_CPU_ARM_FEATURE_NEON);
//	logI("android_getCpuFamily = %d",android_getCpuFamily());
//	logI("android_getCpuFeatures = %" PRIu64 "",android_getCpuFeatures());


	int cpu = android_getCpuFamily();
	// checking if CPU is of ARM family or not
	if (cpu == ANDROID_CPU_FAMILY_ARM) {
		strcpy(arch_info, "ARM");
	} else if (cpu == ANDROID_CPU_FAMILY_ARM64) {
		strcpy(arch_info, "ARM_64");
	}
	// checking if CPU is ARM v7 or not
	uint64_t cpuFeatures = android_getCpuFeatures();
	if ((cpuFeatures & ANDROID_CPU_ARM_FEATURE_ARMv7) != 0) {
		strcat(arch_info, " v7");

		// checking if CPU is ARM v7 Neon
		if((cpuFeatures & ANDROID_CPU_ARM_FEATURE_NEON) != 0) {
			strcat(arch_info, "-neon");
		}
	}
	return (*env)->NewStringUTF(env, arch_info);
}
