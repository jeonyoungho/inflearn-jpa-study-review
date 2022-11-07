package study.datajpa.repository;

/**
 * Created by jyh1004 on 2022-11-07
 */

public class UsernameOnlyDto {

	private final String username;

	public UsernameOnlyDto(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
