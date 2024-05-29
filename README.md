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

## :clock1: 일정

<br/>
<br/>

![image](https://github.com/kty1210/fashionflow/assets/154123644/95ccece8-9d88-44e0-9d73-b5ba967a9304)

<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

## :clock2: 요구사항 명세서

<br/>
<br/>

![image](https://github.com/kty1210/fashionflow/assets/154123644/61048b17-0697-490b-a02e-148afba5dabc)

<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>


## :clock3: 시퀀스 다이어그램

<br/>
<br/>


![image](https://github.com/kty1210/fashionflow/assets/154123644/c82d3d2e-8fe5-41b0-b36f-e04431284d65)


<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>


## :clock4: 와이어 프레임

<br/>
<br/>

![image](https://github.com/kty1210/fashionflow/assets/154123644/6865a75b-43a4-43ef-89fb-c5fb3116197d)

<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

<!-- 기술스택 -->
## :clock5: 주요 기술스택

<br/>
<br/>

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



<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>


<br/>
<br/>
<br/>



## :clock6: 주요 라이브러리

<br/>
<br/>

* Spring Security
* Jackson
* validation
* OAuth
* Websocket
* mail
* thymeleaf
* google chart

<br/>
<br/>



<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>


## :clock7: 클래스 다이어그램


<br/>
<br/>

![Heart](https://github.com/kty1210/fashionflow/assets/154123644/06b0e53f-5f01-4faf-bd95-4dd9f97c421b)


<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

## :clock8: 엔티티 관계도

![fashiondb](https://github.com/kty1210/fashionflow/assets/154123644/23249664-266c-4b6b-bfd2-b028637b9274)


<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>

## :clock9: 기능 시연

<br/>

<br/> 

* 담당부분

[![Video Label](http://img.youtube.com/vi/yRVWUnFJsqE/0.jpg)](https://youtu.be/yRVWUnFJsqE)

https://youtu.be/yRVWUnFJsqE?si=EqvX258-7EbXQsi7
<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>



## :clock10: 이슈

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

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<br/>
<br/>
<br/>
<br/>



## 🛬 마치며...


<br/>
<br/>

생각해봐야할 점

<br/>

* 사이트가 볼륨이 커지게 될때 서버 구축시 데이터 처리를 어떻게 해야되는가?
* Git merge시 공통 부분 처리하게 될때 좀 더 효과적인 merge방법은 어떤것이 있는가?

<br/>
<br/>

긍정적인 측면


<br/>

* API 사용법 
* AWS의 EC2를 이용한 서버 구축과 RDS를 이용한 관계형데이터베이스 구축
* 팀 단위 프로젝트를 통해 코드 이해도 향상과 팀원과의 귀중한 커뮤니케이션 경험



<br/>
<br/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

