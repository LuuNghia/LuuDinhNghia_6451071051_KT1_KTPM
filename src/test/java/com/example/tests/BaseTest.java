package com.example.tests;

import com.example.base.DriverFactory;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.utils.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lop co so quan ly lifecycle cua WebDriver (Khoi tao va Dong driver, Chup anh khi failure)
 */
public class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected String baseUrl;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        baseUrl = ConfigReader.getProperty("baseUrl", "https://vanphongdientu.utc.edu.vn/Login");
        driver = DriverFactory.createDriver();
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        if (driver != null) {
            try {
                // Chup anh man hinh luu vao target/screenshots
                captureScreenshot(testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_"));
            } catch (Exception e) {
                System.err.println("Loi khi chup screenshot: " + e.getMessage());
            } finally {
                driver.quit();
            }
        }
    }

    /**
     * Chup va luu screenshot man hinh hien tai
     */
    protected void captureScreenshot(String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Path screenshotDir = Paths.get("target", "screenshots");
                Files.createDirectories(screenshotDir);

                Path destPath = screenshotDir.resolve(testName + "_" + timestamp + ".png");
                Files.copy(scrFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot luu tai: " + destPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Khong the tao screenshot: " + e.getMessage());
        }
    }
}
