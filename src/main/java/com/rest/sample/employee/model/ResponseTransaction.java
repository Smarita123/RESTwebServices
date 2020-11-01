package com.rest.sample.employee.model;

public class ResponseTransaction {
	private Transaction transaction;
	private Card card;
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	

}
