package com.dnd.mountclim;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class NaverPlaceCrawlingTest {

    @Test
    public void 네이버_Jsoup_테스트(){
        String URL = "https://finance.naver.com/item/main.nhn?code=005930";
        String tUrl = "https://map.naver.com/v5/search/033-244-5115";
        Document doc;

        try {
            doc = Jsoup.connect(tUrl).get();
            Elements elem = doc.select("body");
            String[] str = elem.text().split(" ");

            Elements todaylist =doc.select(".new_totalinfo dl>dd");

            String juga = todaylist.get(3).text().split(" ")[1];
            String DungRakrate = todaylist.get(3).text().split(" ")[6];
            String siga =  todaylist.get(5).text().split(" ")[1];
            String goga = todaylist.get(6).text().split(" ")[1];
            String zeoga = todaylist.get(8).text().split(" ")[1];
            String georaeryang = todaylist.get(10).text().split(" ")[1];


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void 네이버_Selenium_테스트() {
        String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
        String WEB_DRIVER_PATH = "C:\\dnd\\chromedriver.exe"; //드라이버 경로

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);
        String url = "https://map.naver.com/v5/search/033-244-5115";

        try {
            driver.get(url);
            Thread.sleep(2000);
            driver.switchTo().frame(driver.findElement(By.id("entryIframe")));
//            System.out.println(driver.getPageSource());

//            Thread.sleep(2000);

            WebElement titleElement = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div[1]/div[1]/span[1]"));
            String title = titleElement.getText();

            WebElement ratingElement = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div[1]/div[2]/span[1]"));
            String rating = ratingElement.getText().replace("별점\n", "");

            WebElement reviewCntElement = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div[1]/div[2]/span[2]/a/em"));
            String reviewCnt = reviewCntElement.getText();



            List<WebElement> tapBtnElements = driver.findElements(By.xpath("/html/body/div[3]/div/div/div/div[5]/div/div/div/div"));
            String ab = tapBtnElements.get(0).getText();
            List<WebElement> menuElements = driver.findElements(By.xpath("/html/body/div[3]/div/div/div/div[6]/div/div[1]/div/ul"));
            List<WebElement> menuElements2 = driver.findElements(By.cssSelector("#app-root > div > div > div > div:nth-child(6) > div > div.place_section.no_margin > div"));
            for(WebElement we : menuElements2){
                System.out.println(we.getText());
            }
            List<WebElement> menuElements3 = driver.findElements(By.xpath("/html/body/div[3]/div/div/div/div[6]/div/div[1]/div/ul/li[1]/a/div/div[1]/div/span"));
            for(WebElement we : menuElements3){
                System.out.println(we.getText());
            }



            System.out.println("제목: [" + title + "]");
            System.out.println("별점: [" + rating + "]");
            System.out.println("리뷰 개수: [" + reviewCnt + "]");
//            System.out.println("메뉴: [" + menuCnt + "]");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }

    }

}
