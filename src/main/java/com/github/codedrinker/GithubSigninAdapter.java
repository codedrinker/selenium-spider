package com.github.codedrinker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by codedrinker on 2019/1/21.
 */
public class GithubSigninAdapter {
    private WebDriver driver;

    public GithubSigninAdapter(WebDriver driver) {
        this.driver = driver;
    }

    public void signin() {
        String currentWindow = driver.getWindowHandle();
        for (String s : driver.getWindowHandles()) {
            if (!currentWindow.equals(s)) {
                driver.switchTo().window(s);
            }
        }
        WebElement usernameInput = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("login_field")));
        usernameInput.sendKeys("输入你的 Github 用户名");
        driver.findElement(By.id("password")).sendKeys("输入你的 Github 密码");
        driver.findElement(By.name("commit")).click();
    }
}
