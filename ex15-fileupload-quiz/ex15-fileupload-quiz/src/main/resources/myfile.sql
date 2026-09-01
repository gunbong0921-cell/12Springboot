-- ==========================================================
-- 퀴즈1: MYFILE 테이블 및 시퀀스 생성 스크립트
-- ==========================================================

-- 1. 시퀀스 삭제 및 생성 (순차적 일련번호 증가용)
DROP SEQUENCE seq_myfile_num;
CREATE SEQUENCE seq_myfile_num
    INCREMENT BY 1
    START WITH 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

-- 2. 기존 테이블 삭제 (필요시)
DROP TABLE MYFILE CASCADE CONSTRAINTS;

-- 3. 개선된 MYFILE 테이블 생성
-- [수정/개선 사항]
-- 1) OFILE: AL32UTF8 환경에서 한글은 1자당 3바이트를 차지하므로, 
--           100바이트(한글 약 33자)는 긴 파일명 시 ORA-12899 오류가 발생할 수 있어 200 BYTE로 확장.
-- 2) SFILE: UUID(32자) + 확장자 저장을 위해 기본 50자에서 안전하게 100 BYTE로 확장.
-- 3) IDX  : 시퀀스를 통한 자동 번호 증가 및 기본키(PK) 지정.
CREATE TABLE MYFILE (
    IDX       NUMBER              NOT NULL,
    TITLE     VARCHAR2(200 BYTE)  NOT NULL,
    CATE      VARCHAR2(100 BYTE),
    OFILE     VARCHAR2(200 BYTE)  NOT NULL,
    SFILE     VARCHAR2(100 BYTE)  NOT NULL,
    POSTDATE  DATE                DEFAULT SYSDATE NOT NULL,
    CONSTRAINT myfile_pk PRIMARY KEY (IDX)
);

-- 테이블 확인
SELECT * FROM MYFILE;
