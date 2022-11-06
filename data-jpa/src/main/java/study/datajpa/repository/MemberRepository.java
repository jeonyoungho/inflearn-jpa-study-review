package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

/**
 * Created by jyh1004 on 2022-10-31
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

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


}
