rsp_project API 명세서
======================

Base URL: http://localhost:8080/

1. 게시판 조회
--------------
- URL: GET /api/boards
- 설명: 게시글 목록 조회 (검색 조건 포함)
- 헤더:
  - Content-Type: application/json
- Query Parameters:
  | 이름 | 예시값 | 설명 |
  |------|--------|------|
  | codeId | 003 | 게시판 코드 ID |
  | boardTitle | 테스트제 | 제목 검색어 |
  | boardContent | 테스트본 | 내용 검색어 |
  | fromDate | 2025-07-21T23:12:00 | 검색 시작일 |
  | toDate | 2025-07-25T23:12:00 | 검색 종료일 |

2. 게시판 상세 조회
-------------------
- URL: GET /api/boards/46
- 설명: 특정 게시글 상세 조회
- 헤더:
  - Content-Type: application/json

3. 게시판 등록 (첨부파일 없음)
-----------------------------
- URL: POST /api/boards
- 설명: 게시글 등록 (일반 JSON 요청)
- 헤더:
  - Content-Type: application/json
- Body (JSON):
{
  "boardTitle": "테스트 제목",
  "boardContent": "테스트 본문",
  "startDate": "2024-07-24T23:12:00",
  "endDate": "2024-07-31T23:12:00",
  "insertId": "전일훈"
}

4. 게시판 등록 (첨부파일 포함)
-----------------------------
- URL: POST /api/boards
- 설명: 게시글 등록 (첨부파일 포함 - multipart/form-data)
- 헤더:
  - Content-Type: multipart/form-data
- Form Data:
  | Key | 타입 | 예시 |
  |-----|------|------|
  | dto | text (application/json) | { "boardTitle": "테스트제목", "boardContent": "테스트본문", "startDate": "2025-07-21T00:58:09", "endDate": "2025-07-29T00:58:09", "insertId": "작성자 전일훈" } |
  | files | file | (첨부파일) |

5. 게시판 수정 (첨부파일 포함)
----------------------------
- URL: PUT /api/boards/46
- 설명: 특정 게시글 수정 (첨부파일 포함 - multipart/form-data)
- 헤더:
  - Content-Type: multipart/form-data
- Form Data:
  | Key | 타입 | 예시 |
  |-----|------|------|
  | dto | text (application/json) | { "boardTitle": "테스트제목", "boardContent": "테스트본문", "startDate": "2025-07-21T00:58:09", "endDate": "2025-07-29T00:58:09", "updateId": "수정자 전일훈" } |
  | files | file | (첨부파일) |

6. 게시판 삭제
--------------
- URL: DELETE /api/boards/44
- 설명: 특정 게시글 삭제
- 헤더:
  - Content-Type: application/json

참고
----
- {{baseURL}} 변수는 실제 요청 시 http://localhost:8080/