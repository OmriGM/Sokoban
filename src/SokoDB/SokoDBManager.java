package SokoDB;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class SokoDBManager {
	private static SokoDBManager instance = new SokoDBManager();
	
	private static void searchScoresWhoseNameStartsWith(String prefix) {
		Session session = factory.openSession();
		Query query = session.createQuery("from SokoTable where Username LIKE :prefix");
		query.setParameter("prefix", prefix + "%");
		List<Score> list = query.list();
		for (Score e : list) {
			System.out.println(e.getUserName());
		}
		session.close();
	}
	
	public static SokoDBManager getInstance() {
		return instance;
	}
	private static SessionFactory factory;
	
	private SokoDBManager() {
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}
	
	public void addScore(Score score){
		Session session = null;
		Transaction tx = null;
		
		try {
			session = factory.openSession();
			tx = session.beginTransaction();
						
			session.save(score);
			tx.commit();			
		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			System.out.println(ex.getMessage());
		}
		finally {
			if (session != null)
				session.close();
		}		
		
	}
	// Method to print all Scores
	private static void printLevelScore(String Levelname,int num) {
		Session session = factory.openSession();
		try {
			Query<Score> query = session.createQuery("from SokoTable where [Level] =:Levelname Order by Steps");
			query.setParameter("Levelname",Levelname);
			List<Score> list = query.list();
			
			for (Score e : list) {
				
				System.out.println(e.getUserName());
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	public void close() {
		factory.close();
	}
}
