package types;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Reservation {
	Customer customer;
	LocalDateTime start, end;
	BigDecimal totalCost;
	
	public Reservation(Customer cust, LocalDateTime start, LocalDateTime end) {
		this.customer = cust;
		this.start = start;
		this.end = end;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
}
