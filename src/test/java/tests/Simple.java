package tests;

import helpers.HelperBooking;
import core.models.NewBooking;
import org.junit.jupiter.api.Test;

public class Simple {
    public HelperBooking helperBooking;
    public NewBooking booking;

    @Test
    public void simTest() {
        booking = helperBooking.createNewRandomBooking();
        System.out.println(booking);
    }
}
