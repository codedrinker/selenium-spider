package com.github.codedrinker;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sun.awt.OSInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by codedrinker on 2019/1/21.
 */
public class SearchSpider {
    public static void main(String[] args) {
        // 配置环境
        initialProperties();

        // 获取 WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // 跳转地址
            driver.get("https://www.csdn.net/");

            // 获取首页元素
            findIndexPageArticles(driver);

            // 搜索元素
            searchArticles(driver);

            // 发布博客
            post(driver);

            // 关闭
            driver.quit();
            System.out.println("程序执行完毕");
        } catch (Exception e) {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                String savePath = SearchSpider.class.getClassLoader().getResource("").getPath();
                //复制内容到指定文件中
                FileUtils.copyFile(scrFile, new File(savePath + UUID.randomUUID().toString() + ".png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void post(WebDriver driver) {
        DispatchMarkdown markdown = new DispatchMarkdown();
        markdown.setTitle("这是我通过 Selenium 发布的文章标题");
        markdown.setContent("这是我通过 Selenium 发布的文章内容");
        new CSDNMarkdownDispatcher(driver).post(markdown);
    }

    private static void searchArticles(WebDriver driver) {
        // 获取元素
        WebElement searchInput = driver.findElement(By.id("toolber-keyword"));

        // 设置搜索内容
        searchInput.sendKeys("码匠笔记");

        // 获取搜索按钮
        WebElement searchBtn = driver.findElement(By.className("btn-search"));

        // 点击
        searchBtn.click();

        String winHandleBefore = driver.getWindowHandle();
        for (String winHandle : driver.getWindowHandles()) {
            if (winHandle.equals(winHandleBefore)) {
                continue;
            }
            driver.switchTo().window(winHandle);
            break;
        }

        System.out.println("搜索成功");

        parseArticles(driver);
    }

    private static void findIndexPageArticles(WebDriver driver) throws InterruptedException {
        int count = 0;
        // 获取 200 条首页内容，获取 200 以后开始搜索内容
        while (count < 200) {
            int pageCount = driver.findElements(By.className("clearfix")).size();
            count += pageCount;
            System.out.println("当前页数:" + count);
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
    }

    private static void parseArticles(WebDriver driver) {
        int count = 500;
        List<WebElement> articles = driver.findElements(By.tagName("dl"));
        for (WebElement article : articles) {
            try {
                String title = article.findElement(By.className("limit_width")).getText();
                String link = article.findElement(By.className("search-link")).findElement(By.tagName("a")).getAttribute("href");
                System.out.println(title);
                System.out.println(link);
                count++;
                // 获取 500 个，多了直接结束
                if (count > 500) return;
            } catch (Exception e) {
                continue;
            }
        }
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().contains("码匠笔记- CSDN搜索");
            }
        });
        WebElement nextBtn = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("btn-next")));
        nextBtn.click();
        parseArticles(driver);
    }

    private static void initialProperties() {
        switch (OSInfo.getOSType()) {
            case LINUX:
                System.setProperty("webdriver.chrome.driver", SearchSpider.class.getClassLoader().getResource("chromedriver_linux64").getPath());
                break;
            case MACOSX:
                System.setProperty("webdriver.chrome.driver", SearchSpider.class.getClassLoader().getResource("chromedriver_mac64").getPath());
                break;
            case WINDOWS:
                System.setProperty("webdriver.chrome.driver", SearchSpider.class.getClassLoader().getResource("chromedriver_win32.exe").getPath());
                break;
            default:
                throw new RuntimeException("不支持当前操作系统类型");
        }
    }
}
