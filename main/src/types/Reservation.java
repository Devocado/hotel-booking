package types;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Reservation {
    
    private final long id = -1;
	private Customer customer;
	private LocalDate startDate, endDate;
	private BigDecimal totalCost;
	private List<Room> rooms;
	
//	public Reservation(Customer cust, LocalDateTime start, LocalDateTime end, Room... rooms) {
//		this.customer = cust;
//		this.startDate = start;
//		this.endDate = end;
//		this.rooms = Arrays.asList(rooms);
//	}
	
	public Reservation(int id, Customer cust, LocalDate start, LocalDate end, List<Room> rooms) {
        this.customer = cust;
        this.startDate = start;
        this.endDate = end;
        this.rooms = rooms;
    }
	
	public Reservation(Customer cust, LocalDate start, LocalDate end, List<Room> rooms) {
	    this(-1, cust, start, end, rooms);
	}
	
	public long getId() {
	    return id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDate getStart() {
		return startDate;
	}
	public void setStart(LocalDate start) {
		this.startDate = start;
	}
	public LocalDate getEnd() {
		return endDate;
	}
	public void setEnd(LocalDate end) {
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
