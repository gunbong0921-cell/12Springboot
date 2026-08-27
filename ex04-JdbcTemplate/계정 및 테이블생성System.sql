--새로운 계정 생성은 system으로 접속한 후 실행한다.
--boot_user / 1234 로 계정 생성
create user boot_user identified by 1234; 
--권한 부여 
grant connect, resource, unlimited tablespace to boot_user;


