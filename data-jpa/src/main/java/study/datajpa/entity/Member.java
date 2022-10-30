package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jyh1004 on 2022-10-31
 */

@Entity
@Getter @Setter
public class Member {

	@Id @GeneratedValue
	private Long id;
	private String username;

	protected Member() {
	}

	public Member(String username) {
		this.username = username;
	}

}
