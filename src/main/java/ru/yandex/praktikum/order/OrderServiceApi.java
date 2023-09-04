package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderServiceApi {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_ORDER_URI = "/api/v1/orders";
    private static final String GET_ORDER_LIST_URI = "/api/v1/orders";
    private static final String CANCEL_ORDER_URI = "/api/v1/orders/cancel";

    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();

    @Step("Создание заказа")
    public Response createOrder(OrderData orderData) {
        return given()
                .spec(requestSpec)
                .body(orderData)
                .post(CREATE_ORDER_URI);
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .spec(requestSpec)
                .get(GET_ORDER_LIST_URI);
    }

    @Step("Отмена заказа")
    public Response cancelOrder(String track) {
        return given()
                .spec(requestSpec)
                .body("{\"track\":" + track + "}")
                .put(CANCEL_ORDER_URI);
    }
}
