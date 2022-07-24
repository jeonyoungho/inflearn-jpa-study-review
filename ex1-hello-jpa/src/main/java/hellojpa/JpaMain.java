package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member  = new Member();
			member.setUsername("hello");
			member.setWorkAddress(new Address("city", "street", "zipcode"));
			member.setWorkPeriod(new Period());
			em.persist(member);

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
