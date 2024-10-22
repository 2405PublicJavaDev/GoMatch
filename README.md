<div align="center">
  <img src="https://github.com/user-attachments/assets/8e4534a1-5f04-43d4-b8fe-359a02560608" alt="고매치 로고">
  
  # GoMatch
  ### 야구 직관 소모임 사이트
</div>

## 📋 프로젝트 소개
야구의 인기가 높아지는 가운데, 직관을 함께할 동행을 찾기 어려운 야구 팬들을 위한 소모임 플랫폼입니다. 직관을 가고 싶지만 함께 갈 사람을 구하기 어려운 사람들이 마음 맞는 사람들과 함께 즐길 수 있도록 돕는 서비스를 제공합니다.

## 🗓 개발 기간
**2024.09.19 ~ 2024.10.23**

## 🛠 개발 환경
<div style="display: flex; flex-wrap: wrap; gap: 5px;">
  <!-- Language -->
<img src="https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<!-- Framework & Library -->
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
<img src="https://img.shields.io/badge/Jsoup-43853D?style=for-the-badge&logo=jsoup&logoColor=white">
<img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=websocket&logoColor=white"> <br>
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/FullCalendar-4285F4?style=for-the-badge&logo=google-calendar&logoColor=white">
<img src="https://img.shields.io/badge/Toast_UI_Editor-181717?style=for-the-badge&logo=tui&logoColor=white">
<img src="https://img.shields.io/badge/SweetAlert2-3085D6?style=for-the-badge&logo=SweetAlert&logoColor=white">
<img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white"> <br>
<!-- Payment & Map Services -->
<img src="https://img.shields.io/badge/Kakao_Login-FFCD00?style=for-the-badge&logo=kakao&logoColor=black">
<img src="https://img.shields.io/badge/Inicis_Payment-003F87?style=for-the-badge&logo=payment&logoColor=white">
<img src="https://img.shields.io/badge/Naver_Map_API-03C75A?style=for-the-badge&logo=naver&logoColor=white">
<!-- API -->
<img src="https://img.shields.io/badge/Open_API-008080?style=for-the-badge&logo=openapiinitiative&logoColor=white"> 
<img src="https://img.shields.io/badge/Open_Weather_API-1E90FF?style=for-the-badge&logo=openweather&logoColor=white"> <br>
<!-- WAS -->
<img src="https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black">
<!-- DB -->
<img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
<!-- IDE -->
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white">
<!-- Version Control & Collaboration -->
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/Google_Drive-4285F4?style=for-the-badge&logo=google-drive&logoColor=white">
</div>

## 👥 팀 소개
### 모이조
<img src="https://github.com/user-attachments/assets/f7e11bc0-065f-49b6-a9d6-e6fe19a57bd9" alt="팀 소개">

## 💡 주요 기능

### 1. 회원 관리
#### 회원가입
- 필수 정보: 아이디, 비밀번호, 이름, 닉네임, 생년월일, 이메일
- 아이디/닉네임 중복 체크
- 이메일 인증 시스템
- 유효성 검사를 통한 회원가입 폼 검증

#### 로그인
- 일반 로그인
- 카카오 소셜 로그인
- 로그인 상태에서 사용자 닉네임 표시
- 로그아웃 기능

#### 계정 찾기
- 아이디 찾기: 이름과 생년월일로 조회
- 비밀번호 찾기: 등록된 이메일로 임시 비밀번호 발급

#### 마이페이지
- 굿즈 구매 내역 조회
- 가입한 소모임 리스트 확인
- 승부 예측 기록 확인
- 회원 정보 수정
- 회원 탈퇴 (경고 메시지 확인 후 진행)

#### 관리자 기능
- 회원 계정 비활성화/활성화 관리
- 회원 계정 삭제
- 회원 목록 관리

### 2. 경기
#### 경기일정
- **Jsoup, Selenium** 통해 크롤링 하여 연도별 KBO 경기 일정/ 결과 확인
- 경기일정을 리스트 혹은 달력으로 확인 가능
- 사용자가 선호하는 팀의 경기는 하이라이트 칠하여 구분
- **Scheduled 어노테이션**을 통해 매일 9:30에 경기 정보 DB에 업데이트

#### 팀 순위
- Jsoup 통해 크롤링 하여 연도별 KBO 리그 순위 확인
- 순위, 팀명, 경기수, 승, 패 , 무, 승률, 연승, 최근 10경기 등 해당 팀의 정보 확인

#### 선수 순위
- Jsoup, Selenium 통해 크롤링 하여 연도별 KBO 선수 순위 확인
- 투수 혹은 타자를 선택하여 해당 타입의 선수 순위 확인 가능

#### 야구장 정보
- 네이버 지도 API를 이용하여 야구장의 위치 확인
- OPEN WEATHER API 를 이용하여 **해당 야구장의 현재 날씨 정보(온도/상태/강수량/습도/풍속)** 확인
- 공공데이터 이용하여 미세먼지 정보 확인
- 해당 야구장의 시간대별 예보 날씨 정보 확인
- 해당 야구장의 주간대별 예보 날씨 정보 확인

### 5. 채팅
#### 채팅 조회
- 채팅방의 생성시간을 기준으로 내림차순으로 출력
- 해당 소모임 별 채팅 리스트들을 출력하게 함

#### 채팅방 생성
- **WebSocket** 이용하여 채팅방 생성
- 채팅방 이름을 입력하여 채팅방 생성
- 채팅방 생성하면 바로 입장 가능

#### 채팅하기
- 소모임 가입 시 해당 소모임에서 채팅 가능
- 1:다수로 여러 사람들과 채팅 가능
- 채팅방 입장 혹은 퇴장 시 안내 메시지로 확인 가능
- 닉네임과 프로필 사진으로 대화상대 구성

## 📱 구현 화면

### 메인페이지
![메인페이지](https://github.com/user-attachments/assets/3461b633-377d-4090-b52a-f93e4543c1f4)

### 회원 관련 페이지
<details>
<summary>회원 관리 페이지 보기</summary>

- **로그인**
  ![로그인](https://github.com/user-attachments/assets/1f46ff92-75e8-475e-8f25-60ba7384ac50)

- **아이디 찾기**
  ![아이디찾기](https://github.com/user-attachments/assets/f2ea6400-1586-47d2-8055-dbff1d652ecb)

- **비밀번호 찾기**
  ![비밀번호찾기](https://github.com/user-attachments/assets/c8b68858-ebb6-437e-9666-03818fa2abc3)

- **회원가입**
  ![회원가입](https://github.com/user-attachments/assets/10450ecb-b568-430d-a64b-80d3be9bdb17)

- **마이페이지**
  ![마이페이지](https://github.com/user-attachments/assets/da7c8fc0-a560-4242-a8ac-98a3a391a9d8)

- **회원정보 수정**
  ![회원정보수정](https://github.com/user-attachments/assets/3b835457-0b97-4262-b976-3d50ba86b723)

- **회원 탈퇴**
  ![회원탈퇴](https://github.com/user-attachments/assets/d908e41f-8d45-49d1-8678-c21ccf08f399)
</details>

### 관리자 페이지
<details>
<summary>관리자 페이지 보기</summary>

- **관리자 메인**
  ![관리자메인](https://github.com/user-attachments/assets/36deb6e9-8f4a-486a-83aa-43a1336b9c7f)

- **회원 관리**
  ![회원관리](https://github.com/user-attachments/assets/19366d68-0f15-4d84-bb96-b5bc1af02322)
</details>

### 경기 관련 페이지
<details>
<summary>경기 페이지 보기</summary>

- **경기 일정 - 리스트**
  
  ![경기 일정 - 리스트 ](https://github.com/user-attachments/assets/8480d302-db9d-4cb7-b8ee-9433c8ab43cd)

- **경기 일정 - 달력**
  
  ![경기 일정 - 달력](https://github.com/user-attachments/assets/6eba1a32-509c-470d-bfd2-68f3708a5abd)

- **팀 순위**
  
  ![팀 순위](https://github.com/user-attachments/assets/f10ff7e0-be58-4281-8254-f265d74032ad)

- **선수 순위**
  
  ![선수 순위](https://github.com/user-attachments/assets/5ff701e6-513b-48b6-ac87-f27e108a9592)

- **야구장 날씨**
  
  ![야구장 날씨](https://github.com/user-attachments/assets/e82649af-ee4b-4784-ab79-0af43153be11)
</details>

### 채팅 페이지
<details>
<summary>채팅 페이지 보기</summary>

- **채팅방 조회**
  
  ![채팅방 조회](https://github.com/user-attachments/assets/fddb79d3-8aab-4a34-9f64-e5a8b5e79811)

- **채팅방 생성**
  
  ![채팅방 생성](https://github.com/user-attachments/assets/694a9739-670f-4de8-a841-d7bdc034429c)
  
- **채팅하기**
  
  ![채팅하기](https://github.com/user-attachments/assets/502a9330-ec71-4edf-8e01-b3de0b35e160)
</details>
