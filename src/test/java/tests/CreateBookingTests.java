package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Bookingdates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateBookingTests {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private NewBooking newBooking;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        newBooking = new NewBooking();
        newBooking.setFirstname("Evgen");
        newBooking.setLastname("Telepnev");
        newBooking.setTotalprice(900);
        newBooking.setDepositpaid(false);
        newBooking.setBookingdates(new Bookingdates("2024-12-12", "2024-12-13"));
        newBooking.setAdditionalneeds("bear");
    }

    @Test
    public void createBookingTest() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);

        assertThat(createdBooking.getBookingid()).isNotNull();
        assertThat(newBooking.getFirstname()).isEqualTo(createdBooking.getBooking().getFirstname());
        assertThat(newBooking.getLastname()).isEqualTo(createdBooking.getBooking().getLastname());
        assertThat(newBooking.getTotalprice()).isEqualTo(createdBooking.getBooking().getTotalprice());
        assertThat(newBooking.getDepositpaid()).isEqualTo(createdBooking.getBooking().getDepositpaid());

        assertThat(newBooking.getBookingdates().getCheckin())
                .isEqualTo(createdBooking.getBooking().getBookingdates().getCheckin());
        assertThat(newBooking.getBookingdates().getCheckout())
                .isEqualTo(createdBooking.getBooking().getBookingdates().getCheckout());

        assertThat(newBooking.getAdditionalneeds()).isEqualTo(createdBooking.getBooking().getAdditionalneeds());
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createdBooking.getBookingid());

        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode())
                .isEqualTo(404);
    }
}
