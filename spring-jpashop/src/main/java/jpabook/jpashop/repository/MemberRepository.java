package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jyh1004 on 2022-10-24
 */

public interface MemberRepository extends JpaRepository<Member, Long> {
	// select m from Member m where m.name = ?
	List<Member> findByName(String name);
}
