package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class LoginCourierTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    CourierServiceApi courierServiceApi = new CourierServiceApi();
    CourierData courierData;
    Response response;

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Авторизацияя курьера с валидными данными")
    public void loginCourier_Success() {
        courierData = new CourierData("CourierLogin9991112", "123", "Asd");
        courierServiceApi.createCourier(courierData);
        response = courierServiceApi.loginCourier(courierData);
        response.then()
                .statusCode(200)
                .log().all()
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Неуспешная аторизация курьера. Запрос без логина.")
    @Description("Попытка авторизации курьера без логина")
    public void loginCourier_EmptyLogin_UnSuccess() {
        courierData = new CourierData("CourierLogin9991112", "123", "Asd");
        courierServiceApi.createCourier(courierData);
        response = courierServiceApi.loginCourier(new CourierData("" , "123", "Asd"));
        response.then()
                .statusCode(400)
                .log().all()
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неуспешная аторизация курьера. Запрос без пароля.")
    @Description("Попытка авторизации курьера без пароля")
    public void loginCourier_EmptyPassword_UnSuccess() {
        courierData = new CourierData("CourierLogin9991112", "123", "Asd");
        courierServiceApi.createCourier(courierData);
        response = courierServiceApi.loginCourier(new CourierData("CourierLogin9991112" , "", "Asd"));
        response.then()
                .statusCode(400)
                .log().all()
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Неуспешная авторизация курьера. Логин не существует.")
    @Description("Попытка авторизации курьера с несуществующим логином")
    public void loginCourier_CourierIsNotCreated_UnSuccess() {
        courierData = new CourierData("CourierMike", "222", "Asd");
        response = courierServiceApi.loginCourier(courierData);
        response.then()
                .statusCode(404)
                .log().all()
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
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
