LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := mylibrary
LOCAL_SRC_FILES =: mylibrary.cpp
include $(BUILD_SHARED_LIBRARY)