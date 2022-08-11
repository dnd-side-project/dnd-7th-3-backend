package com.dnd.mountclim.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dnd.mountclim.domain.dto.KaKaoResponseDto;
import com.dnd.mountclim.domain.dto.KaKaoResponseDto.Document;

@Service
public class HaeyongService {

	private final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
	private final String WEB_DRIVER_PATH = "D:\\work\\chromedriver.exe"; // 드라이버 경로
	private final String KAKAO_APIKEY = "66ae58f6853d9564d14b71f00d1bb360";	
	
	KaKaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;
	
	WebDriver driver = null;
	JavascriptExecutor executor = null;
	
	@PostConstruct
	private void init() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		driver = new ChromeDriver(options);
		executor = (JavascriptExecutor) driver;
	}
	
	public ResponseEntity<KaKaoResponseDto> kakaoApi(
		String food,
		String latitude,
		String longitude,
		String radius) throws Exception {
		RestTemplate restTemplate = new RestTemplate();			
		HttpHeaders headers = new HttpHeaders();
		KaKaoResponseDto kakaoResponseDto = null;
		newKakaoResponseDto = new KaKaoResponseDto();
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
				
				ResponseEntity<KaKaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KaKaoResponseDto.class);
				kakaoResponseDto = responseEntity.getBody();	
				
				if(kakaoResponseDto.meta.is_end) {
					this.foodClassification(kakaoResponseDto, food); 
					break;
				} else {
					this.foodClassification(kakaoResponseDto, food); 
					page++;
				}
			}
			newKakaoResponseDto.setDocuments(newDocuments);
			newKakaoResponseDto.setMeta(kakaoResponseDto.meta);
		} catch(Exception e) {
			throw e;
		}
		return new ResponseEntity<KaKaoResponseDto>(newKakaoResponseDto, headers, HttpStatus.valueOf(200));
	}
	
	public void foodClassification(KaKaoResponseDto kakaoResponseDto, String food) throws Exception {
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
		String dom = kakaoWrap.getAttribute("innerHTML");
		System.out.println(dom);
		
		List<WebElement> linkEvaluations = kakaoWrap.findElements(By.className("link_evaluation"));
		String discuss = linkEvaluations.get(0).getAttribute("data-cnt");
		String review = linkEvaluations.get(1).getAttribute("data-cnt");
		document.setDiscuss(discuss);
		document.setReview(review);
		
		List<WebElement> menuInfos = kakaoWrap.findElements(By.className("info_menu"));
		for(WebElement menuInfo : menuInfos) {
			String menuName = menuInfo.findElement(By.className("loss_word")).getText();
			String menuPrice = menuInfo.findElement(By.className("price_menu")).getText();
			System.out.println(menuName);
			System.out.println(menuPrice);
		}
	}
}
