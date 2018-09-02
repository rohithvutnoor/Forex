package com.utils;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.models.Role;
import com.models.RoleEnum;
import com.models.User;

public class UserUtilities {

	static private SessionFactory sessionFactory;

	public UserUtilities() {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			sessionFactory = config.buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Exception occurred at " + this.getClass().getName() + " and the error was " + e);
		}
	}

	public User addUser(String firstname, String lastname, String email, String password) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = new User();
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setEmail(email);
			user.setPassword(EncryptDecrypt.getEncrypted(password));
			user.setRole(getRole(RoleEnum.TRADER));
			session.save(user);
			transaction.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception ocured while saving user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public void updateUser(Integer userId, String firstname, String lastname, String password) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = (User) session.get(User.class, userId);
			if (firstname != null && !firstname.isEmpty())
				user.setFirstname(firstname);
			if (lastname != null && !lastname.isEmpty())
				user.setLastname(lastname);
			if (password != null && !password.isEmpty())
				user.setPassword(EncryptDecrypt.getEncrypted(password));
			session.update(user);
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception ocured while update a user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	public User getUserById(Integer id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = (User) session.get(User.class, id);
			transaction.commit();
			return user;
		} catch (Exception e) {
			System.out.println("Exception ocured while geting a user by id and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public User getUserByEmail(String email) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<User> users = session.createQuery("From User where email='" + email + "'").list();
			transaction.commit();
			if (users.isEmpty())
				return null;
			else
				return users.get(0);
		} catch (Exception e) {
			System.out.println("Exception ocured while getting a user by using email and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public Boolean validateUser(String email, String password) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = getUserByEmail(email);
			if (user != null) {
				if (EncryptDecrypt.getDecrypt(user.getPassword()).equals(password))
					return true;
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception ocured while validating a user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<User> listUsers() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<User> users = (List<User>) session.createQuery("from User").list();
			transaction.commit();
			return users;
		} catch (Exception e) {
			System.out.println("Exception ocured while fetching list of users and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public User deleteUser(Integer id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			User user = session.get(User.class, id);
			session.delete(user);
			transaction.commit();
			return user;
		} catch (Exception e) {
			System.out.println("Exception ocured while deleting a user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}

	public void updateToken(User user) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			user.setToken(user.getEmail());
			session.update(user);
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception ocured while deleting a user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Role getRole(RoleEnum roleEnum) {
		Role role = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			List<Role> rolesList = session.createQuery("from Role where role='" + roleEnum.name() + "'").list();
			role = rolesList.get(0);
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception ocured while deleting a user and exception was " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return role;
	}

}
