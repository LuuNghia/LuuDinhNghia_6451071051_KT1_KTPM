package com.example.base;

import com.example.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.time.Duration;

/**
 * Factory khoi tao WebDriver theo thiet lap tu config.properties va Slide Buoi 8
 */
public class DriverFactory {

    public static WebDriver createDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBooleanProperty("headless", false);
        return createDriver(browser, headless, null);
    }

    public static WebDriver createDriverWithProfile(File profileDir) {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBooleanProperty("headless", false);
        return createDriver(browser, headless, profileDir);
    }

    public static WebDriver createDriver(String browser, boolean headless, File customProfileDir) {
        WebDriver driver;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                if (customProfileDir != null) {
                    firefoxOptions.addArguments("-profile", customProfileDir.getAbsolutePath());
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                if (customProfileDir != null) {
                    edgeOptions.addArguments("user-data-dir=" + customProfileDir.getAbsolutePath());
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");

                // Cau hinh Headless theo Slide 60 (Buoi 8)
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                }

                if (customProfileDir != null) {
                    chromeOptions.addArguments("user-data-dir=" + customProfileDir.getAbsolutePath());
                }
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        int implicitWaitSec = ConfigReader.getIntProperty("implicitWait", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSec));

        if (!headless) {
            driver.manage().window().maximize();
        }

        return driver;
    }
}
