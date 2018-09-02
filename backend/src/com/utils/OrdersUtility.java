package com.utils;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.models.CurrencyPair;
import com.models.Order;
import com.models.OrderMapping;
import com.models.OrderStatus;
import com.models.OrderType;
import com.models.RoleEnum;
import com.models.User;

/*
 * This is a wrapper class for order model where it does CRUD operations and do some other operations on orders
 */
public class OrdersUtility {
	static private SessionFactory sessionFactory;
	private UserUtilities userUtil;
	private CurrencyPairUtility currencyPairUtility;
	private OrderMappingUtility orderMappingUtility;

	public OrdersUtility() {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			sessionFactory = config.buildSessionFactory();
			userUtil = new UserUtilities();
			currencyPairUtility = new CurrencyPairUtility();
			orderMappingUtility = new OrderMappingUtility();
		} catch (Exception e) {
			System.out.println("Exception occurred at " + this.getClass().getName() + " and the error was " + e);
		}

	}

	/*
	 * Adds order into database by taking
	 * 
	 * @param notionalAmount
	 * 
	 * @param orderedDate
	 * 
	 * @param status
	 * 
	 * @param type
	 * 
	 * @param currencyPair_id
	 * 
	 * @param user_id
	 */
	public Order addOrder(Double notionalAmount, Date orderedDate, OrderStatus status, OrderType type,
			Integer currencyPair_id, Integer user_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = userUtil.getUserById(user_id);
			CurrencyPair currencyPair = currencyPairUtility.getCurrencyPairById(currencyPair_id);
			Order order = new Order();
			order.setNotionalAmount(notionalAmount);
			order.setOrderedDate(orderedDate);
			order.setStatus(status);
			order.setType(type);
			order.setCurrencyPair(currencyPair);
			order.setUser(user);
			session.save(order);
			transaction.commit();
			return order;
		} catch (Exception e) {
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	/*
	 * Modifies order of type BUY to EXECUTED if it finds a sell order with same
	 * specification or to CANCELED
	 */
	@SuppressWarnings("unchecked")
	public OrderMapping executeOrCancelBuyOrder(Order buyOrder) {
		Session session = null;
		OrderMapping orderMapping = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			String query = String.format(
					"from Order where currencyPair=%d and type='%s' and notionalAmount=%f and status='%s and orderedDate=now()'",
					buyOrder.getCurrencyPair().getId(), OrderType.SELL, buyOrder.getNotionalAmount(),
					OrderStatus.OPENED);
			List<Order> sellOrders = (List<Order>) session.createQuery(query).list();
			if (sellOrders.isEmpty()) {
				buyOrder.setStatus(OrderStatus.CANCELED);
				session.update(buyOrder);
			} else {
				Order sellOrder = sellOrders.get(0);
				orderMapping = orderMappingUtility.addTransaction(buyOrder, sellOrder);
				buyOrder.setStatus(OrderStatus.EXECUTED);
				sellOrder.setStatus(OrderStatus.EXECUTED);
				session.update(buyOrder);
				session.update(sellOrder);
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return orderMapping;
	}

	/*
	 * Get orders of a particular user and also based filters provides as params
	 * 
	 * @param user
	 * 
	 * @param orderStatuses
	 * 
	 * @param dateInMilliSec
	 * 
	 * @param orderType
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getOrders(User user, List<OrderStatus> orderStatuses, String dateInMilliSec,
			OrderType orderType) {
		Session session = null;
		List<Order> orders = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			Criteria criteria = session.createCriteria(Order.class);
			Criterion userCriterion = Restrictions.and(Restrictions.eq("user", user));
			Criterion statusCriterion = Restrictions.in("status", orderStatuses);
			criteria.add(userCriterion);
			criteria.add(statusCriterion);
			if (dateInMilliSec != null && !dateInMilliSec.equals("")) {
				Date date = new Date(Long.parseLong(dateInMilliSec));
				Criterion dateCriterion = Restrictions.and(Restrictions.eq("orderedDate", date));
				criteria.add(dateCriterion);
			}
			if (orderType != null) {
				Criterion typeCriterion = Restrictions.and(Restrictions.eq("type", orderType));
				criteria.add(typeCriterion);
			}
			orders = (List<Order>) criteria.list();
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Following error was occurred at getOrders method in class " + this.getClass().getName()
					+ " : " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return orders;
	}

	/*
	 * Get all orders of particular curencyPairID and orderDate for admin
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getAllOrders(User user, Integer currencyPairId, Date orderDate) {
		Session session = null;
		List<Order> orders = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			if (user.getRole().getRole().equals(RoleEnum.ADMIN)) {
				String query = String.format("from Order o where o.currencyPair.id=%d and o.orderedDate='%s'",
						currencyPairId, orderDate);
				orders = (List<Order>) session.createQuery(query).list();
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Following error was occurred at getAllOrders method in class "
					+ this.getClass().getName() + " : " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return orders;
	}

	/*
	 * Gives positions of a particular currencyPairId and orderDate orders for admin
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> positions(User user, Integer currencyPairId, Date orderDate) {
		Map<String, String> positions = new HashMap<String, String>();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			if (user.getRole().getRole().equals(RoleEnum.ADMIN)) {
				String query = String.format("select sum(o.notionalAmount) from Order o where o.currencyPair.id=%d and "
						+ "o.orderedDate='%s' and o.type='%s'", currencyPairId, orderDate, OrderType.SELL);
				List<Object> totalSellNotionalAmount = session.createQuery(query).list();
				if (!totalSellNotionalAmount.isEmpty() && totalSellNotionalAmount.get(0) != null) {
					positions.put("totla_sell_orders_amount", totalSellNotionalAmount.get(0).toString());
				} else {
					positions.put("totla_sell_orders_amount", "0");
				}
				query = String.format("select sum(o.notionalAmount) from Order o where o.currencyPair.id=%d and "
						+ "o.orderedDate='%s' and o.type='%s'", currencyPairId, orderDate, OrderType.BUY);
				List<Object> totalBuyNotionalAmount = session.createQuery(query).list();
				if (!totalBuyNotionalAmount.isEmpty() && totalBuyNotionalAmount.get(0) != null) {
					positions.put("totla_buy_orders_amount", totalBuyNotionalAmount.get(0).toString());
				} else {
					positions.put("totla_buy_orders_amount", "0");
				}
				System.out.println("totalBuyNotionalAmount " + totalBuyNotionalAmount);
				System.out.println("totalSellNotionalAmount " + totalSellNotionalAmount);
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Following error was occurred at positions method in class " + this.getClass().getName()
					+ " : " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return positions;
	}

}
