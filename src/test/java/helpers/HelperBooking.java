package helpers;

import core.models.Bookingdates;
import core.models.NewBooking;

import java.util.Random;


public class HelperBooking {


    public static NewBooking createNewRandomBooking() {
        Random rand = new Random();
        int randomValue = rand.nextInt(1000);
        int randomDateCheckIn = rand.nextInt(9);
        int randomDateCheckOut = rand.nextInt(15, 28);
        String checkin = String.format("2025-01-0%d", randomDateCheckIn);
        String checkout = String.format("2025-01-%d", randomDateCheckOut);

        return NewBooking.builder()
                .firstname("Evgen" + randomValue)
                .lastname("Telepnev" + randomValue)
                .totalprice(randomValue)
                .depositpaid(false)
                .bookingdates(Bookingdates.builder()
                        .checkin(checkin)
                        .checkout(checkout)
                        .build())
                .additionalneeds("Beer and fish" + randomValue)
                .build();
    }

}
