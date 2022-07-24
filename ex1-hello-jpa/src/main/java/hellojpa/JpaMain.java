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
			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);

			Team teamB = new Team();
			teamB.setName("teamB");
			em.persist(teamB);

			Member member1 = new Member();
			member1.setUsername("member1");
			em.persist(member1);
			member1.setTeam(teamA);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("member2");
			em.persist(member2);
			member2.setTeam(teamB);
			em.persist(member2);

			em.flush();
			em.clear();

//			Member m = em.find(Member.class, member1.getId());
			List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
					.getResultList();

			for (Member member : members) {
				System.out.println("member = " + member.getTeam().getName());
			}


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
