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
https://massive-frill-1a6.notion.site/1590c00226148048ac51dcf6df193ff1?pvs=4

## 5. API
[chaelog API.pdf](https://github.com/user-attachments/files/18094554/chaelog.API.pdf)

## 6. TEST 결과 
### -ControllerDocsTest

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/3004637a-33b7-4bd0-8982-09e65d706a61/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/193586e5-6fc0-4c99-9343-f8363279dbfc/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/233a1405-9369-4e05-8276-eba7ed172623/image.png)

### -ControllerTest

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/0213fb56-d13b-47a3-9251-d29ac9203938/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/5306bcb4-db31-4784-8ef7-6176e852a9a8/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/830b24f2-d4a3-4c07-8ce4-a372bfdbd3ce/image.png)

### -ServiceTest

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/0794b01b-2340-4ead-92e2-80c37fe8c436/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/bd84df19-5962-44c6-9f2a-00ce569a8d6e/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9cfc9120-10f6-4563-b5d5-af85fd4194f4/2dafea69-1882-425d-b21d-5b37a51434a8/image.png)

## 7. 실행화면
  https://massive-frill-1a6.notion.site/1590c0022614805d9976c42191b4b752?pvs=4

## 8. 앞으로의 계획
- **검색 및 필터링**: 게시글을 제목, 카테고리, 태그, 작성자 등으로 검색하거나 필터링하는 기능을 추가.
- **파일 업로드**: 게시글이나 댓글에 이미지나 파일을 첨부할 수 있는 기능 구현.
- **관리자 대시보드**: 관리자 권한으로 사용자, 게시글, 댓글을 관리할 수 있는 간단한 관리 페이지.
- **좋아요/싫어요 또는 반응 시스템**: 게시글에 대한 사용자 반응(좋아요, 싫어요, 이모지 등)을 추가.
- **실시간 업데이트**: 댓글이나 게시글 목록이 실시간으로 갱신되는 기능(WebSocket 또는 폴링 사용).
- **배포**: AWS EC2, Nginx
  
