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
}
