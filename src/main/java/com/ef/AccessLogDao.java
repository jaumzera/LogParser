package com.ef;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AccessLogDao {
	
	private static AccessLogDao instance;
	
	public static final AccessLogDao instance() {
		if(instance == null) {
			instance = new AccessLogDao();
		}
		return instance;
	}
	
	private EntityManagerFactory emf;

	private AccessLogDao() {
		emf = Persistence.createEntityManagerFactory("accessLogPU");
	}
	
	public void save(Collection<?> logEntries) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		logEntries.forEach(em::persist);
		em.getTransaction().commit();
		em.close();
	}

	public void dispose() {
		emf.close();
		this.emf = null;
		instance = null;
	}
}
