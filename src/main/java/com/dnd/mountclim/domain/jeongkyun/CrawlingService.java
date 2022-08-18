package com.dnd.mountclim.domain.jeongkyun;

import com.dnd.mountclim.config.AppConfig;
import com.dnd.mountclim.domain.dto.KakaoResponseDto;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingService {

    private final int INDEX_NOT_FOUND = -1;
    private final String WEB_DRIVER_ID = "webdriver.chrome.driver";                                                            // 드라이버 ID
    private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{"chromedriver.exe"}).toString();  // 드라이버 경로
    private final String naverCrawlingPrefixUfl ="https://map.naver.com/v5/search/";
    private WebDriver driver = null;
    private WebDriverWait webDriverWait = null;
    private JavascriptExecutor executor = null;


    public CrawlingService() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        driver = new ChromeDriver(options);
        executor = (JavascriptExecutor) driver;
        webDriverWait = new WebDriverWait(driver, 10);
    }

    public void naverPlaceCrawling(List<KaKaoSearchResponseDto.Document> documents) {

//        for (KaKaoSearchResponseDto.Document doc : documents){
//            String crawlingUrl = naverCrawlingPrefixUfl + doc.phone;
////            driver.get(crawlingUrl);
////            WebElement webElement = driver.findElement(By.id("container"));
////            executor.executeScript("arguments[0].click();", webElement);
////            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("link_evaluation")));
////            List<WebElement> linkEvaluations = webElement.findElements(By.className("link_evaluation"));
//
//        }


    }

}
