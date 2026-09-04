package com.edu.springboot.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository 
public interface MemberRepository extends JpaRepository<Member, Long> {
  /*
  반환타입을 List로 설정하면 엔티티에서 인출한 레코드만 반환한다. 득 해당 페이지에 출력할
  Resultset을 만들어서 반환한다.
  */
  // List<Member> findByNameLike(String keyword, Pageable pageable);

  /*
  반환타입을 Page로 설정하면 인출된 Resultset을 다양한 정보를 페이징 처리하여 반환한다.
  총페이지수, 레코드의 개수 등이 반환된다. 
  */
  Page<Member> findByNameLike(String keyword, Pageable pageable);
}
