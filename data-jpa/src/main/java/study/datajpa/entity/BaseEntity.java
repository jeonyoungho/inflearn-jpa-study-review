package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Created by jyh1004 on 2022-11-07
 *
 *
 * auditing: 엔티티를 생성하거나 변경할때 변경한 사람과 시간을 추적하고 싶은 경우 사용
 *
 * - 디테일한 추적이라기보단 실무에서 기본적으로 테이블을 만들때 꼭 등록일, 수정일을 꼭남긴다. 그래야 운영 할 때 편하다.
 * - 안 남길경우 이력이 추적이 안되서 운영할 때 힘들다.
 * - 등록일과 수정일은 기본적으로 모든 테이블에 가져간다. (그 테이블에 수정이 필요없다 하는 경우 제외하고는)
 * - 등록자, 수정자의 경우엔 로그인한 세션정보를 가지고 로그인한 아이디를 기반으로 남긴다.
 * - 그렇게 해서 등록일, 수정일, 등록자, 수정자를 기본적으로 깔고간다.
 */

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity {

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;
}
