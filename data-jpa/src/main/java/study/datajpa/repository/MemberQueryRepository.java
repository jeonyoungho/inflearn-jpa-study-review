package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;


/**
 * Created by jyh1004 on 2022-11-07
 *
 * 화면에 맞춘 api, 그리고 통계성 쿼리들을 별도로 분리하는 것이 좋다.
 *
 * 핵심 비즈니스 로직 쿼리 메서드와 위 쿼리 메서드는 근본적으로 수정 라이플 사이클이 다르기에
 * 별도 클래스로 분리해서 관리하는 것이 좋다.
 *
 * 모든 것을 MemberRepositoryImpl 클래스로 다 몰아넣어선 안된다.
 *
 * 아키텍처적으로 고민을 해보는게 좋다.
 * - 커맨드와 쿼리를 분리하는 것
 * - 핵심 비즈니스 로직과 아닌 것들을 분리하는 것
 * - 라이프사이클에 따라 뭘 변경해야되는지가 달라지는 것
 * => 이런 것들을 다각적으로 고민하면서 클래스들을 쪼개고 이렇게 하는 것을 권장한다.
 */

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

	private final EntityManager em;

	List<Member> findAllMembers() {
		return em.createQuery("select m from Member m").getResultList();
	}
}
