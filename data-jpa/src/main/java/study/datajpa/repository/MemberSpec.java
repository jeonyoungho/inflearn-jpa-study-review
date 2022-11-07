package study.datajpa.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.*;

/**
 * Created by jyh1004 on 2022-11-07
 *
 * JPA가 제공하는 Criteria를 활용해서 스프링 데이터 JPA가 Specification라는 기능으로 제공함
 * 동적 쿼리 문제를 해결하기 위해 사용
 * 너무 복잡해서 유지보수 관점에서 좋지 못하다.
 * 문법이 기본적으로 직관적이지 못함
 * 실무에선 JPA Criteria를 거의 안쓴다! 대신에 QueryDSL을 사용하자!
 *
 */

public class MemberSpec {

	public static Specification<Member> teamName(final String teamName) {
		return new Specification<Member>(){

			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!StringUtils.hasText(teamName)) {
					return null;
				}

				Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인
				return builder.equal(t.get("name"), teamName);
			}
		};
	}

	public static Specification<Member> username(final String username) {
		return (Specification<Member>) (root, query, builder) ->
			builder.equal(root.get("username"), username);
	}
}
