package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by jyh1004 on 2022-10-31
 *
 * entityManager의 merge는 언제 사용하느냐?
 * -> 영속 상태의 엔티티가 어떠한 이유로 detached 상태가 되었을 경우 다시 영속상태로 만들기 위해 사용한다.
 *
 * Specification -> 실무에서 사용하지 말 것. 유지보수 관점에서 안좋음
 */

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //  1. 해당 메서드명으로 엔티티명.메서드명 의 이름을 가진 네임드쿼리를 먼저 찾음,
    //  2. 네임드쿼리가 없으면 그때 메서드명으로 쿼리를 생성하는 기능을 수행

    // == 실무에서 잘 안씀 ==
    // 1. 엔티티에 쿼리 있는 것도 너무 안좋고, 엔티티랑 레포지토리랑 왔다갔다 해야됨
    // 2. 레포지토리 메서드에 바로 쿼리를 작성하는 기능이 너무 막강함

    // * 네임드 쿼리의 장점 -> 애플리케이션 로딩 시점에 쿼리를 파싱해서 오류를 발견할 수 있음
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 실무에서 많이 사용 (이름이 없는 네임드 쿼리)
    // 애플리케이션 로딩 시점에 파싱해서 SQL 을 미리 만들어 놓는데 문법 오류가 있으면 미리 발견할 수가 있음
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // @Query, 값 조회하기
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // @Query DTO 조회하기
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // @Query 파라미터 바인딩 (실무에서 많이 사용)
    // 위치 기반, 이름 기반 두 가지가 있는데 이름 기반만 사용할 것
    // 위치 기반은 코드 가독성에서나 유지보수 관점에서나 좋지 못함
    // 파라미터 위치가 달라지면 오류가 발생하게 될 확률 높음
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    // == 유연한 반환 타입 지원 ==
    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    // == 스프링 데이터 JPA 페이징과 정렬 ==
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // 성능 최적화를 위해 카운트 쿼리 분리, 카운트 쿼리는 조인할 필요가 없으니 이와 같이 별도로 분리하여 최적화 진행
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable); // <- total 쿼리 안날림
//    List<Member> findByAge(int age, Pageable pageable);


    // == 벌크성 수정 쿼리 ==
    @Modifying(clearAutomatically = true) // jpa 의  executeUpdate 메서드 실행, clearAutomatically -> 영속성 컨텍스트 자동 초기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // == @EntityGraph => 페치 조인을 편리하게 사용 가능 ==
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    public List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all") // NamedEntityGraph 사용(JPA 표준 스펙)
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // == JPA Hint ==
    // 처음부터 튜닝 할 필욘없다. -> 성능 테스트 후 결정하는게 좋다.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // == Lock ==
    // select for update (DB에 select 할떄 다른애들 손대지마! 하고 락을 걸수가 있음
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    // == Projections ==
//    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
//    List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);

    // 동적 프로젝션
    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    // 네이티브 쿼리
    // - select 절에 필드들을 다 지정해줘야됨
    // - 반환타입이 몇 가지 지정 안됨
    /*
    - 페이징 지원
    - 반환 타입
        - Object[]
        - Tuple
        - DTO(스프링 데이터 인터페이스 Projections 지원)
    - 제약
        - Sort 파라미터를 통한 정렬이 정상 동작하지 않을 수 있음(믿지 말고 직접 처리)
        - JPQL처럼 애플리케이션 로딩 시점에 문법 확인 불가
    - 동적 쿼리 불가
     */
    @Query(value = "select username from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName from member m left join team t",
           countQuery = "select count(*) from member",
           nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);


}
