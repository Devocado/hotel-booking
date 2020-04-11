package types;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Reservation {
    
//    private long id = -1;
	private Customer customer;
	private LocalDateTime startDate, endDate;
	private BigDecimal totalCost;
	private List<Room> rooms;
	
	public Reservation(Customer cust, LocalDateTime start, LocalDateTime end, Room... rooms) {
		this.customer = cust;
		this.startDate = start;
		this.endDate = end;
		this.rooms = Arrays.asList(rooms);
	}
	
//	public long getId() {
//	    return id < 0 ? -1 : id;
//	}
	
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
	public List<Room> getRooms() {
        return List.copyOf(rooms);
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
	public String toString() {
	    return customer.getFirstName()+" "+customer.getLastName()+" "+startDate+" "+endDate+" "+rooms;
	}
}
