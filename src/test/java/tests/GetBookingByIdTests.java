package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Создание Бронирования")
public class GetBookingByIdTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Feature("Список бронирования")
    @Owner("telepneves")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение списка бронирования по ID")
    public void testGetBookingByIdNotEmpty() throws JsonProcessingException {
        Random rand = new Random();
        int value = rand.nextInt(100);

        Response response = apiClient.getBookingById(value);
        //десериализуем тело ответа в список обьектов
        String responseBody = response.getBody().asString();
        Booking bookingsById = objectMapper.readValue(responseBody, Booking.class);

        // пока такой тест, дороботать после создания Booking
        assertThat(bookingsById.getFirstname()).isNotEmpty();
        assertThat(bookingsById.getLastname()).isNotEmpty();
        assertThat(bookingsById.getTotalprice()).isNotNull();
        assertThat(bookingsById.getDepositpaid()).isNotNull();
        assertThat(bookingsById.getBookingdates()).isNotNull();
        assertThat(bookingsById.getAdditionalneeds()).isNotEmpty();
        assertThat(bookingsById.getAdditionalneeds()).isNotEmpty();

    }
}
