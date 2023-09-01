package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    CourierServiceApi courierServiceApi = new CourierServiceApi();
    CourierData courierData;
    Response response;

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Создание курьера с валидными данными")
    public void createCourier_Success() {
        courierData = new CourierData("Courier11qwe123123", "pass1234", "Mike");
        response = courierServiceApi.createCourier(courierData);
        response.then()
                .statusCode(201)
                .log().all()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Неуспешное создание курьера. Запрос без логина.")
    @Description("Попытка создания курьера без логина")
    public void createCourier_EmptyLogin_UnSuccess() {
        courierData = new CourierData("", "1414", "Mike");
        response = courierServiceApi.createCourier(courierData);
        response.then()
                .statusCode(400)
                .log().all()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Неуспешное создание курьера. Запрос без пароля.")
    @Description("Попытка создания курьера без пароля")
    public void createCourier_EmptyPassword_UnSuccess() {
        courierData = new CourierData("Courier112qwe123123", "", "Mike");
        response = courierServiceApi.createCourier(courierData);
        response.then()
                .statusCode(400)
                .log().all()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Неуспешное создание курьера. Логин курьера уже занят.")
    @Description("Попытка создания курьера с уже существующим логином")
    public void createCourier_duplicateLogin_UnSuccess() {
        courierData = new CourierData("Courier112qwe123123", "111", "Mike");
        response = courierServiceApi.createCourier(courierData);
        response = courierServiceApi.createCourier(courierData);
        response.then()
                .statusCode(409)
                .log().all()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @After
    public void cleanUp() {
        Response loginResponse = courierServiceApi.loginCourier(courierData);
        int statusCode = loginResponse.then().extract().statusCode();
        if (statusCode == 200) {
            String id = courierServiceApi.getCourierId(loginResponse);
            courierServiceApi.deleteCourier(id);
        }
    }
}
