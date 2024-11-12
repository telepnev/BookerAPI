package core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    public String firstname;
    public String lastname;
    public Integer totalprice;
    public Boolean depositpaid;
    public Bookingdates bookingdates;
    public String additionalneeds;
}
