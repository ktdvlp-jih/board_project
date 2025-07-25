# Spring Boot Board API Project v2

## 📘 프로젝트 소개
Spring Boot 3.x 기반의 현대적인 게시판 API 프로젝트입니다. 
기존 MyBatis 방식에서 **Spring Data JPA + QueryDSL** 조합으로 전환하여 
타입 안전성과 동적 쿼리의 장점을 활용한 프로젝트입니다.

## 🏗️ 아키텍처
- **Domain Driven Design (DDD)** 구조 적용
- **계층형 아키텍처**: Controller → Service → Repository → Entity
- **파일 첨부 시스템**: 3-table 설계 (board, attach_file, link_file)
- **전역 예외 처리**: GlobalExceptionHandler로 통합 관리
- **포괄적 테스트**: 단위 테스트 + 통합 테스트

## 🧰 기술 스택
- **Java 17** + **Spring Boot 3.5.3**
- **Spring Data JPA + Hibernate 6.x**
- **QueryDSL 5.x** (타입 안전 동적 쿼리)
- **Multi-Database**: MySQL, SQL Server, Oracle, H2
- **Testing**: JUnit 5, Mockito, MockMvc
- **Build Tool**: Gradle 8.x
- **API**: RESTful API with JSON

## 📁 프로젝트 구조
```
src/main/java/com/rsp/platform/
├── common/
│   ├── config/           # 설정 클래스
│   ├── exception/        # 전역 예외 처리
│   └── file/            # 파일 저장 서비스
├── domain/
│   ├── board/
│   │   ├── controller/   # REST API 컨트롤러
│   │   ├── service/      # 비즈니스 로직
│   │   ├── repository/   # JPA + QueryDSL 리포지토리
│   │   ├── entity/       # JPA 엔티티
│   │   └── dto/         # 요청/응답 DTO
│   └── file/
│       ├── entity/       # 파일 관련 엔티티
│       ├── repository/   # 파일 리포지토리
│       └── dto/         # 파일 DTO
└── src/test/java/       # 단위/통합 테스트
```

## ✨ 구현된 주요 기능

### 1. 게시판 CRUD
- **게시글 등록**: 제목, 내용, 파일 첨부 지원
- **게시글 조회**: 상세 조회 시 첨부파일 목록 포함
- **게시글 수정**: 기존 파일 유지/삭제/추가 가능
- **게시글 삭제**: Soft Delete 방식 (is_delete = true)

### 2. 동적 검색 시스템 (QueryDSL)
```java
// 검색 조건 예시
- 코드별 검색 (codeId)
- 제목 검색 (boardTitle)
- 내용 검색 (boardContent)
- 제목+내용 통합 검색
- 날짜 범위 검색 (fromDate ~ toDate)
- 페이징 및 정렬 (page, size, sort, direction)
```

### 3. 파일 첨부 시스템
- **다중 파일 업로드** 지원
- **3-Table 설계**: 게시글-파일 다대다 관계
- **메타데이터 관리**: 원본명, 저장명, 크기, 경로
- **UUID 기반 파일명**: 중복 방지

### 4. 예외 처리 시스템
- **400 Bad Request**: 파라미터 유효성 검증, 날짜 형식 오류
- **404 Not Found**: 게시글 미존재, 리소스 없음
- **500 Internal Server Error**: 서버 오류, 파일 저장 실패

### 5. 테스트 커버리지
- **단위 테스트**: 모든 서비스 메서드 및 예외 케이스
- **통합 테스트**: REST API 엔드포인트 및 HTTP 상태코드
- **테스트 격리**: @MockBean, @WebMvcTest 활용

## 🎯 향후 개발 계획 (Phase별 상세)

### Phase 1: 인증/보안 시스템 구축 (2주)
#### 1.1 Spring Security 통합
- [ ] **JWT 토큰 기반 인증** 구현
- [ ] **사용자 등록/로그인** API 개발
- [ ] **권한 기반 접근 제어** (ROLE_USER, ROLE_ADMIN)
- [ ] **비밀번호 암호화** (BCrypt)

#### 1.2 사용자 관리 시스템
- [ ] **User Entity** 설계 (userId, username, email, roles)
- [ ] **UserService** 비즈니스 로직 구현
- [ ] **회원가입/로그인** 컨트롤러
- [ ] **프로필 관리** 기능

### Phase 2: 게시판 고급 기능 (3주)
#### 2.1 조회수 및 통계
- [ ] **조회수 증가** 로직 (중복 방지)
- [ ] **인기 게시글** 조회 API
- [ ] **일별/월별 통계** 대시보드
- [ ] **Redis 캐싱** 적용 (조회수, 인기글)

#### 2.2 파일 관리 고도화
- [ ] **파일 다운로드** API 구현
- [ ] **이미지 썸네일** 생성
- [ ] **파일 타입 제한** 및 용량 검증
- [ ] **AWS S3 연동** (클라우드 저장소)

#### 2.3 검색 성능 최적화
- [ ] **Elasticsearch 연동** (전문 검색)
- [ ] **인덱스 최적화** (복합 인덱스)
- [ ] **검색 결과 하이라이팅**
- [ ] **자동완성** 기능

### Phase 3: 소셜 기능 추가 (2주)
#### 3.1 댓글 시스템
- [ ] **Comment Entity** 설계 (대댓글 지원)
- [ ] **댓글 CRUD** API
- [ ] **댓글 페이징** 및 정렬
- [ ] **댓글 알림** 시스템

#### 3.2 좋아요/북마크
- [ ] **Like Entity** 설계
- [ ] **좋아요/취소** API
- [ ] **북마크** 기능
- [ ] **좋아요 순** 정렬

### Phase 4: 운영/모니터링 시스템 (2주)
#### 4.1 로깅 및 모니터링
- [ ] **Logback 설정** 최적화
- [ ] **API 응답시간** 측정
- [ ] **에러 로그** 알림 시스템
- [ ] **Actuator** 헬스체크

#### 4.2 성능 최적화
- [ ] **JPA N+1 문제** 해결
- [ ] **쿼리 성능** 튜닝
- [ ] **커넥션 풀** 최적화
- [ ] **캐시 전략** 수립

### Phase 5: DevOps 및 배포 (1주)
#### 5.1 컨테이너화
- [ ] **Docker** 이미지 생성
- [ ] **docker-compose** 멀티 컨테이너
- [ ] **환경별 설정** 분리

#### 5.2 CI/CD 파이프라인
- [ ] **GitHub Actions** 워크플로우
- [ ] **자동 테스트** 실행
- [ ] **배포 자동화**

## 🛠️ 개발 가이드

### 테스트 실행
```bash
# 전체 테스트 실행
./gradlew test

# 특정 클래스 테스트
./gradlew test --tests BoardServiceTest

# 커버리지 리포트 생성
./gradlew jacocoTestReport
```

### 데이터베이스 설정
```bash
# MySQL 사용
./gradlew bootRun --args='--spring.profiles.active=mysql'

# SQL Server 사용  
./gradlew bootRun --args='--spring.profiles.active=mssql'

# H2 사용 (개발/테스트)
./gradlew bootRun --args='--spring.profiles.active=h2'
```

### API 테스트
```bash
# 게시글 목록 조회
curl "http://localhost:8080/api/boards?fromDate=20250701&toDate=20250731"

# 게시글 상세 조회
curl "http://localhost:8080/api/boards/1"

# 게시글 등록
curl -X POST http://localhost:8080/api/boards \
  -H "Content-Type: application/json" \
  -d '{"boardTitle":"테스트","boardContent":"내용","insertId":"user1"}'
```

## 📋 각 파일별 프로세스 및 역할

### 🏗️ Entity Layer (도메인 모델)
#### `BoardEntity.java`
- **역할**: 게시글 도메인 모델, `rsp_board` 테이블 매핑
- **프로세스**: 
  - `create()`: 게시글 생성 시 기본값 설정
  - `update()`: 제목, 내용, 수정자 업데이트
  - `incrementViewCount()`: 조회수 증가 (도메인 메서드)
  - `delete()`: 소프트 삭제 처리

#### `AttachFileEntity.java`
- **역할**: 첨부파일 메타정보, `rsp_attach_file` 테이블 매핑
- **프로세스**: 파일 업로드 시 원본명, 저장명, 경로, 크기 관리

#### `LinkFileEntity.java`
- **역할**: 게시글-파일 N:M 관계 해소, `rsp_link_file` 테이블 매핑
- **프로세스**: `refId`(게시글)와 `attachFileId`(파일) 연결 관리

### 🗂️ Repository Layer (데이터 접근)
#### `BoardRepository.java`
- **역할**: 기본 CRUD 및 단순 조건 쿼리
- **프로세스**: `findByBoardIdAndIsDeleteFalseAndIsEnableTrue()` 활성 게시글 조회

#### `BoardQueryRepository.java` (QueryDSL)
- **역할**: 복잡한 동적 쿼리 처리
- **프로세스**: 
  - `searchBoards()`: 다중 조건 검색 + 페이징 + 정렬
  - `findFilesByBoardIdAndIsDeleteFalse()`: 게시글별 첨부파일 목록 JOIN 조회

#### `AttachFileRepository.java` & `LinkFileRepository.java`
- **역할**: 파일 관련 기본 CRUD 및 관계 관리
- **프로세스**: 파일 개수 집계, 연결 관계 생성/삭제

### 🔄 Service Layer (비즈니스 로직)
#### `BoardService.java`
- **역할**: 게시글 비즈니스 로직의 핵심
- **프로세스**:
  - **조회**: `boardInfo()` - 상세조회 + 조회수 증가 + 파일 목록
  - **등록**: `insertBoard()` - 게시글 생성 + 파일 업로드 처리
  - **수정**: `updateBoard()` - 내용 수정 + 파일 관리
  - **삭제**: `deleteBoard()` - 소프트 삭제
  - **검색**: `searchBoards()` - 동적 검색 + 유효성 검증

#### `FileStorageService.java`
- **역할**: 파일 저장 유틸리티
- **프로세스**:
  - `save()`: 실제 파일시스템 저장
  - `generateSaveName()`: UUID 기반 고유명 생성

### 🌐 Controller Layer (API 엔드포인트)
#### `BoardController.java`
- **역할**: REST API 엔드포인트 제공
- **프로세스**:
  - `GET /api/boards`: 목록/검색 (쿼리 파라미터)
  - `GET /api/boards/{id}`: 상세 조회
  - `POST /api/boards`: 등록 (JSON/Multipart)
  - `PUT /api/boards/{id}`: 수정
  - `DELETE /api/boards/{id}`: 삭제

### 📦 DTO Layer (데이터 전송)
#### `BoardRequest.java`
- **역할**: 클라이언트 → 서버 요청 데이터
- **프로세스**: 등록/수정 시 입력값 검증 및 Entity 변환

#### `BoardListResponse.java` 
- **역할**: 목록 조회용 응답 데이터
- **프로세스**: 필수 정보만 포함 + 파일 개수 표시

#### `BoardDetailResponse.java`
- **역할**: 상세 조회용 응답 데이터  
- **프로세스**: 전체 정보 + 첨부파일 목록 포함

#### `AttachFileResponse.java`
- **역할**: 첨부파일 정보 응답
- **프로세스**: 다운로드용 메타정보 제공

### ⚙️ Configuration Layer (설정)
#### `GlobalExceptionHandler.java`
- **역할**: 전역 예외 처리
- **프로세스**:
  - 400 Bad Request: `IllegalArgumentException`
  - 404 Not Found: `NoSuchElementException`  
  - 500 Internal Server Error: 기타 예외

#### `QueryDslConfig.java`
- **역할**: QueryDSL 설정
- **프로세스**: `JPAQueryFactory` 빈 등록

#### `WebConfig.java`
- **역할**: 웹 설정
- **프로세스**: CORS 설정으로 프론트엔드 통신 허용

### 🔄 전체 아키텍처 흐름
```
1. 요청: Client → Controller → Service → Repository → Database
2. 응답: Database → Entity → DTO → Controller → Client  
3. 파일: MultipartFile → FileStorageService → AttachFileEntity → LinkFileEntity
4. 검색: QueryDSL 동적 쿼리 + 페이징
5. 예외: GlobalExceptionHandler 일관된 에러 처리
```

## 📊 개발 진행 현황
- ✅ **기본 CRUD**: 완료 (100%)
- ✅ **파일 첨부**: 완료 (100%)
- ✅ **동적 검색**: 완료 (100%)
- ✅ **예외 처리**: 완료 (100%)
- ✅ **테스트 코드**: 완료 (100%)
- ✅ **조회수 증가**: 완료 (100%)
- ⏳ **인증/보안**: 예정 (Phase 1)
- ⏳ **고급 기능**: 예정 (Phase 2-3)

## 💡 학습 포인트
1. **MyBatis → JPA 전환**: XML 매핑에서 어노테이션 기반으로
2. **QueryDSL 활용**: 타입 안전 동적 쿼리 구현
3. **테스트 주도 개발**: Mock을 활용한 단위/통합 테스트
4. **예외 처리 패턴**: 전역 예외 핸들러로 일관성 확보
5. **도메인 주도 설계**: 계층별 책임 분리

---
> 🚀 **다음 목표**: Spring Security + JWT 인증 시스템 구축