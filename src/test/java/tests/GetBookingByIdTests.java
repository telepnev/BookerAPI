package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByIdTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingByIdNotEmpty() throws JsonProcessingException {
        Response response = apiClient.getBookingById(3);
        //десериализуем тело ответа в список обьектов
        String responseBody = response.getBody().asString();
        BookingById bookingsById = objectMapper.readValue(responseBody, BookingById.class);

        assertThat(response.getStatusCode()).isEqualTo(200);
        // пока такой тест, дороботать после создания Booking
        assertThat(bookingsById.getFirstname()).isNotEmpty();
        assertThat(bookingsById.getLastname()).isNotEmpty();
        assertThat(bookingsById.getTotalprice()).isNotNull();
        assertThat(bookingsById.getDepositpaid()).isNotNull();
        assertThat(bookingsById.getBookingdates()).isNotNull();
        assertThat(bookingsById.getAdditionalneeds()).isNotEmpty();

    }
}
