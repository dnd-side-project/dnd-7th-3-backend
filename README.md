# dnd-7th-3-backend

## 1. 💻 Developer

<div align="center">
<table>
  <tr>
      <td align="center"><a href="https://github.com/anjeongkyun"><img src="./images/anjeongkyun.jpeg" width="150x;" alt=""/><br /><p><b>안정균</b></p></a><small>🍀 Back-End Developer</small></td>
    <td align="center"><a href="https://github.com/haeyonghahn"><img src="./images/haeyonghahn.jpeg" width="150x;" alt=""/><br /><p><b>한해용</b></p></a><small>🍀 Back-End Developer</small></td>
    <td align="center"><a href="https://github.com/Amenable-C"><img src="./images/Amenable-C.jpeg" width="150px;" alt=""/><br /><p><b>최연재</b></p></a><small>🍀 Back-End Developer</small></td>
  </tr> 
</table>
</div>
<br>

## 2. 서비스 소개
#### "우리 이제 어디 가지?...😨"
1차 장소에서 즐거운 시간을 보낸 후, 2차 장소 선정에 어려움을 겪으신 경험이 있으신가요?<br>
이러한 문제를 해결하고자 <b>2차 장소를 쉽게 재미있게 선정하는 서비스</b>를 기획하였습니다.<br>

#### 　　　　　　　　　<흐름도>
<img src="https://user-images.githubusercontent.com/61836238/193735388-0a8e96a9-bf5f-499f-9676-0b643cdbc93d.PNG" width="400" height="400" align='center'>

## 3. 화면
<div>
<img src="https://user-images.githubusercontent.com/61836238/193739452-03e74dba-8ca8-4171-8368-e23c4189fc2e.png" width="225" height="487" align='center'>
<img src="https://user-images.githubusercontent.com/61836238/193739494-db8dc2ad-4e85-47df-a87f-9917ceb63a94.png" width="225" height="487" align='center'>
<img src="https://user-images.githubusercontent.com/61836238/193739577-ff0f5dfb-a3be-45e7-ae0c-933b18d1d2be.png" width="225" height="487" align='center'><br><br>
<img src="https://user-images.githubusercontent.com/61836238/193739592-8ce13507-aabe-4d4e-bbb2-a2ce955c0161.png" width="225" height="487" align='center'>
<img src="https://user-images.githubusercontent.com/61836238/193739619-9ca501d0-26ae-4078-8880-acaf3a9dd861.png" width="225" height="487" align='center'>
<img src="https://user-images.githubusercontent.com/61836238/193739643-c0fbb8df-68a4-411b-9611-7776172245b7.png" width="225" height="487" align='center'><br><br>
<img src="https://user-images.githubusercontent.com/61836238/193739659-67dfed68-b7af-4f78-a23c-e817d315a70b.png" width="225" height="487" align='center'>
<img src="https://user-images.githubusercontent.com/61836238/193740418-1a9c211f-9cc5-44da-b5bc-dfb0fa4970b0.png" width="225" height="487" align='center'><br><br>
</div>

## 4. Architecture
![architecture](https://user-images.githubusercontent.com/61836238/193740893-bb7b50d0-56e9-49bf-888b-40cc11e81555.png)
<br>

## 5. 개발 환경
* Java 8
* Maven
* Spring Boot (v2.5.6)
* Selenium
* Querydsl
* JUnit
* Github actions
* AWS EC2
* AWS S3
* AWS CodeDeploy
<br><br>

## 6. 사용한 Open API 정보
<b>1. [Kakao Open API - 카테고리로 장소 검색하기](https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category)</b><br>
<b>2. [Naver Open API - 블로그 검색](https://developers.naver.com/docs/serviceapi/search/blog/blog.md)</b><br>
<b>3. [Naver Open API - 통합 검색어 트랜드](https://developers.naver.com/docs/serviceapi/datalab/search/search.md)</b><br>
<br>

## 7. 참고 사항
#### 1. 지정된 구역에서 16개의 포인트를 기준으로 API를 요청하는 이유
&nbsp;구역의 크기에 상관없이 한번 API를 요청할 때 최대 45개의 문서(음식점의 정보)를 가지고 올 수 있다. 이를 고려하여 많이 요청하여 많은 문서(식당)를 얻고자 해당 구역의 16개의 지점(파란점)에서 API를 호출하였다. <b>(45개(기존) vs 720(=16 x 45)(변경후))</b><br>
<img src="https://user-images.githubusercontent.com/61836238/193741017-6bcf19f0-83de-4243-8fd8-baf4942ddbd3.PNG" width="100" height="100" align='center'>
#### 2. 라운드에 맞는 식당 선정 기준
&nbsp;검색된 음식점들 중에서 사용자가 진행할 월드컵 게임에 들어갈 음식점은 '검색량'과 '거리'를 가지고 선정하였다. 검색량과 거리의 가중치는 7:3으로 선정한 후 계산을 하였다.
#### 3. 주간 인기 검색순
&nbsp;월드컵 게임에 있는 음식점들을 '주간 인기 검색순'으로 확인 할 수 있다. 이는 네이버 일주일 검색량의 평균치를 통하여 순위를 매긴 후 서비스를 제공하였다. 
