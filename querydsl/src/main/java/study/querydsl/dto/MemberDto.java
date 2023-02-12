package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private String username;
    private int age;

    // 장점: 해당 Dto로 큐파일 생성, 컴파일시점에 오류 검출 가능
    // 단점: QueryDSL에 강하게 의존하게 된다. 만약 하부 구현 기술을 Querydsl 에서 다른 것으로 바꾸게되면 해당 Dto도 영향이 가게 된다.
    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
