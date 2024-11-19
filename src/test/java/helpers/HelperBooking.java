package helpers;

import core.models.Bookingdates;
import core.models.NewBooking;
import java.util.Random;


public class HelperBooking {

   public static NewBooking createNewRandomBooking() {
       Random rand = new Random();
       int randomValue = rand.nextInt(1000);
       return NewBooking.builder()
                .firstname("Evgen" + randomValue)
                .lastname("Telepnev" + randomValue)
                .totalprice(randomValue)
                .depositpaid(false)
                .bookingdates(Bookingdates.builder()
                        .checkin("2025-01-01")
                        .checkout("2025-01-01")
                        .build())
                .additionalneeds("Beer and fish")
                .build();
    }

}
