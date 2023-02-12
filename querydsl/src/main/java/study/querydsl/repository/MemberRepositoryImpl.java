package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

// 구현체 클래스명은 스프링 Data Jpa 인터페이스명 + Impl 로 작명해줘야함!
// @Repository 어노테이션 별도로 필요없다. 스프링 bean 으로 등록하고 자동으로 해준다.
public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
//    public MemberRepositoryImpl(Class<?> domainClass) {
//        super(domainClass);
//    }

    private final JPAQueryFactory queryFactory;
//
//    public MemberRepositoryImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }

//    public MemberRepositoryImpl() {
//        super(Member.class);
//    }

    public MemberRepositoryImpl(EntityManager em) {
        super(Member.class);
        this.queryFactory = new JPAQueryFactory(em);
    }



    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

        EntityManager entityManager = getEntityManager();

        // querydsl 3버전은 from 부터 시작을 했고 그떄 QuerydslSupporter 가 만들어졌기에 어쩔수없이 from 부터 시작하게 된다.
        List<MemberTeamDto> result = from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .fetch();

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults(); // 자동으로 contents 용 쿼리 날리고 count 쿼리 두 방 날린다. order by total 카운트 쿼리에선 자동으로 지워진다. (최적화)

        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<MemberTeamDto> searchPageSimple2(MemberSearchCondition condition, Pageable pageable) {
        JPQLQuery<MemberTeamDto> jpqQuery = from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")));
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())

        /**
         * 장점
         * - getQuerydsl().applyPagination() 스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환 가능(단! Sort는 오류발생)
         * - from() 으로 시작 가능(최근에는 QueryFactory를 사용해서 select() 로 시작하는 것이 더 명시적) EntityManager 제공
         *
         * 한계
         * - Querydsl 3.x 버전을 대상으로 만듬
         * - Querydsl 4.x에 나온 JPAQueryFactory로 시작할 수 없음
         * - select로 시작할 수 없음 (from으로 시작해야함) QueryFactory 를 제공하지 않음
         * - 스프링 데이터 Sort 기능이 정상 동작하지 않음
         */
        JPQLQuery<MemberTeamDto> query = getQuerydsl().applyPagination(pageable, jpqQuery);

        QueryResults<MemberTeamDto> results = query.fetchResults();


        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /**
         * - 직접 total 카운트 쿼리 호출 (커스터마이징)
         * - 쿼리를 짜다보면 content 쿼리는 복잡한데, count 쿼리는 심플하게 뽑아낼 수 있을떄가 있음 (join 이 필요없는 등)
         * - 그럴 떄 사용하면 약간의 성능상의 이점을 챙길 수 있다.
         * - 더 최적화하려면 count 쿼리를 먼저 실행하고 데이터가 없으면 content 쿼리를 실행안하는 듯 더 최적화할 수 있다.
         * - 생각보다 카운트 쿼리를 효율화할 수 있으면 효율화하는게 좋다. (데이터 많을 경우에만 해당)
         */
        JPAQuery<Member> countQuery = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        /**
         * count 쿼리가 생략 가능한 경우 생략해서 처리 (카운트 쿼리 약간 더 최적화할 수 있는 기법)
         * - 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
         * - 마지막 페이지 일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함)
         *
         * 위 조건에 해당할 경우 3번쨰 인자로 넘어간 함수 자체를 실행하지 않음
         */
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
//        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}
