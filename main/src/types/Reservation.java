package types;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Reservation {
    
    private long id = -1;
	private Customer customer;
	private LocalDateTime startDate, endDate;
	private BigDecimal totalCost;
	private Room[] rooms;
	
	public Reservation(Customer cust, LocalDateTime start, LocalDateTime end, Room... rooms) {
		this.customer = cust;
		this.startDate = start;
		this.endDate = end;
		this.rooms = rooms;
	}
	
	public long getId() {
	    return id < 0 ? -1 : id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDateTime getStart() {
		return startDate;
	}
	public void setStart(LocalDateTime start) {
		this.startDate = start;
	}
	public LocalDateTime getEnd() {
		return endDate;
	}
	public void setEnd(LocalDateTime end) {
		this.endDate = end;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	
	public Room[] getRooms() {
        return rooms.clone();
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    @Override
	public String toString() {
	    return customer.getFirstName()+" "+customer.getLastName()+" "+startDate+" "+endDate;
	}
}
