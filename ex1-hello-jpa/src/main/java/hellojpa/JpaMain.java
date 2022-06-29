package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("member1");
			em.persist(member);

			team.addMember(member);

			em.flush();
			em.clear();

			Team findTeam = em.find(Team.class, team.getId());
			List<Member> members = findTeam.getMembers();

			System.out.println("============");
//			for (Member member1 : members) {
//				System.out.println("m = " + member1.getUsername());
//			}
			System.out.println("members = " + findTeam);
			System.out.println("============");


			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			//close
			em.close();
		}

		emf.close();

	}
}
