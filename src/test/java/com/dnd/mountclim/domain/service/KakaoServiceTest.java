package com.dnd.mountclim.domain.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.dnd.mountclim.config.AppConfig;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document.Menu;

@SpringBootTest
public class KakaoServiceTest {

	private final int INDEX_NOT_FOUND = -1;
	private final String WEB_DRIVER_ID = "webdriver.chrome.driver"; 															// 드라이버 ID
	private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{"chromedriver.exe"}).toString();  // 드라이버 경로
	
	@Value("${kakao.api.key}")
	private String KAKAO_APIKEY;	
	private String PLACE_URL = "http://place.map.kakao.com/26893165";

	@Test
	public void placeUrl_크롤링테스트() {
		Document document = new Document();
		document.setPlace_url(PLACE_URL);
		
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		
		driver.get(document.place_url);
		WebElement kakaoWrap = driver.findElement(By.id("kakaoWrap"));
		executor.executeScript("arguments[0].click();", kakaoWrap);	
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("link_evaluation")));
		
		// ****** 리뷰 및 후기 갯수 가져오기 ******
		List<WebElement> linkEvaluations = kakaoWrap.findElements(By.className("link_evaluation"));
		if(linkEvaluations.size() > 0) {
			if(linkEvaluations.size() == 1) {
				String discuss = linkEvaluations.get(0).getAttribute("data-cnt");
				document.setDiscuss(Integer.parseInt(discuss));
				document.setReview(0);
			} else {
				String discuss = linkEvaluations.get(0).getAttribute("data-cnt");
				String review = linkEvaluations.get(1).getAttribute("data-cnt");
				document.setDiscuss(Integer.parseInt(discuss));
				document.setReview(Integer.parseInt(review));	
			}
		} else {
			document.setDiscuss(0);
			document.setReview(0);
		}
		// ******************************
		
		// ****** 메뉴 데이터 가져오기 *********
		List<WebElement> menuInfos = kakaoWrap.findElements(By.className("info_menu"));
		List<Menu> menus = new ArrayList<>();
		for(WebElement menuInfo : menuInfos) {
			Menu menu = new Menu();
			List<WebElement> menuName = menuInfo.findElements(By.className("loss_word"));
			List<WebElement> menuPrice = menuInfo.findElements(By.className("price_menu"));
			
			if(menuName.size() > 0) {
				menu.setMenu_name(menuName.get(0).getText());
			}
			if(menuPrice.size() > 0) {				
				menu.setMenu_price(menuPrice.get(0).getText());
			}
			if(menuName.size() > 0 && !"".equals(menuName.get(0).getText())) {
				menus.add(menu);
			}
		}
		document.setMenus(menus);		
		// *****************************
		
		// ****** imgUrl 데이터 가져오기 ****
		List<WebElement> linkPhotos = kakaoWrap.findElements(By.className("link_photo"));
		List<String> photoList = new ArrayList<>();
		for(WebElement linkPhoto : linkPhotos) {
			String imgUrl = linkPhoto.getAttribute("style") + "";
			if("".equals(imgUrl)) {
				continue;
			}
			String img = "http:" + substringBetween(imgUrl, "\"", "\"");
			photoList.add(img);
		}
		document.setImg_url(photoList);		
		//*****************************
	}
	
	public String substringBetween(String str, String open, String close) {		
		if(str == null) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			final int end = str.indexOf(close, start + open.length());
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}
}
