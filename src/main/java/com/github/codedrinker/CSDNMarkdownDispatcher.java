package com.github.codedrinker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


/**
 * Created by codedrinker on 2019/1/21.
 */
public class CSDNMarkdownDispatcher {
    private WebDriver driver;

    public CSDNMarkdownDispatcher(WebDriver driver) {
        this.driver = driver;
    }

    void post(DispatchMarkdown dispatchMarkdown) {
        login();
        directToPostPage();
        publishPost(dispatchMarkdown);
    }

    private void publishPost(DispatchMarkdown dispatchMarkdown) {

        findElementUntil(By.className("article-bar__title")).clear();
        findElementUntil(By.className("article-bar__title")).sendKeys(dispatchMarkdown.getTitle());

        driver.findElement(By.className("editor__inner")).clear();
        driver.findElement(By.className("editor__inner")).sendKeys(dispatchMarkdown.getContent());

        findElementUntil(By.className("btn-publish")).click();

        List<WebElement> select = driver.findElements(By.tagName("select"));

        Select selType = new Select(select.get(0));
        selType.selectByIndex(1);
        Select radChl = new Select(select.get(1));
        radChl.selectByIndex(12);
        findElementUntil(By.className("btn-c-blue")).click();
    }

    private void directToPostPage() {
        untilTitleLocated("CSDN");
        driver.get("https://mp.csdn.net/mdeditor?not_checkout=1#");
    }

    void login() {
        driver.get("https://passport.csdn.net/account/login");
        WebElement githubSigninLink = findElementUntil(By.className("icon-github"));
        githubSigninLink.click();
        new GithubSigninAdapter(driver).signin();
    }

    protected WebElement findElementUntil(By by) {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
        }
        return new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    protected void untilTitleLocated(String title) {
        new WebDriverWait(driver, 20).until(ExpectedConditions.titleContains(title));
    }
}
