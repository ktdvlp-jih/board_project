# Spring Boot 게시판 API 프로젝트

## 📘 1. 프로젝트 소개
Spring Boot와 JPA를 활용한 RESTful 게시판 API입니다.  
게시글 CRUD 및 파일 첨부 기능을 제공합니다.

## 🧰 2. 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.5.3
- **ORM**: Spring Data JPA + Hibernate
- **Query**: QueryDSL
- **Database**: SQL Server
- **Build Tool**: Gradle 8.14.3

## 📁 3. 프로젝트 구조
```
src/main/java/com/rsp/platform/
├── BoardApiApplication.java          # 메인 애플리케이션 클래스
├── common/
│   ├── config/WebConfig.java         # 웹 설정
│   ├── exception/                    # 전역 예외 처리
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ApiError.java
│   │   └── NoContentException.java
│   └── util/CommonUtils.java         # 공통 유틸리티
├── config/QueryDslConfig.java        # QueryDSL 설정
└── domain/
    ├── board/                        # 게시판 도메인
    │   ├── controller/BoardController.java    # REST API 컨트롤러
    │   ├── service/BoardService.java          # 비즈니스 로직
    │   ├── repository/                        # 데이터 접근 계층
    │   │   ├── BoardRepository.java
    │   │   └── BoardQueryRepository.java
    │   ├── entity/BoardEntity.java           # JPA 엔티티
    │   └── dto/                              # 데이터 전송 객체
    │       ├── BoardRequest.java
    │       ├── BoardListResponse.java
    │       └── BoardDetailResponse.java
    └── file/                         # 파일 관리 도메인
        ├── service/FileStorageService.java    # 파일 저장 서비스
        ├── repository/                        # 파일 데이터 접근
        │   ├── AttachFileRepository.java
        │   └── LinkFileRepository.java
        ├── entity/                           # 파일 엔티티
        │   ├── AttachFileEntity.java
        │   └── LinkFileEntity.java
        └── dto/AttachFileResponse.java       # 파일 응답 DTO
```

## 4. 실행 방법 (IntelliJ 기준)

이 프로젝트는 별도의 배포 파일 없이 IntelliJ IDEA에서 직접 실행하여 테스트하였습니다.

1. **프로젝트 클론**
```bash
git clone https://github.com/ktdvlp-jih/board_project

```

2. **IntelliJ로 열기**
   - IntelliJ IDEA 실행
   - File > New > Project from version Control 선택 후 clone

3. **README2 열기**
   - mssql DB 생성

4. **의존성 다운로드**
   - Gradle: 우측 Gradle 탭에서 'Refresh' 클릭

5. **애플리케이션 실행**
   - 'BoardApiApplication.java' 파일 열기
      - 자바 17 로 설정되어 있지 않다면 단축키 사용후 Ctrl+Shift+Alt+S 자바 17로 설정
      - BoardApiApplication 실행 설정이 필요하다면 Run > Edit Configurations 이동 후 
         Name : BoardApiApplication
         Build and run 패키지 경로 com.rsp.platform.BoardApiApplication로 설정

6. **API 테스트** [rsp_project_API명세서.md 또는 rsp_project_API명세서.xlsx 확인]
   - Postman을 이용해 API 호출
      - rsp_project.postman_collection.json 파일 이용하요 import후 사용가능

---

## ✨ 5. 기능 요약
- **게시글 CRUD**: 등록, 조회, 수정, 삭제 (소프트 삭제 적용)
- **파일 첨부**: 다중 파일 업로드 및 관리 (UUID 기반 파일명)
- **동적 검색**: 제목, 내용, 날짜 범위별 검색 (QueryDSL 활용)
- **페이징 처리**: 대용량 데이터 효율적 처리
- **조회수 관리**: 게시글 조회시 자동 조회수 증가
- **전역 예외 처리**: 일관된 API 응답 구조

## 📋 6. API 명세 [명세서파일 참고]
1. rsp_project API 명세서.md
2. rsp_project API 명세서.xlsx
**Base URL**: `http://localhost:8080/api`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/boards` | 게시글 목록 조회 (검색/페이징) |
| GET | `/boards/{boardId}` | 게시글 상세 조회 |
| POST | `/boards` | 게시글 등록 (파일 첨부 지원) |
| PUT | `/boards/{boardId}` | 게시글 수정 |
| DELETE | `/boards/{boardId}` | 게시글 삭제 (소프트 삭제) |
