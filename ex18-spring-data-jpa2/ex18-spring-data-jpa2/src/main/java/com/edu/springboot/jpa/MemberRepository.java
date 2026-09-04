package com.edu.springboot.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  List<Member> findByName(String keyword);
  List<Member> findByEmail(String keyword);

  List<Member> findByNameLike(String keyword);
  List<Member> findByNameLikeOrderByNameDesc(String keyword);
  List<Member> findByNameLikeOrderByEmailDesc(String keyword);
  List<Member> findByNameLike(String keyword, Sort sort);
}
