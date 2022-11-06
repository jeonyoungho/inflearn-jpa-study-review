package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Item;

/**
 * Created by jyh1004 on 2022-11-07
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
}
