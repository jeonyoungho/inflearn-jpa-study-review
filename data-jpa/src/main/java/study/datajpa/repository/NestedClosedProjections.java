package study.datajpa.repository;

/**
 * Created by jyh1004 on 2022-11-07
 *
 * 중첩 구조일때 select 절 최적화가 안됨
 */

public interface NestedClosedProjections {
	String getUsername();
	TeamInfo getTeam();

	interface TeamInfo {
		String getName();
	}
}
