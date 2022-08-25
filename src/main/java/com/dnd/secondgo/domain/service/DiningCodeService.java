package com.dnd.secondgo.domain.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.dnd.secondgo.config.AppConfig;
import com.dnd.secondgo.domain.dto.KakaoResponseDto.Document;

@Service
public class DiningCodeService {

	private final String WEB_DRIVER_ID = "webdriver.chrome.driver"; 																	// 드라이버 ID
	private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{AppConfig.getChromePath()}).toString();   // 드라이버 경로
	
	WebDriver driver = null;
	ChromeOptions options = null;
	JavascriptExecutor executor = null;
	WebDriverWait webDriverWait = null;
	HashMap<String, HashMap<String, Object>> chromePrefs = new HashMap<String, HashMap<String, Object>>();
	HashMap<String, Object> defaultContentSettingValues = new HashMap<String, Object>();
	
	
	public DiningCodeService() {
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
		defaultContentSettingValues.put("javascript", 2);
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
	}
	
	public void driverInit() {
		driver = new ChromeDriver(options);
	}	
	public void dinignCodeCrawling(Document document) throws ParseException {
		driver.get("http://www.diningcode.com/search?query="+document.address_name+", " + document.place_name);
		System.out.println(document.address_name+", "+ document.place_name);
		
		List<WebElement> element = driver.findElements(By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[3]/ol/li"));
		if(element.size() > 0) {			
			org.jsoup.nodes.Document dom = Jsoup.parse(element.get(0).getAttribute("innerHTML"));

			// ****** 리뷰 및 후기 갯수 가져오기 *****
			Elements reviewCntElements = dom.select(".heart");
			if(reviewCntElements.size() > 0) {
				String reviewCnt = reviewCntElements.get(0).text();
				document.setReviewCnt(Integer.parseInt(reviewCnt));
			} else {
				document.setReviewCnt(0);
			}
			// ******************************
			
			// ****** 해시태그 가져오기 ***********
			Elements tagElements = dom.select(".Hash");
			if(tagElements.size() > 0) {
				String tag = tagElements.get(0).text();
				document.setTag(tag);
			}
			// ******************************
		
			// ****** imgUrl 데이터 가져오기 *****
			Elements imgUrlElements = dom.select(".title");
			List<String> imgUrlList = new ArrayList<>();
			if(imgUrlElements.size() > 0) {	
				String imgUrl = imgUrlElements.get(0).attr("src");
				imgUrlList.add(imgUrl);
			}
			document.setImg_url(imgUrlList);
			// ******************************

			// ****** 한줄 리뷰 데이터 가져오기 *****
			Elements reviewTextElements = dom.select(".Review");
			String reviewText = "";
			if(reviewTextElements.size() > 0) {
				reviewText = reviewTextElements.get(0).text();
			}
			document.setReviewText(reviewText);
			// ******************************
		}
	}
	public void driverQuit() {
		if(driver != null) {			
			driver.quit();
		}
	}
}
