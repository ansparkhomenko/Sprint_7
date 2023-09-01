package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    Response response;
    OrderServiceApi orderServiceApi;

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public CreateOrderParametrizedTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+78003553535", 5, "2023-08-30", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Mike", "Tyson", "Moscow", 2, "+79031234567", 2, "2023-08-30", "Comment", new String[]{"GRAY"}},
                {"Борис", "Бритва", "Тверская", 3, "+79031234567", 3, "1990-09-09", " ", new String[]{"BLACK", "GRAY"}},
                {"Федор", "Михайлов", "   ", 5, "+79031234567", 1, "2033-12-31", "Комментарий", new String[]{}}
        };
    }

    @Before
    public void init() {
        orderServiceApi = new OrderServiceApi();
    }

    @Test
    @DisplayName("Проверка создания заказа")
    @Description("Создание заказа с разными параметрами")
    public void createOrderTest() {
        OrderData orderData = new OrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());


    }
}
