package com.github.codedrinker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.awt.OSInfo;

/**
 * Created by codedrinker on 2019/1/21.
 */
public class Spider {
    public static void main(String[] args) {
        // 配置环境
        initialProperties();

        // 获取 WebDriver
        WebDriver driver = new ChromeDriver();

        // 跳转地址
        driver.get("https://www.csdn.net/");

        // 关闭
        driver.quit();
    }

    private static void initialProperties() {
        switch (OSInfo.getOSType()) {
            case LINUX:
                System.setProperty("webdriver.chrome.driver", Spider.class.getClassLoader().getResource("chromedriver_linux64").getPath());
                break;
            case MACOSX:
                System.setProperty("webdriver.chrome.driver", Spider.class.getClassLoader().getResource("chromedriver_mac64").getPath());
                break;
            case WINDOWS:
                System.setProperty("webdriver.chrome.driver", Spider.class.getClassLoader().getResource("chromedriver_win32.exe").getPath());
                break;
            default:
                throw new RuntimeException("不支持当前操作系统类型");
        }
    }
}
