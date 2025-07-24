# README.md

## 📘 프로젝트 소개
Spring Boot 기반의 게시판 프로젝트입니다. 기존에는 MyBatis 방식에 익숙했으나
이번 프로젝트에서는 Spring Data JPA + Hibernate + QueryDSL 조합을 새롭게 적용하였습니다.

## 🧰 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Data JPA + Hibernate
- QueryDSL (동적 쿼리)
- MySQL, SQL Server, Oracle, H2 (다중 DB 지원)
- Gradle
- JUnit 5, Mockito (테스트)

## 📁 프로젝트 구조
```
├── src
│   ├── main
│   │   ├── java/com/rsp/board
│   │   │   ├── entity
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── controller
│   │   └── resources
│   │       ├── application.properties
│   │       ├── application-mssql.properties
│   │       ├── application-mysql.properties
│   │       ├── application-maria.properties
│   │       ├── application-oracle.properties
│   │       ├── application-postsql.properties
│   │       └── logback-spring.xml
```

## ✨ 주요 기능
- 게시글 CRUD (등록, 조회, 수정, 삭제)
- 파일 첨부 및 관리 (다중 파일 지원)
- 동적 검색 (제목, 내용, 제목+내용, 날짜 범위)
- 페이징 처리 및 정렬
- 단위/통합 테스트

## ⚙ 실행 방법
1. 원하는 DB에 맞는 프로파일을 선택해 활성화 (`application.properties`)
2. Gradle 빌드 후 실행
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=mssql'
   ```

## 📝 예시 설정
### application.properties
```properties
spring.profiles.active=mssql
```

### application-mssql.properties
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=boarddb
spring.datasource.username=sa
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> 💡 다른 DB를 사용할 경우 `application-mysql.properties`, `application-oracle.properties` 등에서 설정을 변경해 사용하면 됩니다.

## 📚 JPA Repository 예시
```java
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByIsDeleteFalseAndIsEnableTrueOrderByBoardIdDesc();
}
```

## 🧩 Service 예시
```java
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getActiveBoards() {
        return boardRepository.findByIsDeleteFalseAndIsEnableTrueOrderByBoardIdDesc();
    }
}
```

## 🚧 향후 계획
- 사용자 인증/권한 관리
- 게시글 조회수 증가 기능
- 파일 다운로드 API
- 게시글 좋아요/댓글 기능

