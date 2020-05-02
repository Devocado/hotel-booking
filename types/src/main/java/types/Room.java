package types;

import java.math.BigDecimal;

public class Room {
    
    private long roomId;
    private BigDecimal pricePerNight;
    private int maxGuests;

    
    public Room(long roomId, BigDecimal pricePerNight, int maxGuests) {
        this.roomId = roomId;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public int getMaxGuests() {
        return maxGuests;
    }
    
    public long getRoomNumber() {
        return roomId;
    }
    
    @Override
    public String toString() {
        return String.format("%d %.2f %d", roomId, pricePerNight, maxGuests);
    }

}
