@echo off
chcp 65001 > NUL
echo ===================================================
echo   CHẠY TỰ ĐỘNG HÓA KIỂM THỬ ĐĂNG NHẬP (20 TEST CASES)
echo ===================================================

SET MVN_PATH="C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.1\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"

if exist %MVN_PATH% (
    %MVN_PATH% test %*
) else (
    mvn test %*
)

echo.
echo Hoan thanh! Kiem tra ket qua report hoac screenshot tai target/screenshots/
pause
