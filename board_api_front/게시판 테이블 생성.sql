/* 

    Date : 2025-07-19
    Author:전일훈
    Description: 게시판용 시퀀스 및 테이블 생성

    url : 192.168.219.107,1433
    DB: rsp_project
    id : rsp_project
    pw : rsp_project123

*/

/*
/*

-- DROP 순서 (실행 시 주의)
DROP TABLE rsp_file;
DROP TABLE rsp_comment;
DROP TABLE rsp_board;
DROP TABLE rsp_category;

DROP SEQUENCE rsp_file_seq;
DROP SEQUENCE rsp_comment_seq;
DROP SEQUENCE rsp_board_seq;
DROP SEQUENCE rsp_category_seq;
*/

*/

CREATE SEQUENCE rsp_category_seq AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 NO MAXVALUE CACHE 20;
CREATE SEQUENCE rsp_board_seq    AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 NO MAXVALUE CACHE 20;
CREATE SEQUENCE rsp_comment_seq  AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 NO MAXVALUE CACHE 20;
CREATE SEQUENCE rsp_file_seq     AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 NO MAXVALUE CACHE 20;

CREATE TABLE rsp_category (
    category_id   BIGINT PRIMARY KEY NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    is_enable     BIT DEFAULT 1 NOT NULL,
    insert_id     VARCHAR(20) NOT NULL,
    insert_date   DATETIME DEFAULT GETDATE() NOT NULL,
    update_id     VARCHAR(20) NOT NULL,
    update_date   DATETIME DEFAULT GETDATE() NOT NULL
);

CREATE TABLE rsp_board (
    board_id      BIGINT PRIMARY KEY NOT NULL,
    board_title   VARCHAR(255) NOT NULL,
    board_content TEXT NOT NULL,
    view_count    INT DEFAULT 0 NOT NULL,
    is_delete     BIT DEFAULT 0 NOT NULL,
    is_enable     BIT DEFAULT 1 NOT NULL,
    insert_id     VARCHAR(20) NOT NULL,
    insert_date   DATETIME DEFAULT GETDATE() NOT NULL,
    update_id     VARCHAR(20) NOT NULL,
    update_date   DATETIME DEFAULT GETDATE() NOT NULL
);

CREATE TABLE rsp_comment (
    comment_id     BIGINT PRIMARY KEY NOT NULL,
    board_id       BIGINT NOT NULL,
    comment_content TEXT NOT NULL,
    is_delete      BIT DEFAULT 0 NOT NULL,
    is_enable      BIT DEFAULT 1 NOT NULL,
    insert_id      VARCHAR(20) NOT NULL,
    insert_date    DATETIME DEFAULT GETDATE() NOT NULL,
    update_id      VARCHAR(20) NOT NULL,
    update_date    DATETIME DEFAULT GETDATE() NOT NULL
);


CREATE TABLE rsp_file (
    file_id       BIGINT PRIMARY KEY NOT NULL,
    board_id      BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    saved_name    VARCHAR(255) NOT NULL,
    file_path     VARCHAR(500) NOT NULL,
    file_size     BIGINT NOT NULL,
    is_delete     BIT DEFAULT 0 NOT NULL,
    is_enable     BIT DEFAULT 1 NOT NULL,
    insert_id     VARCHAR(20) NOT NULL,
    insert_date   DATETIME DEFAULT GETDATE() NOT NULL,
    update_id     VARCHAR(20) NOT NULL,
    update_date   DATETIME DEFAULT GETDATE() NOT NULL
);

-- rsp_category
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'카테고리 고유 ID (시퀀스)',        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = category_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'카테고리명',                       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = category_name;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'사용 가능 여부 (1: 사용, 0: 비활성)', @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = is_enable;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성자 ID',                        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = insert_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성 일시',                        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = insert_date;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정자 ID',                        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = update_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정 일시',                        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_category, @level2type = N'COLUMN', @level2name = update_date;

-- rsp_board
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'게시판 고유 ID (시퀀스 사용)',       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = board_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'게시글 제목',                       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = board_title;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'게시글 내용',                       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = board_content;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'조회수',                            @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = view_count;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'삭제 여부 (0: 정상, 1: 삭제)',       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = is_delete;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'활성화 여부 (1: 활성화, 0: 비활성화)', @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = is_enable;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'데이터 생성자 ID',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = insert_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'데이터 생성 일시',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = insert_date;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'데이터 수정자 ID',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = update_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'데이터 수정 일시',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_board, @level2type = N'COLUMN', @level2name = update_date;

-- rsp_comment
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'댓글 고유 ID (시퀀스)',             @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = comment_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'연결된 게시글 ID',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = board_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'댓글 내용',                          @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = comment_content;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'삭제 여부 (0: 정상, 1: 삭제)',       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = is_delete;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'활성화 여부 (1: 활성, 0: 비활성)',  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = is_enable;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성자 ID',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = insert_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성 일시',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = insert_date;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정자 ID',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = update_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정 일시',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_comment, @level2type = N'COLUMN', @level2name = update_date;

-- rsp_file
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'파일 고유 ID (시퀀스)',              @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = file_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'연결된 게시글 ID',                  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = board_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'원본 파일명',                        @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = original_name;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'저장된 파일명',                      @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = saved_name;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'파일 저장 경로',                     @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = file_path;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'파일 크기 (byte)',                   @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = file_size;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'삭제 여부 (0: 정상, 1: 삭제)',       @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = is_delete;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'활성화 여부 (1: 활성, 0: 비활성)',  @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = is_enable;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성자 ID',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = insert_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'생성 일시',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = insert_date;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정자 ID',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = update_id;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'수정 일시',                         @level0type = N'SCHEMA', @level0name = dbo, @level1type = N'TABLE',  @level1name = rsp_file, @level2type = N'COLUMN', @level2name = update_date;

-- 카테고리
INSERT INTO rsp_category (category_id, category_name, is_enable, insert_id, insert_date, update_id, update_date)
VALUES 
  (NEXT VALUE FOR rsp_category_seq, '공지사항', 1, 'admin', GETDATE(), 'admin', GETDATE()),
  (NEXT VALUE FOR rsp_category_seq, '기술지원', 1, 'admin', GETDATE(), 'admin', GETDATE());

-- 게시글
INSERT INTO rsp_board (board_id, board_title, board_content, view_count, is_delete, is_enable, insert_id, insert_date, update_id, update_date)
VALUES 
  (NEXT VALUE FOR rsp_board_seq, '첫 글입니다', '내용입니다.', 0, 0, 1, 'admin', GETDATE(), 'admin', GETDATE());

-- 댓글
INSERT INTO rsp_comment (comment_id, board_id, comment_content, is_delete, is_enable, insert_id, insert_date, update_id, update_date)
VALUES 
  (NEXT VALUE FOR rsp_comment_seq, 1, '첫 댓글입니다.', 0, 1, 'user1', GETDATE(), 'user1', GETDATE());

-- 첨부파일
INSERT INTO rsp_file (file_id, board_id, original_name, saved_name, file_path, file_size, is_delete, is_enable, insert_id, insert_date, update_id, update_date)
VALUES 
  (NEXT VALUE FOR rsp_file_seq, 1, '안내문.pdf', 'uuid_abc123.pdf', '/files/2025/07/', 102400, 0, 1, 'admin', GETDATE(), 'admin', GETDATE());
