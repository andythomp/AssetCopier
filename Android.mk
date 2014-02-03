LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src) 
LOCAL_PACKAGE_NAME := AssetCopier
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v13

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_MODULE_OWNER := google
LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)
