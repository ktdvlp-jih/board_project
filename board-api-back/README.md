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

## ✨ 4. 기능 요약
- **게시글 CRUD**: 등록, 조회, 수정, 삭제 (소프트 삭제 적용)
- **파일 첨부**: 다중 파일 업로드 및 관리 (UUID 기반 파일명)
- **동적 검색**: 제목, 내용, 날짜 범위별 검색 (QueryDSL 활용)
- **페이징 처리**: 대용량 데이터 효율적 처리
- **조회수 관리**: 게시글 조회시 자동 조회수 증가
- **전역 예외 처리**: 일관된 API 응답 구조

## 📋 5. API 명세 [명세서파일 참고]
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