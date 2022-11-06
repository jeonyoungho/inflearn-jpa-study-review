package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

/**
 * Created by jyh1004 on 2022-11-07
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	// * 도메인 클래스 컨버터 - 권장하지 않음
	// pk로 막 조회를 외부에 공개해서 왔다갔다하는 경우가 생각보다 없음
	// 단순하게 돌아가는 경우도 많지 않음
	// 간단간단할때만 사용할 수 있다 정도로만 알고 있으면 된다.
	// 조회용으로만 사용해라 (트랜잭션이 없는 범위에서 엔티티를 조회했으므로)
	@GetMapping("/members2/{id}")
	public String findMember2(@PathVariable("id") Member member) {
		return member.getUsername();
	}


	// @Qualifier -> 페이징할 대상이 여러개일때 구분하기 위해 사용 (접두어로 붙임)
	/*
	Page를 1부터 시작하기
	스프링 데이터는 Page를 0부터 시작한다.
	만약 1부터 시작하려면?
	1. Pageable, Page를 파리미터와 응답 값으로 사용히지 않고, 직접 클래스를 만들어서 처리한다. 그리고
	직접 PageRequest(Pageable 구현체)를 생성해서 리포지토리에 넘긴다. 물론 응답값도 Page 대신에
	직접 만들어서 제공해야 한다.
	2. spring.data.web.pageable.one-indexed-parameters 를 true 로 설정한다. 그런데 이 방법은
	web에서 page 파라미터를 -1 처리 할 뿐이다. 따라서 응답값인 Page 에 모두 0 페이지 인덱스를
	사용하는 한계가 있다.
	 */
	@GetMapping("/members")
	public Page<MemberDto> list(@Qualifier("member") @PageableDefault(size = 5) Pageable pageable) {
		Page<Member> page = memberRepository.findAll(pageable);
		Page<MemberDto> map = page.map(MemberDto::new);
		return map;
	}

//	@PostConstruct
	public void init() {
		for (int i = 0; i < 100; i++) {
			memberRepository.save(new Member("user" + i, i));
		}

	}
}
