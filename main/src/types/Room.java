package types;

import java.math.BigDecimal;

public class Room {
    
    private int roomNumber;
    private BigDecimal pricePerNight;
    private int maxGuests;

    
    public Room(int roomNumber, BigDecimal pricePerNight, int maxGuests) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public int getMaxGuests() {
        return maxGuests;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    @Override
    public String toString() {
        return String.format("%d %.2f %d", roomNumber, pricePerNight, maxGuests);
    }

}
