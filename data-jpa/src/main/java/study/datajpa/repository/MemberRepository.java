package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * Created by jyh1004 on 2022-10-31
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);
}
