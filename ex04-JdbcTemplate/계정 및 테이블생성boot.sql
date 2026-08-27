--##테입블생성 및 더미데이터 입력은 boot_user로 입력합니다.
--회원관리 테이블 생성
create table member (
	id varchar2(30) not null,    
	pass varchar2(40) not null,  
	name varchar2(50) not null,  
	regidate date default sysdate, 
	primary key (id)
);

--더미 데이터 입력 및 커밋 
insert into member (id, pass, name) values ('user1', '1111', '사용자1');
commit;

--레코드 확인 
select * from member;
