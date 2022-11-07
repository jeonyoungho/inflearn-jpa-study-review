package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jyh1004 on 2022-11-07
 */

public interface UsernameOnly {

	@Value("#{target.username + ' ' + target.age}") // Open Projection, SPEL, 없을 경우 Close Projection
	String getUsername();
}
