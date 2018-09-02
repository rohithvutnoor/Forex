package com.utils;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.models.CurrencyPair;
import com.models.CurrencyPairPrice;

public class CurrencyPairPriceUtility {

	static private SessionFactory sessionFactory;
	private CurrencyPairUtility currencyPairUtility;

	public CurrencyPairPriceUtility() {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			sessionFactory = config.buildSessionFactory();
			currencyPairUtility = new CurrencyPairUtility();
		} catch (Exception e) {
			System.out.println("Exception occurred at " + this.getClass().getName() + " and the error was " + e);
		}
	}

	public CurrencyPairPrice addCurrencyPairPrice(Integer currencyPairId, Date onDate, Double sellingPrice,
			Double buyingPrice) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CurrencyPair currencyPair = currencyPairUtility.getCurrencyPairById(currencyPairId);
			CurrencyPairPrice currencyPairPrice = new CurrencyPairPrice();
			currencyPairPrice.setCurrencyPair(currencyPair);
			currencyPairPrice.setOnDate(onDate);
			currencyPairPrice.setSellingPrice(sellingPrice);
			currencyPairPrice.setBuying_price(buyingPrice);
			session.save(currencyPairPrice);
			transaction.commit();
			return currencyPairPrice;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public CurrencyPairPrice getCurrencyPairPrice(Integer id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CurrencyPairPrice currencyPairPrice = session.get(CurrencyPairPrice.class, id);
			transaction.commit();
			return currencyPairPrice;
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
	public CurrencyPairPrice getCurrencyPairPrice(Date onDate, String currencyPairName) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CurrencyPair currencyPair = currencyPairUtility.getCurrencyPairByName(currencyPairName);
			List<CurrencyPairPrice> currencyPairPriceList = session.createQuery(
					"from CurrencyPairPrice where onDate='" + onDate + "' and currencyPair='" + currencyPair + "'")
					.list();
			transaction.commit();
			return currencyPairPriceList.get(0);
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
	public List<CurrencyPairPrice> getList(Date onDate) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<CurrencyPairPrice> currencyPairPriceList = session
					.createQuery("from CurrencyPairPrice where onDate='" + onDate + "'").list();
			transaction.commit();
			return currencyPairPriceList;
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
