package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

/**
 * Created by jyh1004 on 2022-11-07
 */

public interface MemberRepositoryCustom {
	List<Member> findMemberCustom();
}
