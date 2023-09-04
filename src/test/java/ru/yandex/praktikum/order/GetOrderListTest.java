package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class GetOrderListTest {
    Response response;
    OrderServiceApi orderServiceApi;
    OrderData orderData;

    @Before
    public void init() {
        orderServiceApi = new OrderServiceApi();
        orderData = new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+78003553535", 5, "2023-08-30", "Saske, come back to Konoha", new String[]{"BLACK"});
        orderServiceApi.createOrder(orderData);
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Проверка что в тело ответа возвращается список заказов. Список заказова больше 0.")
    public void orderList_IsNotEmpty() {
        response = orderServiceApi.getOrderList();
        response.then()
                .log().all()
                .statusCode(200)
                .and()
                .body("orders", hasSize(greaterThan(0)));
    }

    @After
    public void cleanUp() {
        orderServiceApi.cancelOrder(response.then().extract().path("track"));
    }
}
