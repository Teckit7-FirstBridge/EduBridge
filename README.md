# EduBridge

TECH!T back-end 7기 1차 프로젝트 : 교육 영상 플랫폼 EduBridge

<br> <br>
 
---
![image](https://github.com/harriet221/EduBridge/assets/101785754/f218907f-6bfb-4e4f-8797-9bfd0a396475)
---

<br><br>

## 1. ERD

![ERD](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/f24b8a46-1414-44f8-9a41-58b8cafe4976)

<br><br>

## 2. 시스템 구조도

![구조도](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/0b11016a-3565-4be9-8aca-4fac1e6976a9)

<br><br>

## 3. 프로덕트 세부 (주요 페이지 소개)

### 1) 메인 홈 & 소셜 로그인 (카카오 / 구글)
![프로덕트1](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/4d43bfae-21f9-42ba-b8ea-1a0b28cd2f81)
---
<br>

### 2) 마이 페이지
![프로덕트2](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/f5985124-271e-4412-811c-f422fe76fd48)
---
<br>

### 3) 강좌 페이지
![프로덕트3](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/82d64b97-2512-4f6e-a911-06454393c149)
---
<br>

### 4) QnA 게시판
![프로덕트4](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/f962e648-31be-48c1-8129-ec666b93d377)
---
<br>

### 5) 관리자 페이지
![프로덕트5](https://github.com/Teckit7-FirstBridge/EduBridge/assets/101785754/334474cb-e538-44da-83da-21ddc260405f)


<br><br>
 
## 4. 실행 방법

1. 사이트 접속 (도메인 사용 기간 이후에는 접근 불가) <br>
: https://www.eb.kys.blue/

2. IntelliJ IDEA + Visual Studio Code (윈도우 기준) <br>
    
    1) **git clone** 
    ```bash
    git clone https://github.com/Teckit7-FirstBridge/EduBridge.git
    ```
    
    2) **IntelliJ IDEA - 백엔드 서버** <br>
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
     
          
