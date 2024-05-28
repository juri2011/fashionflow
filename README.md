🛫 스프링 부트 의류 중고 거래 팀 프로젝트
==
<br/>



<br/>
<br/>

<!-- ABOUT THE PROJECT -->
## :clock12: 소개
<br/>
<br/>


C2C를 기반으로 진행되는 상거래 사이트

<br/>

API를 이용한 기능 구현

<br/>

AWS를 이용한 웹 서버 구축


<br/>
<br/>
<br/>
<br/>

### i. 일정

![image](https://github.com/kty1210/fashionflow/assets/154123644/95ccece8-9d88-44e0-9d73-b5ba967a9304)


### ii. 요구사항 명세서

![image](https://github.com/kty1210/fashionflow/assets/154123644/61048b17-0697-490b-a02e-148afba5dabc)

![image](https://github.com/kty1210/fashionflow/assets/154123644/c82d3d2e-8fe5-41b0-b36f-e04431284d65)



<br/>
<br/>

<!-- 기술스택 -->
## :clock1: 주요 기술스택





* <img src="https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white">
* <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
* <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
* <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
* <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jqueryt&logoColor=white">
* <img src="https://img.shields.io/badge/jpa-E53525?style=for-the-badge&logo=jpa&logoColor=white">
* <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
* <img src="https://img.shields.io/badge/openjdk-000000?style=for-the-badge&logo=openjdk&logoColor=white">
* <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
* <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
* <img src="https://img.shields.io/badge/gitkraken-179287?style=for-the-badge&logo=gitkraken&logoColor=white">


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>


<br/>
<br/>
<br/>



## :clock2: 주요 라이브러리
<br/>

* Spring Security
* Jackson
* validation
* OAuth
* Websocket
* mail
* thymeleaf 

<br/>
<br/>




<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>


## :clock3: 클래스 다이어그램


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

## :clock4: 엔티티 관계도

![fashiondb](https://github.com/kty1210/fashionflow/assets/154123644/23249664-266c-4b6b-bfd2-b028637b9274)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

## :clock5: 기능 시연

<br/>

<br/> ✦ 메인 페이지 <br/>
<br/>
<br/>
<br/>
<br/>

<br/> ✦ 조건 검색 <br/>
<br/>
<br/>
<br/>
<br/>

<br/> ✦ 페이지네이션 <br/>
<br/>
<br/>
<br/>
<br/>
 

<br/> ✦ 업로드 <br/>

<br/>
<br/>
<br/>
<br/>



<br/> ✦ 상세 페이지 <br/>


<br/>
<br/>
<br/>
<br/>

<br/> ✦ 글 수정 <br/>

<br/>
<br/>
<br/>
<br/>


<br/> ✦ 댓글 <br/>

<br/>
<br/>
<br/>
<br/>




<br/>
<br/>


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>



## :clock6: 이슈

### i.문제

<br/>
<br/>


![image](https://github.com/kty1210/fashionflow/assets/154123644/9416b8ae-2248-44f7-b1eb-13176001e981)

<br/>


UserDetailsService의 loadUserByUsername메소드를 이용하여 사용자 인증 필터 오버라이딩을 정상적으로했음에도 <br/>
BLACK 등급의 회원 차단시 기본 로그인 실패 메세지 출력

<br/>
<br/>
<br/>


### ii.원인

<br/>
<br/>


![image](https://github.com/kty1210/fashionflow/assets/154123644/d9053305-161a-4ca8-b37d-3e492cab00b7)

<br/>


AuthenticationException를 상속받는 예외가 발생하면, 스프링 시큐리티는 이를 InternalAuthenticationServiceException 자동적으로 한번 더 래핑해서 처리

<br/>
<br/>
<br/>


### iii.해결방법

<br/>
<br/>

![image](https://github.com/kty1210/fashionflow/assets/154123644/07830bf9-8ca3-4ac6-9df2-82008740adeb)

<br/>

InternalAuthenticationServiceException의 원인이 되는 예외로 특정시켜 메세지가 출력

<br/>
<br/>
<br/>
<br/>



## 🛬 마치며...

개선할 점

* 


긍정적인 측면

* API 사용법 
* AWS의 EC2를 이용한 서버 구축과 RDS를 이용한 관계형데이터베이스 구축 
