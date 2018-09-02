package com.utils;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.models.CurrencyPair;

public class CurrencyPairUtility {

	static private SessionFactory sessionFactory;

	public CurrencyPairUtility() {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			sessionFactory = config.buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Exception occurred at " + this.getClass().getName() + " and the error was " + e);
		}
	}

	public CurrencyPair addCurrencyPair(String currencyName) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CurrencyPair currencyPair = new CurrencyPair();
			currencyPair.setName(currencyName);
			session.save(currencyPair);
			transaction.commit();
			return currencyPair;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public CurrencyPair getCurrencyPairById(Integer id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CurrencyPair currencyPair = session.get(CurrencyPair.class, id);
			transaction.commit();
			return currencyPair;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public CurrencyPair getCurrencyPairByName(String name) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<CurrencyPair> currencyPairList = session.createQuery("from CurrencyPair where name='" + name + "'")
					.list();
			transaction.commit();
			return currencyPairList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CurrencyPair> getListOfCurrencyPair() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<CurrencyPair> currencyPairList = session.createQuery("from CurrencyPair").list();
			transaction.commit();
			return currencyPairList;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

}
