#!/usr/bin/env bash
echo "==================================================="
echo "  CHẠY TỰ ĐỘNG HÓA KIỂM THỬ ĐĂNG NHẬP (20 TEST CASES)"
echo "==================================================="

MVN_PATH="/c/Program Files/JetBrains/IntelliJ IDEA 2026.2.1/plugins/maven-plugin/lib/maven3/bin/mvn"

if [ -f "$MVN_PATH" ]; then
    "$MVN_PATH" test "$@"
else
    mvn test "$@"
fi

echo ""
echo "Hoàn thành! Kiểm tra kết quả report hoặc screenshot tại target/screenshots/"
