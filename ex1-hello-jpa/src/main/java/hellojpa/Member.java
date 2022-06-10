package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Member {

	@Id
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String username;

	private int age;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	private LocalDate test1;
	private LocalDateTime test2;

	@Lob
	private String description;

	@Transient // 메모리에서만 쓰겠다
	private int temp;

	public Member() {
	}


}
