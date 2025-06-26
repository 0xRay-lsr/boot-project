#!/bin/bash

set -e

# 🧭 项目结构配置
PACKAGE="cn.aps.jni"
CLASSNAME="NativeDB"
JAVA_SRC="src/main/java"
CPP_SRC="src/native"
CLASS_PATH="target/classes"
LIB_OUTPUT_DIR="${CPP_SRC}"

# 🧠 推导路径与文件名
JNI_HEADER_NAME="${CLASSNAME}.h"
FULL_CLASS="${PACKAGE}.${CLASSNAME}"
PACKAGE_PATH=$(echo ${PACKAGE} | tr '.' '/')
JAVA_FILE="${JAVA_SRC}/${PACKAGE_PATH}/${CLASSNAME}.java"
JAVA_HOME="/Library/Java/JavaVirtualMachines/zulu-19.jdk/Contents/Home"

# 🌍 自动检测系统类型
OS_NAME="$(uname -s)"
if [ "$OS_NAME" == "Darwin" ]; then
  PLATFORM="darwin"
  LIB_EXT="dylib"
elif [ "$OS_NAME" == "Linux" ]; then
  PLATFORM="linux"
  LIB_EXT="so"
else
  echo "❌ 不支持的系统: $OS_NAME"
  exit 3
fi

LIB_NAME="lib${CLASSNAME}.${LIB_EXT}"

echo ${LIB_NAME}
# 🧹 清理旧文件
echo "🧹 清理旧文件..."
rm -f "${JNI_HEADER_NAME}"
rm -f "${LIB_OUTPUT_DIR}/${LIB_NAME}"

# ✅ 编译 Java 类并生成 JNI 头文件
echo "🛠 编译 Java 并生成头文件..."
mkdir -p ${CLASS_PATH}
# 生产编译文件到脚本根目录javac -h . -d ${CLASS_PATH} ${JAVA_FILE}
#
javac -h ${CPP_SRC} -d ${CLASS_PATH} ${JAVA_FILE}

# ⚙️ 编译动态库
echo "⚙️ 编译 C++ 动态库..."
g++ -fPIC \
  -I"${JAVA_HOME}/include" \
  -I"${JAVA_HOME}/include/${PLATFORM}" \
  -shared -o "${LIB_OUTPUT_DIR}/${LIB_NAME}" "${CPP_SRC}/${CLASSNAME}.cpp"

echo "✅ 成功生成: ${LIB_OUTPUT_DIR}/${LIB_NAME}"