package com.dnd.mountclim.domain.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dnd.mountclim.config.AppConfig;
import com.dnd.mountclim.domain.dto.KakaoResponseDto;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document.Menu;

@Service
public class KakaoService {

	private final int INDEX_NOT_FOUND = -1;
	private final String WEB_DRIVER_ID = "webdriver.chrome.driver"; 															// 드라이버 ID
	private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{"chromedriver.exe"}).toString();  // 드라이버 경로
	
	@Value("${kakao.api.key}")
	private String KAKAO_APIKEY;	
	
	KakaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;
	
	WebDriver driver = null;
	WebDriverWait webDriverWait = null;
	JavascriptExecutor executor = null;

	public KakaoService() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		driver = new ChromeDriver(options);
		executor = (JavascriptExecutor) driver;
		webDriverWait = new WebDriverWait(driver, 10);
	}
	
	public ResponseEntity<KakaoResponseDto> kakaoApi(
		String food,
		String latitude,
		String longitude,
		String radius,
		String round) throws Exception {
		RestTemplate restTemplate = new RestTemplate();			
		HttpHeaders headers = new HttpHeaders();
		KakaoResponseDto kakaoResponseDto = null;
		newKakaoResponseDto = new KakaoResponseDto();
		newDocuments = new ArrayList<>();
		try {
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			int page = 1;
			while(true) {	
				headers.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
				String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
						+ "&category_group_code=" + "FD6"
						+ "&x=" + longitude
						+ "&y=" + latitude
						+ "&radius=" + radius
						+ "&page=" + page;
				
				ResponseEntity<KakaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KakaoResponseDto.class);
				kakaoResponseDto = responseEntity.getBody();	
				
				if(kakaoResponseDto.meta.is_end) {
					this.foodClassification(kakaoResponseDto, food); 
					break;
				} else {
					this.foodClassification(kakaoResponseDto, food); 
					page++;
				}
			}
			// 제일 많은 리뷰 순서대로 줄 세우고 round 갯수만큼 뽑아내기
			List<Document> orderByDocument = new ArrayList<>();
			if(newDocuments.size() > 0) {
				int max = 0;
				if(newDocuments.size() >= Integer.parseInt(round)) {
					max = Integer.parseInt(round);
				} else {
					max = newDocuments.size();
				}
				newDocuments = newDocuments.stream().filter(x -> x.review != null).sorted(Comparator.comparing(Document::getReview).reversed()).collect(Collectors.toList());
				for(int i = 0; i < max; i++) {
					orderByDocument.add(newDocuments.get(i));
				}
			}
			newKakaoResponseDto.setDocuments(orderByDocument);
			newKakaoResponseDto.setMeta(kakaoResponseDto.meta);
		} catch(Exception e) {
			throw e;
		}
		return new ResponseEntity<KakaoResponseDto>(newKakaoResponseDto, headers, HttpStatus.valueOf(200));
	}
	
	public void foodClassification(KakaoResponseDto kakaoResponseDto, String food) throws Exception {
		List<Document> documents = kakaoResponseDto.documents;
		try {
			for(Document document : documents) {
				String category_name = document.category_name.replace(" ", "").split(">")[1];
				
				if(category_name.equals(food)) {
					newDocuments.add(document);
					placeUrlCrawling(document);
				}
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	public void placeUrlCrawling(Document document) {
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
