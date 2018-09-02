package com.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@ManyToOne
	private User user;

	@ManyToOne
	private CurrencyPair currencyPair;

	@Column(name = "ordered_date")
	private Date orderedDate;

	@Column(name = "notional_amount")
	private Double notionalAmount;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private OrderType type;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public Order() {
		super();
	}

	public Order(User user, CurrencyPair currencyPair, Date orderedDate, Double notionalAmount, OrderType type,
			OrderStatus status) {
		super();
		this.user = user;
		this.currencyPair = currencyPair;
		this.orderedDate = orderedDate;
		this.notionalAmount = notionalAmount;
		this.type = type;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(CurrencyPair currencyPair) {
		this.currencyPair = currencyPair;
	}

	public Date getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}

	public Double getNotionalAmount() {
		return notionalAmount;
	}

	public void setNotionalAmount(Double notionalAmount) {
		this.notionalAmount = notionalAmount;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", currencyPair=" + currencyPair + ", orderedDate=" + orderedDate
				+ ", notionalAmount=" + notionalAmount + ", type=" + type + ", status=" + status + "]";
	}

}
