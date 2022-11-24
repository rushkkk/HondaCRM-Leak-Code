LOCAL_PATH				:=	$(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE			:=	libcrypto
LOCAL_SRC_FILES			:=	libs/libcrypto.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE			:=	libssl
LOCAL_SRC_FILES			:=	libs/libssl.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE			:=	VehicleCryptoLib
LOCAL_SRC_FILES			:=	AuthenticationCrypto.cpp AuthenticationCrypto.h \
							CommonCrypto.cpp CommonCrypto.h \
							Util.h \
							RegistrationCrypto.cpp RegistrationCrypto.h \
							SecurityDataManager.cpp SecurityDataManager.h \
							SecurityFileManager.cpp SecurityFileManager.h \
							SecurityInformation.h \
							VehicleCryptoCommon.h \
							VehicleCryptoLib.cpp \
							VehicleCryptoService.cpp VehicleCryptoService.h
LOCAL_ARM_MODE			:=	thumb
LOCAL_LDLIBS			:=	-ldl -llog $(call host-path, $(LOCAL_PATH)/libs/libssl.so) $(call host-path, $(LOCAL_PATH)/libs/libcrypto.so)
LOCAL_C_INCLUDES		+=	$(LOCAL_PATH)/openssl/include \
							$(LOCAL_PATH)/openssl/include/openssl
LOCAL_SHARED_LIBRARIES	+=	libssl libcrypto
LOCAL_CFLAGS			+=	$(LOCAL_C_INCLUDES:%=-I%)
include $(BUILD_SHARED_LIBRARY)
