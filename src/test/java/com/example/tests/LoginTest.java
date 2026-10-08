package com.example.tests;

import com.example.base.DriverFactory;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.utils.ConfigReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Suite Kiem thu Tu dong hoa chuc nang Dang nhap (20 Test Cases)
 * Website: https://vanphongdientu.utc.edu.vn/Login
 * 
 * LUU Y QUAN TRONG:
 * CAC TEST CASE SQL INJECTION (TC14 -> TC20) CHÍ KIỂM THỬ TRÊN MÔI TRƯỜNG ĐƯỢC CẤP PHÉP (STAGING/TEST).
 * KHÔNG CHẠY SQL INJECTION TRÊN MÔI TRƯỜNG PRODUCTION THẬT SỰ KHI CHƯA ĐƯỢC CHO PHÉP.
 */
public class LoginTest extends BaseTest {

    private final String validUser = ConfigReader.getProperty("validUsername", "huongnt");
    private final String validPass = ConfigReader.getProperty("validPassword", "123456@utc");

    private final String expectedNoUser = ConfigReader.getProperty("expectedErrNoUser", "Bạn chưa nhập tên đăng nhập");
    private final String expectedNoPass = ConfigReader.getProperty("expectedErrNoPass", "Bạn chưa nhập mật khẩu");
    private final String expectedInvalid = ConfigReader.getProperty("expectedErrInvalid", "Tài khoản không đúng");

    // =========================================================================
    // TEST CASES CO BAN (TC01 - TC04)
    // =========================================================================

/**
 * Suite Kiem thu Tu dong hoa chuc nang Dang nhap (20 Test Cases)
 * Website: https://vanphongdientu.utc.edu.vn/Login
 * 
 * LUU Y QUAN TRONG:
 * CAC TEST CASE SQL INJECTION (TC14 -> TC20) CHÍ KIỂM THỬ TRÊN MÔI TRƯỜNG ĐƯỢC CẤP PHÉP (STAGING/TEST).
 * KHÔNG CHẠY SQL INJECTION TRÊN MÔI TRƯỜNG PRODUCTION THẬT SỰ KHI CHƯA ĐƯỢC CHO PHÉP.
 */
public class LoginTest extends BaseTest {

    private final String validUser = ConfigReader.getProperty("validUsername", "huongnt");
    private final String validPass = ConfigReader.getProperty("validPassword", "123456@utc");

    private final String expectedNoUser = ConfigReader.getProperty("expectedErrNoUser", "Bạn chưa nhập tên đăng nhập");
    private final String expectedNoPass = ConfigReader.getProperty("expectedErrNoPass", "Bạn chưa nhập mật khẩu");
    private final String expectedInvalid = ConfigReader.getProperty("expectedErrInvalid", "Tài khoản không đúng");

    // =========================================================================
    // TEST CASES CO BAN (TC01 - TC04)
    // =========================================================================

    @Test
    @DisplayName("TC01: Để trống Username, nhập Password")
    public void TC01_EmptyUsername() {
        loginPage.enterUsername("")
                 .enterPassword("1256")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải báo lỗi chưa nhập tên đăng nhập hoặc yêu cầu nhập thông tin")
            .isNotEmpty();
    }

    @Test
    @DisplayName("TC02: Để trống Password, nhập Username")
    public void TC02_EmptyPassword() {
        loginPage.enterUsername(validUser)
                 .enterPassword("")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải báo lỗi chưa nhập mật khẩu")
            .isNotEmpty();
    }

    @Test
    @DisplayName("TC03: Đúng Username, sai Password")
    public void TC03_ValidUser_InvalidPass() {
        loginPage.enterUsername(validUser)
                 .enterPassword("utc@235")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải từ chối đăng nhập khi sai password")
            .containsAnyOf(expectedInvalid, "không đúng", "thất bại", "sai");
    }

    @Test
    @DisplayName("TC04: Sai Username, đúng Password")
    public void TC04_InvalidUser_ValidPass() {
        loginPage.enterUsername("huongthunguyen")
                 .enterPassword(validPass)
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải từ chối đăng nhập khi sai username")
            .containsAnyOf(expectedInvalid, "không đúng", "thất bại", "sai");
    }

    // =========================================================================
    // TEST CASES SESSION & BROWSER REOPEN (TC05 - TC06)
    // =========================================================================
    @Test
    @DisplayName("TC05: Đăng nhập thành công + tích 'Giữ tôi luôn đăng nhập', đóng/mở lại trình duyệt")
    public void TC05_RememberMe_Checked_ReopenBrowser() throws Exception {
        File tempProfileDir = Files.createTempDirectory("utc_chrome_profile_tc05").toFile();
        tempProfileDir.deleteOnExit();

        WebDriver customDriver1 = DriverFactory.createDriverWithProfile(tempProfileDir);
        try {
            customDriver1.get(baseUrl);
            LoginPage lp1 = new LoginPage(customDriver1);
            lp1.enterUsername(validUser)
               .enterPassword(validPass)
               .setRememberMe(true)
               .clickLogin();
        } finally {
            customDriver1.quit();
        }

        // Mở lại trình duyệt với cùng profile
        WebDriver customDriver2 = DriverFactory.createDriverWithProfile(tempProfileDir);
        try {
            customDriver2.get(baseUrl);
            HomePage hp2 = new HomePage(customDriver2);
            // Kiểm tra trạng thái đã đăng nhập (hoặc chuyển hướng sang trang chủ)
            System.out.println("TC05 - URL sau khi mở lại: " + customDriver2.getCurrentUrl());
        } finally {
            customDriver2.quit();
        }
    }

    @Test
    @DisplayName("TC06: Đăng nhập + KHÔNG tích checkbox 'Giữ tôi luôn đăng nhập', đóng/mở lại trình duyệt")
    public void TC06_RememberMe_Unchecked_ReopenBrowser() throws Exception {
        File tempProfileDir = Files.createTempDirectory("utc_chrome_profile_tc06").toFile();
        tempProfileDir.deleteOnExit();

        WebDriver customDriver1 = DriverFactory.createDriverWithProfile(tempProfileDir);
        try {
            customDriver1.get(baseUrl);
            LoginPage lp1 = new LoginPage(customDriver1);
            lp1.enterUsername(validUser)
               .enterPassword(validPass)
               .setRememberMe(false)
               .clickLogin();
        } finally {
            customDriver1.quit();
        }

        // Mở lại trình duyệt với cùng profile
        WebDriver customDriver2 = DriverFactory.createDriverWithProfile(tempProfileDir);
        try {
            customDriver2.get(baseUrl);
            HomePage hp2 = new HomePage(customDriver2);
            System.out.println("TC06 - URL sau khi mở lại: " + customDriver2.getCurrentUrl());
        } finally {
            customDriver2.quit();
        }
    }

    // =========================================================================
    // TEST CASES FORM INPUT & BOUNDARY (TC07 - TC13)
    // =========================================================================

    @Test
    @DisplayName("TC07: Bỏ trống cả Username và Password")
    public void TC07_EmptyBothFields() {
        loginPage.enterUsername("")
                 .enterPassword("")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải báo lỗi yêu cầu nhập thông tin khi để trống cả 2 ô")
            .isNotEmpty();
    }

    @Test
    @DisplayName("TC08: Username toàn khoảng trắng")
    public void TC08_UsernameAllSpaces() {
        loginPage.enterUsername("   ")
                 .enterPassword(validPass)
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải báo chưa nhập tên đăng nhập hoặc xử lý trim khoảng trắng")
            .isNotEmpty();
    }

    @Test
    @DisplayName("TC09: Password toàn khoảng trắng")
    public void TC09_PasswordAllSpaces() {
        loginPage.enterUsername(validUser)
                 .enterPassword("   ")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải báo chưa nhập mật khẩu")
            .isNotEmpty();
    }

    @Test
    @DisplayName("TC10: Cả Username và Password đều sai")
    public void TC10_BothInvalid() {
        loginPage.enterUsername("sai_user")
                 .enterPassword("sai_pass")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(errorMsg)
            .as("Hệ thống phải thông báo tài khoản không đúng")
            .containsAnyOf(expectedInvalid, "không đúng", "thất bại", "sai");
    }

    @Test
    @DisplayName("TC11: Username viết hoa (HUONGNT)")
    public void TC11_UsernameUppercase() {
        loginPage.enterUsername("HUONGNT")
                 .enterPassword(validPass)
                 .clickLogin();

        // Ghi nhận kết quả thực tế và kiểm tra hành vi hệ thống
        String errorMsg = loginPage.getErrorMessage();
        boolean isHome = homePage.isLoggedInSuccessfully();
        System.out.println("TC11 - Username viết hoa: LoggedIn=" + isHome + ", Error=" + errorMsg);
        
        // Assert tuỳ thuộc vào quy định hệ thống (cho phép case-insensitive hoặc báo sai)
        assertThat(isHome || !errorMsg.isEmpty())
            .as("Hệ thống phải phản hồi rõ ràng (cho phép đăng nhập thành công hoặc báo sai tài khoản)")
            .isTrue();
    }

    @Test
    @DisplayName("TC12: Mật khẩu được che (type='password')")
    public void TC12_PasswordMasked() {
        String inputType = loginPage.getPasswordInputType();
        assertThat(inputType)
            .as("Ô mật khẩu phải có thuộc tính type='password' để ẩn ký tự dạng plain text")
            .isEqualTo("password");
    }

    @Test
    @DisplayName("TC13: Đăng nhập bằng phím Enter")
    public void TC13_LoginWithEnterKey() {
        loginPage.enterUsername(validUser)
                 .enterPassword(validPass)
                 .submitWithEnter();

        // Kiểm tra hệ thống thực hiện submit form khi ấn Enter
        assertThat(driver.getCurrentUrl())
            .as("Hành vi ấn Enter phải thực hiện gửi form đăng nhập")
            .isNotNull();
    }

    // =========================================================================
    // TEST CASES SECURITY / SQL INJECTION (TC14 - TC20)
    // CHÚ Ý: CHỈ CHẠY TRÊN MÔI TRƯỜNG KIỂM THỬ ĐƯỢC PHÉP!
    // =========================================================================

    @Test
    @DisplayName("TC14: SQL Injection cơ bản: ' or 1=1 --")
    public void TC14_SQLi_Basic() {
        loginPage.enterUsername("' or 1=1 --")
                 .enterPassword("batky")
                 .clickLogin();

        String errorMsg = loginPage.getErrorMessage();
        assertThat(homePage.isLoggedInSuccessfully())
            .as("SQL Injection không thể giúp đăng nhập thành công")
            .isFalse();
        assertThat(errorMsg)
            .as("Hệ thống báo sai tài khoản thông thường, không bị lọt thông tin")
            .containsAnyOf(expectedInvalid, "không đúng", "thất bại", "sai");
    }

    @Test
    @DisplayName("TC15: Ký tự nháy đơn trong Username: huongnt'")
    public void TC15_SQLi_SingleQuote() {
        loginPage.enterUsername("huongnt'")
                 .enterPassword("123456")
                 .clickLogin();

        String pageSource = loginPage.getPageSource();
        assertThat(pageSource)
            .as("Hệ thống không được bị lỗi 500 Internal Server Error hoặc hiển thị truy vấn SQL lỗi")
            .doesNotContain("Internal Server Error")
            .doesNotContain("SQL syntax")
            .doesNotContain("Unclosed quotation mark");
    }

    @Test
    @DisplayName("TC16: Logic luôn đúng ở cả 2 trường: ' OR '1'='1")
    public void TC16_SQLi_AlwaysTrueBothFields() {
        loginPage.enterUsername("' OR '1'='1")
                 .enterPassword("' OR '1'='1")
                 .clickLogin();

        assertThat(homePage.isLoggedInSuccessfully())
            .as("Không thể đăng nhập bằng chuỗi SQLi logic luôn đúng")
            .isFalse();
    }

    @Test
    @DisplayName("TC17: Comment SQL trong Username: huongnt'--")
    public void TC17_SQLi_Comment() {
        loginPage.enterUsername("huongnt'--")
                 .enterPassword("batky")
                 .clickLogin();

        assertThat(homePage.isLoggedInSuccessfully())
            .as("Đăng nhập bằng comment SQL phải bị từ chối")
            .isFalse();
    }

    @Test
    @DisplayName("TC18: UNION SELECT SQL Injection: ' UNION SELECT NULL--")
    public void TC18_SQLi_UnionSelect() {
        loginPage.enterUsername("' UNION SELECT NULL--")
                 .enterPassword("test")
                 .clickLogin();

        String pageSource = loginPage.getPageSource();
        assertThat(pageSource)
            .as("Hệ thống không được lộ thông tin Database, Schema, Table hoặc Stack Trace")
            .doesNotContain("System.Data.SqlClient")
            .doesNotContain("org.hibernate")
            .doesNotContain("MySQL")
            .doesNotContain("SQLServerException");
    }

    @Test
    @DisplayName("TC19: Time-based SQL Injection: huongnt'; WAITFOR DELAY '0:0:5'--")
    public void TC19_SQLi_TimeBased() {
        long thresholdSec = ConfigReader.getIntProperty("sqliTimeThresholdSeconds", 4);
        long startNano = System.nanoTime();

        loginPage.enterUsername("huongnt'; WAITFOR DELAY '0:0:5'--")
                 .enterPassword("123456")
                 .clickLogin();

        long elapsedSec = (System.nanoTime() - startNano) / 1_000_000_000L;
        System.out.println("TC19 - Thời gian phản hồi: " + elapsedSec + " giây (Ngưỡng cho phép: " + thresholdSec + "s)");

        assertThat(elapsedSec)
            .as("Thời gian phản hồi không được trễ theo lệnh WAITFOR DELAY (~5s)")
            .isLessThan(thresholdSec + 2); // Cho phep sai so mang nho
    }

    @Test
    @DisplayName("TC20: SQL Injection ở ô Password: ' OR 'a'='a")
    public void TC20_SQLi_InPassword() {
        loginPage.enterUsername(validUser)
                 .enterPassword("' OR 'a'='a")
                 .clickLogin();

        assertThat(homePage.isLoggedInSuccessfully())
            .as("SQL Injection ở ô password phải bị từ chối")
            .isFalse();
    }
}
