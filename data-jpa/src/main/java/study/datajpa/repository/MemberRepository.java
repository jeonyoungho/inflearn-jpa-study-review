package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

/**
 * Created by jyh1004 on 2022-10-31
 */

public interface MemberRepository extends JpaRepository<Member, Long> {
}
