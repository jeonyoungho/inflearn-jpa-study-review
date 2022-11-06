package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

	@PostConstruct
	public void init() {
		memberRepository.save(new Member("userA"));
	}
}
