package sjc.tds.carpark.models.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum VehicleType {
    SMALL(1, new BigDecimal("0.1")),
    MEDIUM(2, new BigDecimal("0.2")),
    LARGE(3, new BigDecimal("0.4"));


    private final int id;
    private final BigDecimal charge;

    VehicleType(int id, BigDecimal charge) {
        this.id = id;
        this.charge = charge;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public static VehicleType fromId(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid vehicle type: " + id));
    }
}
