package com.dnd.mountclim.domain.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.config.AppConfig;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document.Menu;

@Service
public class DinignCodeService {

	private final String WEB_DRIVER_ID = "webdriver.chrome.driver"; 																	// 드라이버 ID
	private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{AppConfig.getChromePath()}).toString();   // 드라이버 경로
	
	WebDriver driver = null;
	ChromeOptions options = null;
	JavascriptExecutor executor = null;
	WebDriverWait webDriverWait = null;
	HashMap<String, HashMap<String, Object>> chromePrefs = new HashMap<String, HashMap<String, Object>>();
	HashMap<String, Object> defaultContentSettingValues = new HashMap<String, Object>();
	
	
	public DinignCodeService() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("window-size=1920x1080");
		options.addArguments("--no-sandbox");
		options.addArguments("disable-dev-shm-usage");
		options.addArguments("disable-gpu");
		options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		options.addArguments("lang=ko_KR");
		options.addArguments("start-maximized");
		options.addArguments("disable-infobars");
		options.addArguments("--blink-settings=imagesEnabled=false");
		options.addArguments("--disable-extensions");
		options.addArguments("--mute-audio");
		options.addArguments("incognito");
		
		defaultContentSettingValues.put("cookies", 2);
		defaultContentSettingValues.put("images", 2);
		defaultContentSettingValues.put("plugins", 2);
		defaultContentSettingValues.put("popups", 2);
		defaultContentSettingValues.put("geolocation", 2);
		defaultContentSettingValues.put("notifications", 2);
		defaultContentSettingValues.put("auto_select_certificate", 2);
		defaultContentSettingValues.put("fullscreen", 2);
		defaultContentSettingValues.put("mouselock", 2);
		defaultContentSettingValues.put("mixed_script", 2);
		defaultContentSettingValues.put("media_stream", 2);
		defaultContentSettingValues.put("media_stream_mic", 2);
		defaultContentSettingValues.put("media_stream_camera", 2);
		defaultContentSettingValues.put("protocol_handlers", 2);
		defaultContentSettingValues.put("ppapi_broker", 2);
		defaultContentSettingValues.put("automatic_downloads", 2);
		defaultContentSettingValues.put("midi_sysex", 2);
		defaultContentSettingValues.put("push_messaging", 2);
		defaultContentSettingValues.put("ssl_cert_decisions", 2);
		defaultContentSettingValues.put("metro_switch_to_desktop", 2);
		defaultContentSettingValues.put("protected_media_identifier", 2);
		defaultContentSettingValues.put("app_banner", 2);
		defaultContentSettingValues.put("site_engagement", 2);
		defaultContentSettingValues.put("durable_storage", 2);
		
		chromePrefs.put("profile.default_content_setting_values", defaultContentSettingValues);
		options.setExperimentalOption("prefs", chromePrefs);
		driver = new ChromeDriver(options);
		executor = (JavascriptExecutor) driver;
		webDriverWait = new WebDriverWait(driver, 10);
	}
	
	public void dinignCodeCrawling(Document document) {
		System.out.println(document.road_address_name+", "+ document.place_name);
		driver.get("http://www.diningcode.com/search?query="+document.road_address_name+", " + document.place_name);
		
		String defaultWinHandle = driver.getWindowHandle();
		
		List<WebElement> placeList = driver.findElements(By.xpath("/html/body/div[1]/div/div/div[2]/div[3]/ol/li"));
		if(placeList.size() > 0) {
			WebElement place = placeList.get(0);
			place.click();
			for(String winHandle : driver.getWindowHandles()) {
				if(winHandle.equals(defaultWinHandle))
					continue;
				driver.switchTo().window(winHandle);
			}
			// ****** 리뷰 및 후기 갯수 가져오기 ******
			List<WebElement> reviewElement = driver.findElements(By.xpath("/html/body/div[3]/div/div[7]/div/div[2]/div[1]/div[1]/div[4]/p/strong"));
			if(reviewElement.size() > 0) {
				String review = reviewElement.get(0).getText().replace("점", "");
				document.setReview(Integer.parseInt(review.toString()));
				System.out.println("리뷰 : " + Integer.parseInt(review.toString()));
			} else {
				System.out.println("리뷰 : " + 0);
				document.setReview(0);
			}
			document.setDiscuss(0);		
			// ******************************
			
			// ****** 메뉴 데이터 가져오기 *********
			List<WebElement> menuIfo = driver.findElements(By.xpath("/html/body/div[3]/div/div[7]/div/div[2]/div[1]/div[4]/div[2]"));
			if(menuIfo.size() > 0) {				
				List<WebElement> moreBtn = menuIfo.get(0).findElements(By.xpath("/html/body/div[3]/div/div[7]/div/div[2]/div[1]/div[4]/div[2]/p[2]/a/span"));
				if(moreBtn.size() > 0) {
					moreBtn.get(0).click();
				}
			}
			
			List<WebElement> menuList = driver.findElements(By.xpath("/html/body/div[3]/div/div[7]/div/div[2]/div[1]/div[4]/div[2]/ul/li"));
			List<Menu> menus = new ArrayList<>();
			for(WebElement menuElement : menuList) {
				Menu menu = new Menu();
				List<WebElement> menuNameElement = menuElement.findElements(By.className("Restaurant_Menu"));
				if(menuNameElement.size() > 0) {
					String menuName = menuNameElement.get(0).getText();
					menu.setMenu_name(menuName);
					System.out.println("메뉴명 : " + menuName);
				}
				List<WebElement> menuPriceElement = menuElement.findElements(By.className("Restaurant_MenuPrice"));
				if(menuPriceElement.size() > 0) {
					String menuPrice = menuPriceElement.get(0).getText();
					menu.setMenu_price(menuPrice);
					System.out.println("메뉴가격 : " + menuPrice);
				}
				menus.add(menu);
			}
			document.setMenus(menus);
			// *****************************
			
			// ****** imgUrl 데이터 가져오기 ****
			List<WebElement> imgUrlList = driver.findElements(By.xpath("/html/body/div[3]/div/div[7]/div/div[2]/div[1]/div[1]/ul/li"));
			List<String> photoList = new ArrayList<>();
			for(WebElement imgUrl :imgUrlList) {
				WebElement imgSelector = imgUrl.findElement(By.cssSelector("img"));
				String link = imgSelector.getAttribute("src");
				System.out.println("이미지 링크 : " + link);
				photoList.add(link);
			}
			document.setImg_url(photoList);
			// *****************************
		}
	}
}
