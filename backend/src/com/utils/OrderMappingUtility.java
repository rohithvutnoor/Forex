package com.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.models.Order;
import com.models.OrderMapping;

public class OrderMappingUtility {
	static private SessionFactory sessionFactory;

	public OrderMappingUtility() {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			sessionFactory = config.buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Exception occurred at " + this.getClass().getName() + " and the error was " + e);
		}

	}

	public OrderMapping addTransaction(Order buyOrder,Order sellOrder) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			OrderMapping orderMapping = new OrderMapping();
			orderMapping.setValueDate(buyOrder.getOrderedDate());
			orderMapping.setBuyOrder(buyOrder);
			orderMapping.setSellOrder(sellOrder);
			session.save(orderMapping);
			transaction.commit();
			return orderMapping;
		} catch (Exception e) {
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}
	
}
