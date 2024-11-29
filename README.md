# 📌 6-plus README

## 1. 프로젝트 소개

- **진행 기간**: `2024-11-22` ~ `2024-11-29`
- 6-plus는 
  **선착순 응모를 진행하는 DRAW의 백엔드 개발** 프로젝트입니다.
- **동시성 제어**가 중요한 요소로, 여러 사용자가 동시에 응모를 시도할 때, **정확한 응모 순서와 제한된 인원수**를 보장하는 것이 핵심입니다.
- 주요 기능으로는 **회원 관리, DRAW 관리, 관심응모 관리, 리뷰 기능, 알림 기능** 등이 포함됩니다.
- 관리자는 **DRAW을 생성/관리**할 수 있고, 사용자는 DRAW가 열리면 **선착순으로 당첨 결과**를 알 수 있습니다.<br>
  또한, 사용자는 **관심 응모**를 관리할 수 있고, 관심 응모에 관한 **알림**을 받을 수 있습니다.

<br>

---


## 2. 팀원 구성
* 김완영 **(팀장)** https://github.com/kimwanyoung
* 권익현 (팀원) https://github.com/inzulmi132
* 김명훈 (팀원) https://github.com/anewsdev
* 김가빈 (팀원) https://github.com/gabeen-dev
* 최서영 (팀원) https://github.com/seoyeong-4811



<br>

---

## 3. 역할 분담
**동시성 제어가 핵심인 프로젝트로, 각 팀원은 선착순 응모 시스템에서 동시성 문제를 해결하기 위해 다양한 방식으로 구현했습니다.**

### 👩 **최서영**
- **회원 기능** (회원가입, 로그인, 회원정보 수정, 회원 탈퇴)
- **선착순 응모 기능** (비관적 락)
### 👨 **김명훈**
- **Draw 기능** (응모 생성, 응모 수정, 응모 삭제)
-  **선착순 응모 기능** (redis (lettuce/Spinlock))
### 👨 **권익현**
- **Draw 조회** (본인 응모 조회, 특정 응모 조회)
- **리뷰 관리** (리뷰 작성, 리뷰 전체조회, 본인 리뷰 전체조회, 리뷰 수정, 리뷰 삭제)
- **선착순 응모 기능** (메시지 큐 (RabbitMQ))
### 👩 **김가빈**
- **관심응모 관리** (관심 모 생성, 본인 관심응모 조회, 관심응모 삭제)
- **선착순 응모 기능**
### 👨 **김완영**
- **실시간 알림 기능**
- **선착순 응모 기능** (Mysql Named Lock)


<br>

---


## 4. 개발환경과 사용한 기술

###  개발 환경

- **협업 툴**: Slack, Notion
- **Back-end**: Java, Spring Boot, JPA
- **Database**: MySQL (Docker를 사용하여 데이터베이스 실행)
- **버전관리**: GitHub, GitHub Issues
- **컨테이너**: Docker


###  채택한 기술

  ### 🔍Spring Security/JWT 인증

- 사용자 인증에 **JWT(Json Web Token)** 를 적용하여 회원 로그인과 인증을 처리했습니다.

### 🔍SSE(Server Sent Event)
- 알림 시스템에 **SSE**에 을 적용하여 클라이언트에게 실시간으로 알림을 전송합니다.
### 🔍AWS S3 (Simple Storage Service)
- **AWS S3** 을 활용하여 이미지를 업로드하고 관리합니다.
### 🔍동시성제어
- 본 프로젝트에서는 다양한 기술을 사용하여 동시성 문제를 해결하고 있습니다.

  *  **Lock(비관적 락)**  
  *  **redis(lettuce/Spinlock)**
  *  **메시지 큐 (RabbitMQ)**
  *  **Mysql Named Lock**

 <br>

---

## 5. 프로젝트 설계

### 와이어프레임
![image](https://github.com/user-attachments/assets/337b69be-50cd-4e0c-9b1f-4a013a2c9f5c)

### ERD
![image](https://github.com/user-attachments/assets/b353bd62-023a-42d7-81c4-60d961d21c46)

### 포로젝트 구조
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.plus
│   │   │       ├── config        
│   │   │       │   ├── QuerydslConfiguration  # Querydsl 설정
│   │   │       │   ├── S3Config               # AWS 설정
│   │   │       └──domain                      # 주요 도메인별 패키지
│   │   │           ├── auth                   # 회원가입/로그인 관리
│   │   │           ├── common                 # exception / BaseTimestamped 관련 패키지
│   │   │           ├── darw                   # 응모 관리
│   │   │           ├── notification           # 알림 관리
│   │   │           ├── review                 # 리뷰 관리
│   │   │           ├── security               # spring security 설정
│   │   │           └── user                   # 회원 관리
│   │    resources
│   └── test
│       └── java
│           └── com.plus
│               └── test                       # 동시성 제어 테스트
└──PlusApplicationTests.java
```

