package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    public static final String URL = "https://vanphongdientu.utc.edu.vn/Login";
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("userpwd");
    private final By loginButton = By.cssSelector("input.submit_login");

    // Khai báo locators bổ sung cho checkbox và thông báo lỗi
    private final By rememberMeCheckboxLocator = By.id("persistent");
    private final By rememberMeLabelLocator = By.cssSelector("label.check, label[for='persistent']");
    private final By errorMessageLocator = By.cssSelector(".error, .alert, .message, .login-error, span.error-msg");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(URL);
        return this;
    }


    public HomePage loginAs(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        return new HomePage(driver);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl() != null && driver.getCurrentUrl().contains("/Login");
    }

    /**
     * Nhập tên đăng nhập
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    /**
     * Nhập mật khẩu
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    /**
     * Bấm nút "Đăng nhập"
     */
    public void clickLogin() {
        click(loginButton);
    }

 
    public LoginPage setRememberMe(boolean check) {
        WebElement checkbox = driver.findElement(rememberMeCheckboxLocator);
        boolean isChecked = checkbox.isSelected();

        if (check != isChecked) {
            try {
                WebElement fakeBox = driver.findElement(rememberMeLabelLocator);
                fakeBox.click();
            } catch (Exception e) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", checkbox);
            }
        }
        return this;
    }

    /**
     * Đọc trạng thái checked của checkbox 
     */
    public boolean isRememberMeChecked() {
        WebElement checkbox = driver.findElement(rememberMeCheckboxLocator);
        return checkbox.isSelected();
    }

    /**
     * Gửi form bằng phím Enter tại ô Mật khẩu (phục vụ TC13)
     */
    public void submitWithEnter() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Lấy giá trị thuộc tính 'type' của ô Mật khẩu (phục vụ TC12)
     */
    public String getPasswordInputType() {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(passwordField));
        return input.getAttribute("type");
    }

    /**
     * Lấy thông báo lỗi từ JavaScript Alert hoặc Element trên DOM
     */
    public String getErrorMessage() {
        // 1. Kiểm tra JavaScript Alert dialog 
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            if (shortWait.until(ExpectedConditions.alertIsPresent()) != null) {
                String alertText = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();
                return alertText;
            }
        } catch (NoAlertPresentException ignored) {
        } catch (Exception ignored) {
        }

        // 2. Kiểm tra HTML5 validationMessage
        try {
            WebElement usernameInput = driver.findElement(usernameField);
            String valMsgUser = usernameInput.getAttribute("validationMessage");
            if (valMsgUser != null && !valMsgUser.isEmpty()) {
                return valMsgUser;
            }
            WebElement passwordInput = driver.findElement(passwordField);
            String valMsgPass = passwordInput.getAttribute("validationMessage");
            if (valMsgPass != null && !valMsgPass.isEmpty()) {
                return valMsgPass;
            }
        } catch (Exception ignored) {
        }

        // 3. Kiểm tra Element thông báo lỗi trên giao diện DOM
        try {
            WebElement errElem = driver.findElement(errorMessageLocator);
            if (errElem.isDisplayed()) {
                return errElem.getText();
            }
        } catch (Exception ignored) {
        }

        return driver.findElement(By.tagName("body")).getText();
    }

    /**
     * Lấy toàn bộ HTML Page Source
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    public By getUsernameField() {
        return usernameField;
    }

    public By getPasswordField() {
        return passwordField;
    }

    public By getLoginButton() {
        return loginButton;
    }
}
