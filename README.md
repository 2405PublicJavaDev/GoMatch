
![고매치로고 ](https://github.com/user-attachments/assets/8e4534a1-5f04-43d4-b8fe-359a02560608)
<h1>GoMatch
  (야구 직관 소모임 사이트)</h1>


<p>&nbsp;&nbsp;&nbsp;&nbsp;</p> 

<h1>프로젝트 목적</h1>
<h3>기획 의도 : </h3>  국내에서 야구에 대한 관심이 높아지지만 직관을 전문으로 하는 사이트는 부족한 상황,
직관은 가고 싶은데 사람 모으기가 어려울 때 마음 맞는 사람들끼리 가는 사이트를 제공하기 위함

<p>&nbsp;&nbsp;&nbsp;&nbsp;</p>
<h1>개발 기간</h1>
<h4>2024.09.19 ~ 2024.10.23</h4>
<p>&nbsp;&nbsp;&nbsp;&nbsp;</p>

# 개발 환경

<div style="display: flex; flex-wrap: wrap; gap: 5px;">
  <!-- Languages -->
  <img src="https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  
  <!-- Framework & Library -->
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Security-00A591?style=for-the-badge&logo=spring-security&logoColor=white"> 
  <img src="https://img.shields.io/badge/Spring_Batch-4CAF50?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Jsoup-43853D?style=for-the-badge&logo=jsoup&logoColor=white">
  <img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=websocket&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
  <img src="https://img.shields.io/badge/FullCalendar-4285F4?style=for-the-badge&logo=google-calendar&logoColor=white"> 
  <img src="https://img.shields.io/badge/Toast_UI_Editor-181717?style=for-the-badge&logo=tui&logoColor=white">
  <img src="https://img.shields.io/badge/SweetAlert2-3085D6?style=for-the-badge&logo=SweetAlert&logoColor=white"> 
  
  <!-- Payment & Map Services -->
  <img src="https://img.shields.io/badge/Kakao_Login-FFCD00?style=for-the-badge&logo=kakao&logoColor=black">
  <img src="https://img.shields.io/badge/Inicis_Payment-003F87?style=for-the-badge&logo=payment&logoColor=white">
  
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

# 팀 소개 및 팀원 소개

## 모이조
![image](https://github.com/user-attachments/assets/f7e11bc0-065f-49b6-a9d6-e6fe19a57bd9)


<h1>주요 기능</h1>
<h2>회원</h2>

[회원 가입] <br>
사용자들은 웹사이트에 아이디, 비밀번호, 이름, 닉네임, 생년월일, 이메일을 입력 후 가입하며 자신의 계정을 생성합니다. <br>
아이디, 닉네임은 중복 체크를 해야 합니다. 이메일은 중복체크되어야 하며 인증이 되어야 합니다. 회원가입 폼에는 일정한 틀을 지켜야 하며 만약 지키지 않을 시 오류가 납니다. 

[로그인] <br>
사용자는 직접 웹사이트에 가입한 아이디 로그인과 카카오 간편 로그인 두가지로 할 수 있습니다 <br>
	로그인 완료 시 로그인 한 사람의 닉네임이 노출 됩니다 <br>
	회원은 언제든지 로그아웃을 할 수 있습니다.

[아이디 및 비밀번호 찾기] <br>
사용자는 아이디를 잊어버렸을 경우 가입한 이름과 생년월일을 입력하여 아이디를 찾을 수 있습니다.<br>
비밀번호를 잊었을 경우 회원가입시 등록한 이메일로 임시 비밀번호가 발급되며 임시 비밀번호로 해당 회원의 비밀번호가 재설정됩니다.

[마이페이지] <br>
회원은 마이페이지에서굿즈 구매 내역 확인할 수 있습니다. 가입한 소모임 리스트도 확인할 수 있습니다.<br>
내가 예상한 승부 예측도 확인할 수 있습니다. 회원 정보 수정과 회원 탈퇴도 가능합니다 회원 탈퇴를 할때는 경고문이 나오고 확인을 누르면 회원 탈퇴가 완료됩니다 

[회원 관리 - 관리자] <br>
관리자는 회원을 비활성화 , 삭제 시킬 수 있습니다 회원 비활성화가 된 아이디는 로그인이 불가능 하고 관리자가 비활성화를 해제 해야 아이디 사용이 가능 합니다 





# 구현 화면
## 메인페이지
![mainpage](https://github.com/user-attachments/assets/3461b633-377d-4090-b52a-f93e4543c1f4)

<br><br><br><br><br>

## 회원 
![loginpage](https://github.com/user-attachments/assets/1f46ff92-75e8-475e-8f25-60ba7384ac50)

![findidpage](https://github.com/user-attachments/assets/f2ea6400-1586-47d2-8055-dbff1d652ecb)

![findpwpage](https://github.com/user-attachments/assets/c8b68858-ebb6-437e-9666-03818fa2abc3)

![joinpage](https://github.com/user-attachments/assets/10450ecb-b568-430d-a64b-80d3be9bdb17)

![mypage](https://github.com/user-attachments/assets/da7c8fc0-a560-4242-a8ac-98a3a391a9d8)

![modifypage](https://github.com/user-attachments/assets/3b835457-0b97-4262-b976-3d50ba86b723)

![deletepage](https://github.com/user-attachments/assets/d908e41f-8d45-49d1-8678-c21ccf08f399)

<br><br><br><br><br>

## 메인페이지 - 관리자
![adminmainpage](https://github.com/user-attachments/assets/36deb6e9-8f4a-486a-83aa-43a1336b9c7f)

## 회원 관리 - 관리자
![adminmemberpage](https://github.com/user-attachments/assets/19366d68-0f15-4d84-bb96-b5bc1af02322)


