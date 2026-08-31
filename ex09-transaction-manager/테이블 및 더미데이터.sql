

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
    
--조회수 업데이트하기
update myboard set visitcount=visitcount+1 where idx=5;
SELECT idx, title, visitcount from myboard where idx=5;
SELECT idx, title, visitcount from myboard;
SELECT name, title, content FROM myboard;

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

--티켓 구매 금액을 입력하는 테이블
create table transaction_pay (
    userid varchar2(30) not null,  
    amount number not null
);

--구매한 티켓의 매수를 입력하는 테이블 
--check제약조건에 의해 5장을 초과하면 에러가 발생한다.
create table transaction_ticket (
    userid varchar2(30) not null,
    t_count number(2) not null 
        check(t_count<=5)        
);

--티켓 1장당 10000원이라 가정하면 아래는 2장의 티켓을 구매한 데이터가 된다. 
--아래 2개의 insert문이 하나의 단위 업무이므로 하나의 '트랜잭션'이라 할 수 있다. 
insert into transaction_ticket values ('korea1', 2);
insert into transaction_pay values ('korea1', 20000);

----6장 이상은 구매할 수 없으므로 아래 쿼리문은 금액부분만 입력됨 
--즉, 트랜잭션 처리가 되지않아 매수와 금액이 일치하지 않는 오류가 발생된다. 
insert into transaction_ticket values ('japan2', 8);
insert into transaction_pay values ('japan2', 80000);

commit;

/*
아래 결과를 보면 티켓은 2장만 판매되었지만, 금액은 10만원이 입력되어있다.
즉 트랜젝션 처리가 되지 않아 티켓의 매수와 판매금액이 일치하지 않는 문제가
발생되었다고 볼 수 있다.
*/
select * from transaction_ticket;
SELECT * FROM transaction_pay;




