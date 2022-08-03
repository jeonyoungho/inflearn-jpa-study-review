package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

	public static void main1(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Address address = new Address("city", "street", "zipcode");

			Member member1  = new Member();
			member1.setUsername("member1");
			member1.setHomeAddress(address);
			em.persist(member1);

			Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
			member1.setHomeAddress(newAddress);

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
