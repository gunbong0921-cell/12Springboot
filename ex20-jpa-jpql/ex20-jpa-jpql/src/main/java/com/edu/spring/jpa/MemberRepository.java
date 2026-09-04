package com.edu.spring.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository 
public interface MemberRepository extends JpaRepository<Member, Long> {

  //JPQL을 이용해서 like와 order by까지 쿼리로 작성
  //인파라미터는 ':변수명'과 같이 작성하고 @Param("변수명") 과 같이 매핑
  @Query("SELECT m FROM jpamember04 m WHERE m.name LIKE :name1 " + " order by m.id desc")
  List<Member> findMembers(@Param("name1") String name2);

  @Query("SELECT m FROM jpamember04 m WHERE m.name LIKE :name1")
  List<Member> findMembers(@Param("name1") String name2, Sort sort);

  @Query("SELECT m FROM jpamember04 m WHERE m.name LIKE :name1")
  Page<Member> findMembers(@Param("name1") String name2, Pageable pageable);
  /*
  JPQL 에서의 테이블명은 엔티티명을 기술해야 하므로 대소문자를 구분한다. 따라서 엔티티가 
  member라면 JPQL에서도 member라고 기술해야 한다.
  */

  /*
  nativeQuery를 true로 설정하면 표준 SQL문을 사용할 수 있다. 이 경우에는 대소문자를 구분하지 않는다.
  */
  @Query(value = "SELECT * FROM jpamember04 WHERE name LIKE :name1" + " order by id desc", nativeQuery = true)
  List<Member> findMembersNative(@Param("name1") String name2);
}
