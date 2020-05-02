package types;

public class Customer {
    private final long id;
	private String firstName, lastName;
	private String email;
	private String phone;
	
	public Customer(long id, String firstName, String lastName, String email, String phone) {
	    this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}
	
//	public Customer(String firstName, String lastName, String email) {
//	    this(firstName, lastName, email, null);
//	}
	
	public long getId() {
	    return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s", firstName, lastName, email, phone);
	}
	
	@Override
	public Customer clone() {
	    return new Customer(id, firstName, lastName, email, phone);
	}
}
