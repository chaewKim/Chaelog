# Chaelog

## 1. 프로젝트 소개

Chaelog은 Spring Boot와 Vue.js를 기반으로 한 블로그 플랫폼입니다. 사용자 인증, 게시글 작성/수정/삭제, 댓글 기능 등을 제공하며 RESTful API 설계 및 테스트, 문서화를 학습하기 위해 개발되었습니다.

-개발 기간 : 2024.09.01 ~ 2024.12.11

-참여 인원 : 1명

-github : https://github.com/chaewKim/Chaelog.git

## 2. 기술 스택

### **-Backend**
- **Spring Boot 3.x**: RESTful API 설계 및 서비스 로직 구현.
    - `Spring Security`: 인증 및 권한 제어 구현.
    - `Spring Data JPA`: 데이터베이스와의 인터페이스.
    - `Spring REST Docs`: REST API 문서 자동 생성.
- **H2 Database**: 로컬 개발 환경에서 임시 데이터 저장용.
- **JWT**: 사용자 인증 토큰 관리.
- **Spring Session**: 세션 기반 Remember-Me 기능.
- **QueryDSL**: 복잡한 쿼리 작성 및 성능 최적화를 위해 사용.
  
### **-Frontend**
- **Vue.js 3**: SPA 구현 및 사용자 인터페이스 설계.
    - `Vuex`: 상태 관리.
    - `Vue Router`: 라우팅 관리.
    - `Axios`: 백엔드 API 통신.
- **TypeScript**: 타입 안정성과 코드 품질 개선을 위한 선택.
  
### **-Testing**
- **JUnit 5**: 단위 및 통합 테스트.
- **MockMvc**: 컨트롤러 테스트 및 REST Docs 연동.
- **RestDocs**: API 문서화 자동화.

### **-Development Tools**
- **Gradle**: 빌드 및 의존성 관리.
- **IntelliJ IDEA**: 주요 개발 환경.
- **Git/GitHub**: 버전 관리 및 협업.
- **PlantUML**: 시퀀스 다이어그램 생성.

## 3. 아키텍처 설계

### **1. 주요 아키텍처 개요**
Chaelog의 아키텍처는 **클라이언트-서버 모델**을 기반으로 설계되었습니다. 프론트엔드는 Vue.js로 구현되며, 백엔드는 Spring Boot로 RESTful API를 제공하고 데이터베이스와 통신합니다.

### **2. 주요 구성 요소**
- **Client (Frontend)**:
    - Vue.js: 사용자 인터페이스와 상태 관리.
    - Axios: REST API 통신.
    - Vue Router: 페이지 라우팅.
- **Server (Backend)**:
    - Spring Boot: 비즈니스 로직 및 데이터 처리.
    - Spring Security: 세션 기반 인증 처리.
    - JPA: 데이터베이스 연동.
- **Database**:
    - 개발: H2 Database (경량화된 테스트용 DB).
    - 배포: MySQL (실제 데이터 저장소). →배포 진행 시 예정
- **Deployment**:
    - 정적 리소스 (Vue.js) 호스팅: Netlify, AWS S3.
    - 백엔드 서버: AWS EC2 또는 Docker 기반 컨테이너.
    - 데이터베이스: AWS RDS 또는 MySQL 서버
 
## 4. 시퀀스 다이어그램
https://drive.google.com/file/d/1p3bUYRBMSkrH2nz-PTeBflCVXe1Cstmm/view?usp=drive_link

## 5. API
[chaelog API.pdf](https://github.com/user-attachments/files/18094554/chaelog.API.pdf)

## 6. TEST 결과 
### -ControllerDocsTest

![image (2)](https://github.com/user-attachments/assets/a489bb1f-d561-467a-a90d-6eceed06afd8)
![image (3)](https://github.com/user-attachments/assets/2594999f-9cc6-458d-a302-4c0914f79afe)
![image (4)](https://github.com/user-attachments/assets/d1c801ef-4679-48a3-b2a9-4bdf36faa590)

### -ControllerTest
![image (6)](https://github.com/user-attachments/assets/505c7a6e-0f7f-4d21-bb52-cceb270514b3)
![image (7)](https://github.com/user-attachments/assets/2bc638a4-52f9-40bd-be7b-0495e997703c)
![image (5)](https://github.com/user-attachments/assets/ba9a616b-b615-4b70-810c-3299291daf5a)

### -ServiceTest
![image (10)](https://github.com/user-attachments/assets/b10beb64-ada7-4fc5-96e2-f52e6c9ab8df)
![image (9)](https://github.com/user-attachments/assets/9d080e1b-f988-4c43-87e6-c962f252ee29)
![image (8)](https://github.com/user-attachments/assets/800f2a72-870f-4f7b-8b71-3b2d52e7cdb9)


## 7. 실행화면
  https://drive.google.com/file/d/1ruYdIxnTRlOsPxO0nu1IkXWa-SJfgvZc/view?usp=drive_link


