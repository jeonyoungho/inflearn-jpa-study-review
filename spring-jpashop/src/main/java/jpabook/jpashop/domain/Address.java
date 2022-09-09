package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

// 값 타입은 기본적으로 값이라는거 자체는 immutable 하게 설계되어야함
// 변경이 되면 안됨, 생성할때만 생성이 되고 setter 를 아예 제공안하도록 해야함
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
