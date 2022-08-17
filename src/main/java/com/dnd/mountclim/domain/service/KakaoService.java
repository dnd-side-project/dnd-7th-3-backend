package com.dnd.mountclim.domain.service;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.dnd.mountclim.domain.dto.LocationPoint;
import com.dnd.mountclim.domain.dto.RectanglePoints;
import com.dnd.mountclim.domain.util.DeduplicationUtils;
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
	private final String WEB_DRIVER_PATH = Paths.get(AppConfig.getOsPath("dnd"), new String[]{AppConfig.getChromePath()}).toString();  // 드라이버 경로
	
	@Value("${kakao.api.key}")
	private String KAKAO_APIKEY;	
	
	KakaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;

	WebDriver driver = null;
	WebDriverWait webDriverWait = null;
	JavascriptExecutor executor = null;

	public ResponseEntity<KakaoResponseDto> kakaoApi(
		String food,
		List<RectanglePoints> listRectanglePoints,
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
			// 여기에서 감싸야함.
			for(RectanglePoints rp : listRectanglePoints) {
				for (LocationPoint lp : rp.getRectanglePoints()) {
//					System.out.println("longitude : " + lp.getLongitude() + ", latitude : " + lp.getLatitude());
					while (true) {
						headers.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
						String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
								+ "&category_group_code=" + "FD6"
								+ "&x=" + lp.getLongitude()
								+ "&y=" + lp.getLatitude()
								+ "&radius=" + radius
								+ "&page=" + page;

						ResponseEntity<KakaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KakaoResponseDto.class);
						kakaoResponseDto = responseEntity.getBody();

						if (kakaoResponseDto.meta.is_end) {
							this.foodClassification(kakaoResponseDto, food);
							break;
						} else {
							this.foodClassification(kakaoResponseDto, food);
							page++;
						}
					}
				}
			}


			// 중복제거 // 밑에서 부터는 newDocuments가 아닌 distinctNewDocuments를 사용해야해서 바꿔놨습니다.
			List<Document> distinctNewDocuments = DeduplicationUtils.deduplication(newDocuments, Document::getId);


			// 제일 많은 리뷰 순서대로 줄 세우고 round 갯수만큼 뽑아내기
			List<Document> orderByDocument = new ArrayList<>();
			if(distinctNewDocuments.size() > 0) {
				distinctNewDocuments = distinctNewDocuments.stream().filter(x -> x.review != null).sorted(Comparator.comparing(Document::getReview).reversed()).collect(Collectors.toList());
				int max = 0;
				if(distinctNewDocuments.size() >= Integer.parseInt(round)) {
					max = Integer.parseInt(round);
				} else {
					max = distinctNewDocuments.size();
				}
				for(int i = 0; i < max; i++) {
					orderByDocument.add(distinctNewDocuments.get(i));
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

				if(food.equals("국밥,감자탕")){
					if(document.category_name.contains("국밥") || document.category_name.contains("감자탕")){
							newDocuments.add(document);
					}
				}
				else if(food.equals("바")){
					if(document.category_name.contains("와인바") || document.category_name.contains("오뎅바")
							|| document.category_name.contains("칵테일바")){
						newDocuments.add(document);
					}
				}
				else if(food.equals("기타")){
					if(document.category_name.contains("실내포장마차") || document.category_name.contains("일본식주점")){
						newDocuments.add(document);
					}
				}
				else if(document.category_name.contains(food)){
					newDocuments.add(document);
				}
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	public void placeUrlCrawling(Document document) {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("window-size=1920x1080");
		options.addArguments("disable-gpu");
		options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		options.addArguments("lang=ko_KR");
		driver = new ChromeDriver(options);
		executor = (JavascriptExecutor) driver;
		webDriverWait = new WebDriverWait(driver, 10);
		
		driver.get(document.place_url);
		WebElement kakaoWrap = driver.findElement(By.id("kakaoWrap"));
//		executor.executeScript("arguments[0].click();", kakaoWrap);	
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
		driver.close();
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
