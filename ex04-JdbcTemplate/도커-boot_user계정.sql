

--mybatis 게시판
create table myboard (
	idx number primary key,               /* 일련번호 */
	name varchar2(50) not null,         /* 작성자 이름 */
	title varchar2(200) not null,          /* 제목 */
	content varchar2(2000) not null, /* 내용 */
	postdate date default sysdate not null,   /* 작성일 */	
	visitcount number default 0 not null       /* 게시물 조회수 */
);

--게시판 테이블에서 사용할 시퀀스
create sequence seq_board_num 
    increment by 1
    start with 1
    minvalue 1
    nomaxvalue
    nocycle
    nocache;





--더미 데이터 입력
insert into myboard (idx, name, title, content)
    values (seq_board_num.nextval, '김유신', '자료실 제목1 입니다.',
    '삼국 통일 과정에서 큰 공을 세운 신라의 장군이다.');
insert into myboard (idx, name, title, content)
    values (seq_board_num.nextval, '장보고', '자료실 제목2 입니다.',
    '청해진을 설치하고 해상 무역을 장악했던 통일신라의 해상 세력가이다.');
insert into myboard (idx, name, title, content)
    values (seq_board_num.nextval, '이순신', '자료실 제목3 입니다.',
    '임진왜란 당시 뛰어난 전략과 거북선으로 조선을 지켜낸 명장이다.');
insert into myboard (idx, name, title, content)
    values (seq_board_num.nextval, '강감찬', '자료실 제목4 입니다.',
    '귀주대첩에서 거란군을 크게 물리친 고려의 명장이다.  ');
insert into myboard (idx, name, title, content)
    values (seq_board_num.nextval, '대조영', '자료실 제목5 입니다.',
    '고구려 유민을 모아 발해를 건국한 지도자이다.');
    
--커밋 / 레코드 확인 
commit;
select * from myboard;

SELECT seq_board_num.NEXTVAL FROM DUAL;




