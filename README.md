# EduBridge

TECH!T back-end 7기 1차 프로젝트 : 교육 영상 플랫폼 EduBridge

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
<img src="https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white">
<img src="https://img.shields.io/badge/tailwind css-06B6D4?style=for-the-badge&logo=Tailwind Css&logoColor=white">
<img src="https://img.shields.io/badge/svelte-FF3E00?style=for-the-badge&logo=Svelte&logoColor=white">
<br>
<img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/fly.io-B366F6?style=for-the-badge&logo=fly.io&logoColor=white">
<img src="https://img.shields.io/badge/vercel-000000?style=for-the-badge&logo=vercel&logoColor=white">
<br>
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<br> <br>
 
---
![main page](https://i.imgur.com/mEaj1Ow.png)

---

<br><br>

## 1. ERD

![ERD](https://i.imgur.com/OQrklp5.png)

<br><br>

## 2. 시스템 구조도

![구조도](https://i.imgur.com/wAqNkc7.png)

<br><br>

## 3. 프로덕트 세부 (주요 페이지 소개)

### 수강 신청 ~ 요약노트 작성

![GIFMaker_one](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/fb689680-aa5e-4229-ab1b-235d81075432)

### 강좌 등록 ~ 로드맵 등록

![GIFMaker_two](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/452c65d1-1854-4657-917a-19a1ef0ede18)

### 요약노트 내보내기

![GIFMaker_three](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/84341c50-7784-4abe-8e67-b2059b107218)

<br><br>
 
## 4. 실행 방법

1. 사이트 접속 (도메인 사용 기간 이후에는 접근 불가) <br>
: https://www.eb.kys.blue/

2. IntelliJ IDE + Visual Studio Code (윈도우 기준) <br>
    
    1) **git clone** 
    ```bash
    git clone https://github.com/Teckit7-FirstBridge/EduBridge.git
    ```
    
    2) **IntelliJ IDE - 백엔드 서버** <br>
    ```bash
    # RUN 실행 버튼 눌러 백엔드 서버 먼저 실행
    code front  # VScode로 front 폴더 열기
    ```
    
    3) **Visual Studio Code - 프론트엔드 서버**
    ```bash
    npm run dev
    ```
      - npm이 설치되어 있지 않은 경우
        + Node.js 설치 <br>
           https://nodejs.org/en/download/current
        + IntelliJ 또는 기본 터미널에서 다음 실행
          ```bash
          npm config set legacy-peer-deps true
          npm install -D svelte
          npm create svelte@latest front
          code front  # VScode로 front 폴더 열기
          ```
   
        + VScode 터미널에서 다음 실행
          ```bash
          npm install
          npm run dev # Ctrl + C 로 종료 가능
          ```
    4) **각 서버 URL 접근**
     - back : `localhost:8090/h2-console`
     - front : `localhost:5173` <br><br>

    5) **로컬 DB 데이터 추가 및 테스트**
     - 로컬 서버 관리자 계정 : admin / 1234
     
          
