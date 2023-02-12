package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.member;

@Repository
//@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em; // 스프링에선 진짜 영속성 컨택스트가 아닌 가짜 프록시 객체를 주입해준다. 걔가 트랜잭션 단위로 다 다른데 바인딩되도록 라우팅을 해준다. 그래서 동시성 문제를 걱정하지 않아도 된다.
    private final JPAQueryFactory queryFactory;

    // 취향인데 생성자에서 em만 받고내부에서 QueryFactory를 new로 생성해줘도 되고, QueryFactory 를 스프링 빈으로 등록해서 바로 의존성 주입받아도 된다.
    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

//    public MemberJpaRepository(EntityManager em, JPAQueryFactory queryFactory) {
//        this.em = em;
//        this.queryFactory = queryFactory;
//    }

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findALl_Querydsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
//                .where(member.username222.eq(username)) // 컴파일 시점에 오류가 나기에 빌드 자체가 안되는게 장점이다.
                .fetch();
    }
}
