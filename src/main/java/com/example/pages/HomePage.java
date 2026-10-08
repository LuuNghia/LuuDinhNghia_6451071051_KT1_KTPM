package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class HomePage extends BasePage {

    private final By mainContentLocator = By.cssSelector(".main-header, .user-info, .sidebar, #main-wrapper, body");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Kiem tra xem nguoi dung da vao Trang chu thanh cong chua
     */
    public boolean isLoggedInSuccessfully() {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Login")));
            String currentUrl = driver.getCurrentUrl();
            return currentUrl != null && !currentUrl.toLowerCase().contains("/login");
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
