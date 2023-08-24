# CMakeLists.txt
cmake_minimum_required(VERSION 3.10.2)

project("TestSample")

# Set the path to the Android NDK.
set(ANDROID_NDK /Users/macbook/Library/Android/sdk/ndk/25.1.8937393)

# Set additional C++ compiler flags
set(CMAKE_CXX_FLAGS "-Werror -Wall -Wno-error=deprecated-declarations -error-limit=0")

# Set the name for your native library.
set(LIBRARY_NAME tlsTunnelWrapper)

# Specify the source files for your native library.
add_library(${LIBRARY_NAME} SHARED tls_jni.c)

target_include_directories(${LIBRARY_NAME} PRIVATE ${CMAKE_SOURCE_DIR}/include  ${KOTLIN_NATIVE_PATH}/kotlinx-coroutines-core-native)

target_link_directories(${LIBRARY_NAME} PRIVATE ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/)
target_link_libraries(${LIBRARY_NAME} log android -ltlsTunnelAndLib)

