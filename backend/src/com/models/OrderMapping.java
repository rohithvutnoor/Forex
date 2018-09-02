package com.models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders_mapping")
public class OrderMapping {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private Order buyOrder;

	@OneToOne(cascade = CascadeType.ALL)
	private Order sellOrder;

	@Column(name = "value_date")
	private Date valueDate;

	public OrderMapping() {
		super();
	}

	public OrderMapping(Order buyOrder, Order sellOrder, Date valueDate) {
		super();
		this.buyOrder = buyOrder;
		this.sellOrder = sellOrder;
		this.valueDate = valueDate;
	}

	public Integer getId() {
		return id;
	}

	public Order getBuyOrder() {
		return buyOrder;
	}

	public void setBuyOrder(Order buyOrder) {
		this.buyOrder = buyOrder;
	}

	public Order getSellOrder() {
		return sellOrder;
	}

	public void setSellOrder(Order sellOrder) {
		this.sellOrder = sellOrder;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	@Override
	public String toString() {
		return "OrderMapping [id=" + id + ", buyOrder=" + buyOrder + ", sellOrder=" + sellOrder + ", valueDate="
				+ valueDate + "]";
	}

}
