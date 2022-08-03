package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = new Member();
			member.setUsername("member1");
			member.setHomeAddress(new Address("homeCity", "street", "10000"));

			member.getFavoriteFoods().add("치킨");
			member.getFavoriteFoods().add("족발");
			member.getFavoriteFoods().add("피자");

			member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
			member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

			em.persist(member);

			em.flush();
			em.clear();

			System.out.println("=== START ===");
			Member findMember = em.find(Member.class, member.getId());

			// findMember.getHomeAddress().setCity("newCity"); // 이 방법 x -> 사이드 이펙트가 발생할 수 있음
//			Address a = findMember.getHomeAddress();
//			findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode())); // 값 타입 변경은 완전히 통으로 새로운 객체를 생성해서 갈아끼워야됨

			// 치킨 -> 한식
//			findMember.getFavoriteFoods().remove("치킨");
//			findMember.getFavoriteFoods().add("한식");

			// old1 -> newCity1
//			findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "10000"));
//			findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			//close
			em.close();
		}

		emf.close();
	}
}
