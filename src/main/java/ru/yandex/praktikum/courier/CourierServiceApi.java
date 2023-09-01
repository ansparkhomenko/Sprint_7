package ru.yandex.praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;

public class CourierServiceApi {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_COURIER_URI = "/api/v1/courier";
    private static final String LOGIN_COURIER_URI = "/api/v1/courier/login";
    private static final String DELETE_COURIER_URI = "/api/v1/courier/";

    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();

    @Step("Создание курьера")
    public Response createCourier(CourierData courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .post(CREATE_COURIER_URI);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierData courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .post(LOGIN_COURIER_URI);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return given()
                .spec(requestSpec)
                .delete(DELETE_COURIER_URI + id);
    }

    @Step("Получение идентификатора курьера")
    public String getCourierId(Response response) {
        return Integer.toString(response.then().extract().path("id"));
    }

}
