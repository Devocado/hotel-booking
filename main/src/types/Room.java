package types;

import java.math.BigDecimal;

public enum Room {
    
    BLUE_ROOM(new BigDecimal(1000), 6, "Blue Room"),
    GREEN_ROOM(new BigDecimal(1000), 6, "Green Room"),
    YELLOW_ROOM(new BigDecimal(1000), 6, "Yellow Room");
    
    Room(BigDecimal pricePerNight, int maxGuests, String name) {
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.name = name;
    }
    
    private BigDecimal pricePerNight;
    private int maxGuests;
    private String name;
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public int getMaxGuests() {
        return maxGuests;
    }
    
    public String getName() {
        return name;
    }

}
