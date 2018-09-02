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
@Table(name = "currency_pair_prices")
public class CurrencyPairPrice {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private CurrencyPair currencyPair;

	@Column(name = "on_date")
	private Date onDate;

	@Column(name = "selling_price")
	private Double sellingPrice;

	@Column(name = "buying_price")
	private Double buying_price;

	public CurrencyPairPrice() {
		super();
	}

	public CurrencyPairPrice(CurrencyPair currencyPair, Date onDate, Double sellingPrice, Double buying_price) {
		super();
		this.currencyPair = currencyPair;
		this.onDate = onDate;
		this.sellingPrice = sellingPrice;
		this.buying_price = buying_price;
	}

	public Integer getId() {
		return id;
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(CurrencyPair currencyPair) {
		this.currencyPair = currencyPair;
	}

	public Date getOnDate() {
		return onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Double getBuying_price() {
		return buying_price;
	}

	public void setBuying_price(Double buying_price) {
		this.buying_price = buying_price;
	}

	@Override
	public String toString() {
		return "CurrencyPairPrice [id=" + id + ", currencyPair=" + currencyPair + ", onDate=" + onDate
				+ ", sellingPrice=" + sellingPrice + ", buying_price=" + buying_price + "]";
	}

}
